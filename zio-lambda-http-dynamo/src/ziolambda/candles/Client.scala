package ziolambda.candles

import zio.*

import ziolambda.config.*
import zio.json.*

final case class CandleMid(
    o: String,
    h: String,
    l: String,
    c: String
)

final case class Candle(
    time: String,
    mid: CandleMid,
    complete: Boolean
)

final case class Candles(
    instrument: String,
    granularity: String,
    candles: List[Candle]
)

object Candles:
  given JsonDecoder[CandleMid] = DeriveJsonDecoder.gen[CandleMid]
  given JsonDecoder[Candles] = DeriveJsonDecoder.gen[Candles]
  given JsonDecoder[Candle] = DeriveJsonDecoder.gen[Candle]

trait Client:
  def candles(
      instrumentId: String,
      count: Int,
      granularity: String = "D"
  ): ZIO[AppConfig, Throwable, Any]
