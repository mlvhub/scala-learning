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
import zio.logging.{LogAnnotation, LogFormat, consoleJsonLogger}

import ziolambda.config.Configuration
import ziolambda.config.AppConfig
import ziolambda.repo.DynamoVolatilityRepo
import ziolambda.http.CustomNettyClient

object MainApp extends ZIOAppDefault:

  val instrumentIdLogAnnotation =
    LogAnnotation[String]("instrument_id", _ + _, _.toString)
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.removeDefaultLoggers >>> consoleJsonLogger()
  def run: ZIO[Environment & ZIOAppArgs & Scope, Throwable, Any] =
    VolatilitySystem
      .run()
      .map(_.message)
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
