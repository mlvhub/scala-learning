package ziolambda.http

import zio.{ZLayer, ZIO}
import zio.aws.core.httpclient.{HttpClient, Protocol}
import software.amazon.awssdk.http.{Protocol => AwsProtocol}
import software.amazon.awssdk.http.async.SdkAsyncHttpClient
import software.amazon.awssdk.http.nio.netty.{NettyNioAsyncHttpClient}
import java.time.Duration
import zio.aws.netty.NettyHttpClient

object CustomNettyClient {

  val customiser = (builder: NettyNioAsyncHttpClient.Builder) =>
    builder
      .maxConcurrency(500)
      .maxPendingConnectionAcquires(10000)
      .connectionMaxIdleTime(Duration.ofSeconds(600))
      .connectionTimeout(Duration.ofSeconds(20))
      .connectionAcquisitionTimeout(Duration.ofSeconds(60))
      .readTimeout(Duration.ofSeconds(120))

  val live: ZLayer[Any, Throwable, HttpClient] =
    NettyHttpClient.customized(Protocol.Http11, customiser)
}
