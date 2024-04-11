package ziolambda.candles

import zio.*

import ziolambda.config.*

trait Client:
  def candles(
      instrumentId: String,
      count: Int,
      granularity: String = "D"
  ): ZIO[AppConfig, Throwable, Candles]
