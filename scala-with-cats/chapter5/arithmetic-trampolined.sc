type Continuation = Double => Call

enum Call:
  case Loop(e: Expression, cont: Continuation)
  case Continue(value: Double, cont: Continuation)
  case Done(resukt: Double)

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
    def loop(e: Expression, cont: Continuation): Call =
      e match
        case Literal(number) => Call.Continue(number, cont)
        case Addition(e1, e2) => Call.Loop(e1, e11 => Call.Loop(e2, e22 => Call.Continue(e11 + e22, cont)))
        case Substraction(e1, e2) => Call.Loop(e1, e11 => Call.Loop(e2, e22 => Call.Continue(e11 - e22, cont)))
        case Multiplication(e1, e2) => Call.Loop(e1, e11 => Call.Loop(e2, e22 => Call.Continue(e11 * e22, cont)))
        case Division(e1, e2) => Call.Loop(e1, e11 => Call.Loop(e2, e22 => Call.Continue(e11 / e22, cont)))

    def trampoline(call: Call): Double =
      call match
        case Call.Continue(value, k) => trampoline(k(value))
        case Call.Loop(expr, k) => trampoline(loop(expr, k))
        case Call.Done(result) => result

    trampoline(loop(this, x => Call.Done(x)))
    
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
