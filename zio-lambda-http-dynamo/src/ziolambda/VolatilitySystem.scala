package ziolambda

import zio.*
import zio.json.*

import java.time.Instant

import ziolambda.config.AppConfig
import ziolambda.candles
import ziolambda.candles.Candles
import ziolambda.repo.VolatilityRepo
import ziolambda.volatility.Volatility
import zio.logging.LogAnnotation

object VolatilitySystem {
  final case class Result(
      message: String
  )

  val instrumentIdLogAnnotation =
    LogAnnotation[String]("instrument_id", _ + _, _.toString)

  type Environment = AppConfig & candles.Client & VolatilityRepo

  def run(): ZIO[Environment, Throwable, Result] = for {
    appConfig <- ZIO.service[AppConfig]
    oandaClient <- ZIO.service[candles.Client]
    volatilityRepo <- ZIO.service[VolatilityRepo]
    candles <- oandaClient.candles(appConfig.oanda.instrumentId, 10)
    _ <- ZIO.logInfo(
      s"Got ${candles.candles.length} candles for ${appConfig.oanda.instrumentId}"
    )
    _ <- volatilityRepo.save(
      Volatility(
        appConfig.oanda.instrumentId,
        Instant.now().toEpochMilli(),
        0.0
      )
    )
    // TODO: add sort key
    volatility <- volatilityRepo.getVolatility(appConfig.oanda.instrumentId, 0)
    _ <- volatility match {
      case Some(v) =>
        ZIO.logInfo(
          s"The volatility for instrument ${v.instrumentId} is ${v.volatility}"
        )
      case None =>
        ZIO.logInfo(
          s"No volatility found for instrument ${appConfig.oanda.instrumentId}"
        )
    }
  } yield Result(message = volatility.toJson)
}
