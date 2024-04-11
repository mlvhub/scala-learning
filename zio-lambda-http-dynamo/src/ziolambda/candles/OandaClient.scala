package ziolambda.candles

import zio.*
import sttp.client3.*
import sttp.client3.ziojson._

import ziolambda.config.AppConfig

class OandaClient(config: AppConfig, backend: SttpBackend[Task, Any])
    extends Client:
  private val baseUri = uri"${config.oanda.host}/v3"
  private val headers = Map(
    "Content-Type" -> "application/json; charset=UTF-8",
    "Accept" -> "application/json; charset=UTF-8"
  )

  def candles(
      instrumentId: String,
      count: Int,
      granularity: String = "D"
  ): ZIO[AppConfig, Throwable, Candles] =
    val request =
      basicRequest
        .get(
          uri"$baseUri/instruments/$instrumentId/candles?count=$count&granularity=$granularity"
        )
        .auth
        .bearer(config.oanda.apiKey)
        .headers(headers)
        .response(asJson[Candles])
    backend.send(request).flatMap(r => ZIO.fromEither(r.body))

object OandaClient:
  val live: ZLayer[AppConfig & SttpBackend[Task, Any], Nothing, Client] =
    ZLayer.fromFunction(new OandaClient(_, _))
