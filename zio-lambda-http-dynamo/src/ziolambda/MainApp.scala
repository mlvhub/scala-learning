package ziolambda

import zio._
import zio.lambda._
import zio.lambda.event._
import sttp.client3.httpclient.zio.*

import ziolambda.config.Configuration
import ziolambda.config.AppConfig

object MainApp extends ZIOAppDefault:
  def run: ZIO[Environment & ZIOAppArgs & Scope, Throwable, Any] =
    VolatilitySystem
      .run()
      .map(_.message)
      .provide(
        Configuration.live,
        candles.OandaClient.live,
        HttpClientZioBackend.layer()
      )
