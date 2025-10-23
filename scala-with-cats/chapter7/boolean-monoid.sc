trait Semigroup[A]:
  def combine(x: A, y: A): A

trait Monoid[A] extends Semigroup[A]:
  def empty: A

object Monoid:
  def apply[A](using monoid: Monoid[A]) = monoid

given andMonoid: Monoid[Boolean] with
  def combine(x: Boolean, y: Boolean): Boolean = x && y
  def empty: Boolean = true

given orMonoid: Monoid[Boolean] with
  def combine(x: Boolean, y: Boolean): Boolean = x || y
  def empty: Boolean = false

given xorMonoid: Monoid[Boolean] with
  def combine(x: Boolean, y: Boolean): Boolean = (x && !y) || (!x && y)
  def empty: Boolean = false

given xnorMonoid: Monoid[Boolean] with
  def combine(x: Boolean, y: Boolean): Boolean = (x || !y) && (!x || y)
  def empty: Boolean = true
