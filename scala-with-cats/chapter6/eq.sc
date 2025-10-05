//> using dep org.typelevel::cats-core:2.13.0

import cats.*
import cats.syntax.all.*

final case class Cat(name: String, age: Int, color: String)

given Eq[Cat] = Eq.instance[Cat] { (c1, c2) =>
  c1.name === c2.name && 
  c1.age === c2.age &&
  c1.color === c2.color
}

val milo = Cat("Milo", 4, "grey")
val jerry = Cat("Jerry", 4, "three-coloured")

assert(milo =!= jerry)

val optionCat1 = Option(milo)
val optionCat2 = Option.empty[Cat]

assert(optionCat1 =!= optionCat2)
