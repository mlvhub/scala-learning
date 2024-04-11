package ziolambda.repo

import zio.*
import zio.dynamodb.*
import zio.json.{JsonDecoder, JsonEncoder}
import zio.dynamodb.ProjectionExpression

import ziolambda.volatility.Volatility
import ziolambda.config.AppConfig

final class DynamoVolatilityRepo(
    config: AppConfig,
    dynamoDbExecutor: DynamoDBExecutor
) extends VolatilityRepo {
  override def save(volatility: Volatility): Task[Unit] =
    DynamoDBQuery
      .put(config.db.tableName, volatility)
      .execute
      .unit
      .provide(ZLayer.succeed(dynamoDbExecutor))

  override def getVolatility(
      instrumentId: String,
      time: Long
  ): Task[Option[Volatility]] =
    DynamoDBQuery
      .get(config.db.tableName)(
        Volatility.instrumentId.partitionKey === instrumentId
      )
      .execute
      .flatMap {
        case Left(DynamoDBError.ItemError.ValueNotFound(_)) =>
          ZIO.succeed(None)
        case Left(error) =>
          ZIO.fail(error)
        case Right(value) =>
          ZIO.succeed(Some(value))
      }
      .provide(ZLayer.succeed(dynamoDbExecutor))
}

object DynamoVolatilityRepo {
  val live
      : ZLayer[AppConfig & DynamoDBExecutor, Nothing, DynamoVolatilityRepo] =
    ZLayer.fromFunction(DynamoVolatilityRepo(_, _))
}
