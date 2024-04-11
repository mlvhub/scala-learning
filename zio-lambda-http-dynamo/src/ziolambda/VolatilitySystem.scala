package ziolambda

import zio.*

import ziolambda.config.AppConfig
import ziolambda.candles

object VolatilitySystem {
  final case class Result(
      message: String
  )

  def run(): ZIO[AppConfig & candles.Client, Throwable, Result] = for {
    appConfig <- ZIO.service[AppConfig]
    oandaClient <- ZIO.service[candles.Client]
    candles <- oandaClient.candles(appConfig.oanda.instrumentId, 10)
    _ <- Console.printLine(candles)
  } yield Result(message = s"Volatility is: TODO")
}
