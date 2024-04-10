package ziolambdasimple

import zio.Console._
import zio._
import zio.lambda._
import zio.lambda.event._
import java.io.IOException

object LambdaHandler extends ZIOAppDefault {

  def app(
      event: ScheduledEvent,
      context: Context
  ): ZIO[Any, IOException, String] = for {
    _ <- printLine("Hello, world!")
  } yield "Handler ran successfully"

  override val run =
    ZLambdaRunner.serve(app)
}
