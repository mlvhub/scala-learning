package ziolambda

import zio.*
import zio.json.*

import java.time.Instant

import ziolambda.config.AppConfig
import ziolambda.candles
import ziolambda.repo.VolatilityRepo
import ziolambda.volatility.Volatility

object VolatilitySystem {
  final case class Result(
      message: String
  )

  type Environment = AppConfig & candles.Client & VolatilityRepo

  def run(): ZIO[Environment, Throwable, Result] = for {
    appConfig <- ZIO.service[AppConfig]
    oandaClient <- ZIO.service[candles.Client]
    volatilityRepo <- ZIO.service[VolatilityRepo]
    candles <- oandaClient.candles(appConfig.oanda.instrumentId, 10)
    _ <- volatilityRepo.save(
      Volatility(
        appConfig.oanda.instrumentId,
        Instant.now().toEpochMilli(),
        0.0
      )
    )
    volatility <- volatilityRepo.getVolatility(appConfig.oanda.instrumentId, 0)
    _ <- Console.printLine(candles)
    _ <- Console.printLine(volatility)
  } yield Result(message = volatility.toJson)
}
