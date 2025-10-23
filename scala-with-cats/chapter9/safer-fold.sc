//> using dep org.typelevel::cats-core:2.13.0

import cats.*
import cats.syntax.all.*

def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B =
  def foldRightEval[A, B](as: List[A], acc: Eval[B])(fn: (A, Eval[B]) => Eval[B]): Eval[B] =
    as match
      case head :: tail =>
        Eval.defer((fn(head, foldRightEval(tail, acc)(fn))))
      case Nil =>
        acc
  foldRightEval(as, Eval.now(acc)) { (a, evalB) =>
    evalB.map(fn(a, _))
  }.value

val fn = (x: Int, y: Int) => x + y

val items = List.fill(10)(1)

println(foldRight(items, 0)(fn))
