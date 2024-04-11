package ziolambda

import zio._
import zio.lambda._
import zio.lambda.event._
import sttp.client3.httpclient.zio.*
import zio.aws.core.config.AwsConfig
import zio.dynamodb.DynamoDBExecutor
import zio.aws.dynamodb.DynamoDb
import zio.aws.netty.NettyHttpClient
import zio.dynamodb.DynamoDBExecutor

import ziolambda.config.Configuration
import ziolambda.config.AppConfig
import ziolambda.repo.DynamoVolatilityRepo

object MainApp extends ZIOAppDefault:
  def run: ZIO[Environment & ZIOAppArgs & Scope, Throwable, Any] =
    VolatilitySystem
      .run()
      .map(_.message)
      .provide(
        Configuration.live,
        candles.OandaClient.live,
        HttpClientZioBackend.layer(),
        NettyHttpClient.default,
        AwsConfig.default,
        DynamoDb.live,
        DynamoDBExecutor.live,
        DynamoVolatilityRepo.live
      )
