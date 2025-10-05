//> using scala 3.7

//> using dep org.typelevel::cats-effect::3.7.0-RC1

import cats.effect.*

object HelloWorld extends IOApp.Simple:
    val run: IO[Unit] = IO.println("Hello, World!")
