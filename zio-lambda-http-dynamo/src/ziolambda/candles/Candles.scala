package ziolambda.candles

import zio.Task
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
    @jsonField("instrument") instrumentId: String,
    granularity: String,
    candles: List[Candle]
)

object Candles:
  given JsonDecoder[CandleMid] = DeriveJsonDecoder.gen[CandleMid]
  given JsonDecoder[Candles] = DeriveJsonDecoder.gen[Candles]
  given JsonDecoder[Candle] = DeriveJsonDecoder.gen[Candle]

  given JsonEncoder[CandleMid] = DeriveJsonEncoder.gen[CandleMid]
  given JsonEncoder[Candles] = DeriveJsonEncoder.gen[Candles]
  given JsonEncoder[Candle] = DeriveJsonEncoder.gen[Candle]
