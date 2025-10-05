//> using scala 3.7.1

// NOTE: repository taken from https://central.sonatype.org/publish/publish-portal-snapshots/#consuming-via-maven
//> using repository https://central.sonatype.com/repository/maven-snapshots

//> using dep org.http4s::http4s-ember-server::0.23.30-161-f5b9629-SNAPSHOT
//> using dep org.http4s::http4s-dsl::0.23.30-161-f5b9629-SNAPSHOT

//> using dep org.typelevel::cats-effect::3.7.0-RC1

import cats.effect.*
import org.http4s.*
import com.comcast.ip4s.*
import org.http4s.dsl.io.*
import org.http4s.ember.server.*
import org.http4s.implicits.*

object Http4sHelloWorld extends IOApp.Simple:


    val helloWorldService: HttpRoutes[IO] = HttpRoutes.of[IO] {
        case GET -> Root / "hello" / name =>
        Ok(s"Hello, $name!")
        case GET -> Root / "hello" =>
        Ok("Hello, World!")
    }

    val run: IO[Unit] =
        EmberServerBuilder.default[IO]
            .withHost(ipv4"0.0.0.0")
            .withPort(port"8080")
            .withHttpApp(helloWorldService.orNotFound)
            .build
            .use(_ => IO.println("Server started on port 8080. Press Ctrl-C to stop.") *> IO.never)
