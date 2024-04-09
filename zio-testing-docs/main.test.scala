//> using scala "3"
//> using dep "dev.zio::zio::2.0.21"
//> using dep "dev.zio::zio-test::2.0.21"
//> using dep "dev.zio::zio-test-sbt::2.0.21"

import zio._
import zio.test._
import zio.test.Assertion._

object HelloWorldSpec extends ZIOSpecDefault {
  def spec = suite("HelloWorldSpec")(
    test("sayHello correctly displays output") {
      for {
        _ <- HelloWorld.sayHello
        output <- TestConsole.output
      } yield assertTrue(output == Vector("Hello, World!\n"))
    }
  )
}
