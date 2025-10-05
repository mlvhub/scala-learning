// Example taken from https://gist.github.com/kubukoz/97213a24ff292d12870ef10e0ba0133b

//> using scala 3.7

// NOTE: repository taken from https://central.sonatype.org/publish/publish-portal-snapshots/#consuming-via-maven
//> using repository https://central.sonatype.com/repository/maven-snapshots

//> using dep org.http4s::http4s-ember-server::0.23.30-161-f5b9629-SNAPSHOT
//> using dep org.http4s::http4s-dsl::0.23.30-161-f5b9629-SNAPSHOT
//> using dep com.lihaoyi::scalatags::0.13.1
//> using dep org.typelevel::cats-effect::3.7.0-RC1

//> using option -Wunused:all

import scalatags.Text.all.*
import cats.effect.*
import fs2.Stream
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.ember.server.*
import org.http4s.implicits.*
import org.http4s.headers.`Content-Type`

import scala.concurrent.duration.{span as _, *}

object SSEApp extends IOApp.Simple:

  val indexPage = html(
    meta(charset := "utf-8"),
    head(
      script(src := "https://unpkg.com/htmx.org@2.0.4"),
      script(src := "https://unpkg.com/htmx-ext-sse@2.2.3"),
    ),
    body(
      h1("http4s/fs2 ❤️ HTMX + SSE"),
      div(
        id := "random-box",
        p(
          "Your UUID: ",
          span(
            attr("hx-ext") := "sse",
            attr("sse-connect") := "/events",
            attr("sse-swap") := "message",
          ),
        ),
      ),
    ),
  )

  val routes = HttpRoutes.of[IO] {
    case GET -> Root => Ok(indexPage.render, `Content-Type`(MediaType.text.html))

    case GET -> Root / "events" =>
      Ok(Stream.fixedRateStartImmediately[IO](100.millis).evalMap { _ =>
        IO.randomUUID.map { uuid =>
          ServerSentEvent(data = Some(uuid.toString))
        }
      })
  }

  override def run: IO[Unit] =
    EmberServerBuilder
      .default[IO]
      .withHttpApp(routes.orNotFound)
      .build
      .evalTap { server =>
        IO.println(
          s"Server running at ${server.baseUri}"
        )
      }
      .useForever
