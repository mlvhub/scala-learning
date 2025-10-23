//> using scala 3.7.3

//> using dep org.typelevel::cats-core:2.13.0

import cats.*
import cats.syntax.all.*

val list1 = List(1, 2, 3)

val list2 = Functor[List].map(list1)(_ * 2)
println(list2)

val option1 = Option(123)

val option2 = Functor[Option].map(option1)(_.toString)
println(option2)

val func = (x: Int) => x + 1

val liftedFunc = Functor[Option].lift(func)

println(liftedFunc(Option(1)))

println(Functor[List].as(list1, "As"))


val func1 = (a: Int) => a + 1
val func2 = (a: Int) => a * 2
val func3 = (a: Int) => s"${a}!"
val func4 = func1.map(func2).map(func3)
println(func4(123))

def doMath[F[_]](start: F[Int])
  (using functor: Functor[F]): F[Int] =
    start.map(n => n + 1 * 2)

println(doMath(Option(20)))
println(doMath(List(1, 2, 3)) )
