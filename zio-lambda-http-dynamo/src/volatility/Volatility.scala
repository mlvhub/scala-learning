package ziolambda.volatility

import zio.json.*
import zio.schema.{DeriveSchema, Schema}
import zio.dynamodb.ProjectionExpression

final case class Volatility(instrumentId: String, time: Long, value: Double)

object Volatility:
  given JsonDecoder[Volatility] = DeriveJsonDecoder.gen[Volatility]
  given JsonEncoder[Volatility] = DeriveJsonEncoder.gen[Volatility]

  given Schema.CaseClass3[String, Long, Double, Volatility] =
    DeriveSchema.gen[Volatility]

  val (instrumentId, time, value) = ProjectionExpression.accessors[Volatility]
