package ziolambda

import java.io.IOException

import zio._
import zio.lambda._
import zio.lambda.event._
import sttp.client3.httpclient.zio.*

import ziolambda.config.Configuration
import ziolambda.config.AppConfig

object LambdaHandler extends ZIOAppDefault {

  def app(
      event: ScheduledEvent,
      context: Context
  ): ZIO[AppConfig & candles.Client, Throwable, String] =
    VolatilitySystem.run().map(_.message)

  override val run =
    ZLambdaRunner
      .serve(app)
      .provide(
        Configuration.live,
        candles.OandaClient.live,
        HttpClientZioBackend.layer()
      )
}
