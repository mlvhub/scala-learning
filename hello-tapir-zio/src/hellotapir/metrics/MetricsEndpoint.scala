package hellotapir.metrics

import sttp.tapir.*
import zio.*
import zio.metrics.*
import zio.metrics.connectors.prometheus.PrometheusPublisher
import zio.http.{endpoint => zendpoint, *}
import sttp.tapir.ztapir.{
  endpoint,
  stringBody,
  plainBody,
  RichZEndpoint,
  ZPartialServerEndpoint
}
import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.jsonBody
import sttp.model.StatusCode
import sttp.tapir.server.ziohttp.ZioHttpInterpreter

import hellotapir.errors.*
import hellotapir.errors.AppError

class MetricsEndpoint(
    prometheusPublisher: PrometheusPublisher
):

  private val metricsEndpoint: PublicEndpoint[Unit, Unit, String, Any] =
    endpoint.get.in("metrics").out(stringBody)

  private val metricsHttp: HttpApp[Any] =
    ZioHttpInterpreter().toHttp(
      metricsEndpoint.zServerLogic(_ => prometheusPublisher.get)
    )

  private val memoryEndpoint: ZPartialServerEndpoint[
    Any,
    String,
    String,
    Unit,
    AppError,
    String,
    Any
  ] =
    endpoint.get
      .in("memory")
      .out(stringBody)
      .errorOut(MetricsEndpoint.defaultErrorOutputs)
      .securityIn(auth.bearer[String]())
      .zServerSecurityLogic(token =>
        if (token == "secret") {
          ZIO.succeed("AuthOK")
        } else {
          ZIO.fail(Unauthorized("Unauthorized"))
        }
      )

  def memoryUsage: ZIO[Any, Nothing, Double] = {
    import java.lang.Runtime._
    ZIO
      .succeed(getRuntime.totalMemory() - getRuntime.freeMemory())
      .map(_ / (1024.0 * 1024.0)) @@ Metric.gauge("memory_usage")
  }

  private val memoryHttp: HttpApp[Any] =
    ZioHttpInterpreter().toHttp(
      memoryEndpoint.serverLogic(token =>
        _ =>
          ZIO.logAnnotate("token", token) {
            for {
              _ <- ZIO.logInfo(token)
              _ <- memoryUsage
              time <- Clock.currentDateTime
            } yield s"$time\t/foo API called"
          }
      )
    )

  val httpApp: HttpApp[Any] = metricsHttp ++ memoryHttp

object MetricsEndpoint:
  val live: ZLayer[
    PrometheusPublisher,
    Nothing,
    MetricsEndpoint
  ] =
    ZLayer.fromFunction(MetricsEndpoint(_))

  val defaultErrorOutputs: EndpointOutput.OneOf[AppError, AppError] =
    oneOf[AppError](
      oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound])),
      oneOfVariant(
        statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized])
      ),
      oneOfVariant(
        statusCode(StatusCode.InternalServerError).and(
          jsonBody[InternalServerError]
        )
      )
    )
