trait Display[A]:
  def display(a: A): String

object Display:
  given Display[String] = (a: String) => a

  given Display[Int] with
    def display(a: Int): String = s"$a"

  def display[A](a: A)(using d: Display[A]): String = d.display(a)

  def print[A](a: A)(using Display[A]): Unit = println(display(a))

object DisplaySyntax:
  extension [A](a: A)(using d: Display[A])
    def display: String = d.display(a)
    def print: Unit = println(a.display)

Display.print(1)

final case class Cat(name: String, age: Int, color: String)

given Display[Cat] = (cat: Cat) => {
  val n = Display.display(cat.name)
  val a = Display.display(cat.age)
  val c = Display.display(cat.color)
  s"Cat(name=$n, age=$a, colour=$c)"
}

val cat = Cat("Jerry", 4, "three-coloured")

Display.print(cat)

import DisplaySyntax.* 

cat.print
