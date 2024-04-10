package ziolambdasimple

import zio.Console._
import zio._
import zio.lambda._
import zio.lambda.event._
import java.io.IOException

object LambdaHandler extends ZIOAppDefault {

  def run: ZIO[Environment & ZIOAppArgs & Scope, Throwable, Any] =
    for {
      _ <- printLine("Hello, world!")
      zioArgs <- ZIO.service[ZIOAppArgs]
      args = zioArgs.getArgs.mkString(" ")
      _ <- printLine(s"Received arguments: $args")
    } yield "Handler ran successfully"

}
