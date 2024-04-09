import zio.*
import zio.http.*

object HttpClient extends ZIOAppDefault:
  val url = "http://localhost:8080/recipes"

  private val program = for {
    postRes <- Client.request(
      url,
      Method.POST,
      content = Body.fromString("""{
          |"id": 1,
          |"name": "burger",
          |"ingredients": ["beef", "salt", "pepper"]
          |}""".stripMargin)
    )
    data <- postRes.body.asString
    _ <- Console.printLine(s"posted: $data")
    getRes <- Client.request(
      s"$url/1",
      Method.GET
    )
    gotData <- getRes.body.asString
    _ <- Console.printLine(s"gotData: $gotData")

  } yield ()

  def run: ZIO[Environment & ZIOAppArgs & Scope, Any, Any] =
    program.provide(Client.default)
