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
    this match
      case Literal(number) => number
      case Addition(e1, e2) => e1.eval + e2.eval
      case Substraction(e1, e2) => e1.eval - e2.eval
      case Multiplication(e1, e2) => e1.eval * e2.eval
      case Division(e1, e2) => e1.eval / e2.eval
    
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
