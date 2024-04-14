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

object VolatilitySystem:
  final case class Result(
      message: String
  )

  val instrumentIdLogAnnotation =
    LogAnnotation[String]("instrument_id", _ + _, identity)

  type Environment = AppConfig & candles.Client & VolatilityRepo

  def run(): ZIO[Environment, Throwable, Result] =
    ZIO
      .service[AppConfig]
      .flatMap(appConfig =>
        ZIO.logAnnotate("instrument_id", appConfig.oanda.instrumentId) {
          for {
            _ <- ZIO.logInfo(
              s"Running volatility system for instrument ${appConfig.oanda.instrumentId}"
            )
            oandaClient <- ZIO.service[candles.Client]
            volatilityRepo <- ZIO.service[VolatilityRepo]
            candles <- oandaClient.candles(appConfig.oanda.instrumentId, 10)
            _ <- ZIO.logInfo(
              s"Got ${candles.candles.length} candles for ${appConfig.oanda.instrumentId}"
            )
            // TODO: calculate volatility
            volatility = Volatility(
              appConfig.oanda.instrumentId,
              Instant.now().toEpochMilli(),
              0.0
            )
            _ <- volatilityRepo.save(volatility)
            // TODO: add sort key
            volatility <- volatilityRepo.getVolatility(
              appConfig.oanda.instrumentId,
              0
            )
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
            _ <- ZIO.logInfo(
              s"Finished volatility system for instrument ${appConfig.oanda.instrumentId}"
            )
          } yield Result(message = volatility.toJson)
        } @@ instrumentIdLogAnnotation(appConfig.oanda.instrumentId)
      )
