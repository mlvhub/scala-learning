trait Semigroup[A]:
  def combine(x: A, y: A): A

trait Monoid[A] extends Semigroup[A]:
  def empty: A

object Monoid:
  def apply[A](using monoid: Monoid[A]) = monoid

given concatMonoid[A]: Monoid[Set[A]] with
  def combine(x: Set[A], y: Set[A]): Set[A] =
    x ++ y

  def empty: Set[A] = Set.empty[A]

