//> using scala 3.7.3

//> using dep org.typelevel::cats-core:2.13.0

import cats.*
import cats.syntax.all.*

sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

final case class Leaf[A](value: A) extends Tree[A]

object Tree:
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
    Branch(left, right)
  def leaf[A](value: A): Tree[A] =
    Leaf(value)

given Functor[Tree] with
  def map[A, B](value: Tree[A])(f: A => B): Tree[B] =
    value match
      case Branch(left, right) => Branch(map(left)(f), map(right)(f))
      case Leaf(value) => Leaf(f(value))

val leafL = Leaf(1)
val leafR = Leaf(2)
println(Functor[Tree].map(leafL)(_ * 2))

val branch = Branch(leafL, leafR)
println(Functor[Tree].map(branch)(_ * 2))

val branch2 = Tree.branch(leafL, leafR)
println(branch2.map(_ * 5))
