//> using dep org.typelevel::cats-core:2.13.0

import cats.*
import cats.syntax.all.*

object SuperAdder:
  def add[A: Monoid](items: List[A]): A =
    items.foldLeft(Monoid[A].empty)(_ |+| _)

val items = List(1,2,3)
println(SuperAdder.add(items))

val itemsOpt: List[Option[Int]] = List(1.some, None,2.some)
println(SuperAdder.add(itemsOpt))

case class Order(totalCost: Double, quantity: Double)

given Monoid[Order] with
  def combine(x: Order, y: Order): Order = 
    Order(
      totalCost = x.totalCost + y.totalCost,
      quantity = x.quantity + y.quantity,
    )
    
  def empty: Order = Order(totalCost=0, quantity=0)

val o1 = Order(5000, 1)
val o2 = Order(13000, 4)

println(SuperAdder.add(List(o1, o2)))
