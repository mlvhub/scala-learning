// NOTE: don't forget to run `just clone`, this needs locally published libs
//> using scala 3.7.1

// NOTE: repository taken from https://central.sonatype.org/publish/publish-portal-snapshots/#consuming-via-maven
//> using repository https://central.sonatype.com/repository/maven-snapshots

//> using dep org.tpolecat::skunk-core::1.0-7f46fa8-SNAPSHOT

import cats.effect.*
import skunk.*
import skunk.implicits.*
import skunk.codec.all.*
// import natchez.Trace.Implicits.noop                          // (1)
import org.typelevel.otel4s.trace.Tracer.Implicits.noop

object SkunkHello extends IOApp {

  val session: Resource[IO, Session[IO]] =
    Session.single(                                          // (2)
      host     = "localhost",
      port     = 5432,
      user     = "jimmy",
      database = "world",
      password = Some("banana")
    )

  def run(args: List[String]): IO[ExitCode] =
    session.use { s =>                                       // (3)
      for {
        d <- s.unique(sql"select current_date".query(date))  // (4)
        _ <- IO.println(s"The current date is $d.")
      } yield ExitCode.Success
    }

}
