package ziolambda

import java.io.IOException

import zio._
import zio.lambda._
import zio.lambda.event._
import sttp.client3.httpclient.zio.*
import zio.aws.core.config.AwsConfig
import zio.aws.dynamodb.DynamoDb
import zio.aws.netty.NettyHttpClient
import zio.dynamodb.DynamoDBExecutor
import zio.logging.consoleJsonLogger

import ziolambda.config.Configuration
import ziolambda.config.AppConfig
import ziolambda.repo.DynamoVolatilityRepo
import ziolambda.http.CustomNettyClient

object LambdaHandler extends ZIOAppDefault {

  def app(
      event: ScheduledEvent,
      context: Context
  ): ZIO[VolatilitySystem.Environment, Throwable, String] =
    VolatilitySystem.run().map(_.message)

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.removeDefaultLoggers >>> consoleJsonLogger()

  override val run =
    ZLambdaRunner
      .serve(app)
      .provide(
        Configuration.live,
        candles.OandaClient.live,
        HttpClientZioBackend.layer(),
        CustomNettyClient.live,
        AwsConfig.default,
        DynamoDb.live,
        DynamoDBExecutor.live,
        DynamoVolatilityRepo.live
      )
}
