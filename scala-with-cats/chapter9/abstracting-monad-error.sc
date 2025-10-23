//> using dep org.typelevel::cats-core:2.13.0

import scala.util.Try

import cats.*
import cats.syntax.all.*

def validateAdult[F[_]](age: Int)(using me: MonadError[F, Throwable]): F[Int] =
  age match
    case age if age >= 18 =>
      18.pure
    case _ =>
      me.raiseError(new Exception("not an adult"))
  
println(validateAdult[Try](18))
println(validateAdult[Try](8))

type ExceptionOr[A] = Either[Throwable, A]
println(validateAdult[ExceptionOr](-1))
