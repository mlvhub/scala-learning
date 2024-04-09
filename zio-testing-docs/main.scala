//> using scala "3"
//> using dep "dev.zio::zio::2.0.21"

import zio._
import java.io.IOException

object HelloWorld:
  def sayHello: ZIO[Any, IOException, Unit] =
    Console.printLine("Hello, World!")

object MainApp extends ZIOAppDefault:
  def run = HelloWorld.sayHello
