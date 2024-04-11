package ziolambda.repo

import zio.Task

import ziolambda.volatility.Volatility

trait VolatilityRepo {
  def save(volatility: Volatility): Task[Unit]
  def getVolatility(instrumentId: String, time: Long): Task[Option[Volatility]]
}
