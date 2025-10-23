//> using scala "3"
//> using lib "dev.zio::zio::2.0.21"
//> using lib "dev.zio::zio-http::3.0.0-RC4"
//> using lib "dev.zio::zio-json::0.6.2"
package foo

/** Helpful ZIO HTTP URLs:
  * ----------------------
  * \- https://zio.dev/zio-http/
  * \- https://zio.github.io/zio-http/docs/v1.x/getting-started/
  * \- https://zio.dev/zio-http/getting-started/
  */

// SCALA-CLI NOTES:
// ----------------
// Set up your IDE:
//     scala-cli setup-ide .
// Run me like this:
//     scala-cli ThisFilename.scala

import zio.*
import zio.ZIOAppDefault
import zio.http.*

object ZioHttpServer extends ZIOAppDefault:

  // ZIO HTTP 101:
  // val app = Handler.text("Hello, world").toHttpApp

  val app =
    Routes(
      Method.GET / "todo" -> {
        Handler.text(s"1. wake up\n2. make coffee")
      },
      Method.GET / "blog" / "1" ->
        handler(Response.text("1st blog post"))
    ).toHttpApp

  def run = Server
    .serve(app)
    .provide(Server.defaultWithPort(8888))
  // OR: provide(Server.default)   // port 8080
