type Continuation = Double => Double

enum Expression:
  case Literal(number: Double)
  case Addition(e1: Expression, e2: Expression)
  case Substraction(e1: Expression, e2: Expression)
  case Multiplication(e1: Expression, e2: Expression)
  case Division(e1: Expression, e2: Expression)

  def `+`(other: Expression) =
    Addition(this, other)

  def `-`(other: Expression) =
    Substraction(this, other)

  def `*`(other: Expression) =
    Multiplication(this, other)

  def `/`(other: Expression) =
    Division(this, other)

  def eval: Double =
    def loop(e: Expression, cont: Continuation): Double =
      e match
        case Literal(number) => cont(number)
        case Addition(e1, e2) => loop(e1, e11 => loop(e2, e22 => cont(e11 + e22)))
        case Substraction(e1, e2) => loop(e1, e11 => loop(e2, e22 => cont(e11 - e22)))
        case Multiplication(e1, e2) => loop(e1, e11 => loop(e2, e22 => cont(e11 * e22)))
        case Division(e1, e2) => loop(e1, e11 => loop(e2, e22 => cont(e11 / e22)))
    loop(this, identity)
    
object Expression:
  def apply(number: Double): Expression = 
    Literal(number)

import Expression.*

val two = Expression(2)
val three = Expression(3)

assert(Addition(two, three).eval == 5)

assert(Substraction(two, three).eval == -1)
assert(Substraction(three, two).eval == 1)

assert(Multiplication(two, three).eval == 6)

assert(Division(Expression(6), three).eval == 2)
