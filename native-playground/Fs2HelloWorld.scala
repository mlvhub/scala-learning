//> using scala 3.7.3

//> using dep org.typelevel::cats-effect::3.7.0-RC1
//> using dep co.fs2::fs2-core::3.13.0-M7
//> using dep co.fs2::fs2-io::3.13.0-M7

import cats.effect.*
import fs2.*
import fs2.io.*

object Fs2HelloWorld extends IOApp.Simple:
    val run: IO[Unit] = Stream.eval(IO.println("Hello, World!")).compile.drain
