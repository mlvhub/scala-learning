package hellotapir

import sttp.tapir.*
import sttp.tapir.ztapir.{endpoint, stringBody, plainBody, RichZEndpoint}
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import zio.http.{endpoint => zendpoint, *}
import zio.*
import zio.metrics.*
import zio.metrics.connectors.prometheus.PrometheusPublisher
import zio.metrics.connectors.{MetricsConfig, prometheus}
import zio.metrics.connectors.{MetricsConfig, prometheus}
import zio.metrics.jvm.DefaultJvmMetrics

import hellotapir.metrics.MetricsEndpoint

object MainApp extends ZIOAppDefault {

  def memoryUsage: ZIO[Any, Nothing, Double] = {
    import java.lang.Runtime._
    ZIO
      .succeed(getRuntime.totalMemory() - getRuntime.freeMemory())
      .map(_ / (1024.0 * 1024.0)) @@ Metric.gauge("memory_usage")
  }

  def countCharacters(s: String): ZIO[Any, Nothing, Int] =
    ZIO.succeed(s.length)

  val countCharactersEndpoint: PublicEndpoint[String, Unit, Int, Any] =
    endpoint.in("count").in(stringBody).out(plainBody[Int])

  val countCharactersHttp: HttpApp[Any] =
    ZioHttpInterpreter().toHttp(
      countCharactersEndpoint.zServerLogic(countCharacters)
    )

  val httpServer = for {
    metricsEndpoints <- ZIO.service[MetricsEndpoint]
    httpApp = countCharactersHttp ++ metricsEndpoints.httpApp
    _ <- ZIO.logInfo("Starting server on port 8080")
    _ <- Server.install(httpApp)
    _ <- ZIO.never
  } yield ()

  override def run = httpServer
    .provide(
      // ZIO Http default server layer, default port: 8080
      Server.default,
      // The prometheus reporting layer
      prometheus.prometheusLayer,
      prometheus.publisherLayer,
      // Interval for polling metrics
      ZLayer.succeed(MetricsConfig(5.seconds)),
      // Default JVM Metrics
      DefaultJvmMetrics.live.unit,
      // App layers
      MetricsEndpoint.live
    )
}
