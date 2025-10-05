trait Stream[A]:
    def head: A
    def tail: Stream[A]

    def take(n: Int): List[A] =
        if n == 0 then Nil else head :: tail.take(n - 1)

    def map[B](f: A => B): Stream[B] =
      val self = this
      new Stream[B] {
        def head: B = f(self.head)
        def tail: Stream[B] = self.tail.map(f)
      } 

    // bit different from book, could use testing
    def filter(pred: A => Boolean): Stream[A] =
      val self = this
      new Stream[A] {
        def head: A = if pred(self.head) then self.head else self.tail.filter(pred).head
        def tail: Stream[A] = if pred(self.head) then self.tail.filter(pred) else self.tail.tail.filter(pred)
      }

    def zip[B](that: Stream[B]): Stream[(A, B)] =
      val self = this
      new Stream[(A, B)] {
        def head: (A, B) = (self.head, that.head)
        def tail: Stream[(A, B)] = self.tail.zip(that.tail)
      }

    def scanLeft[B](zero: B)(f: (B, A) => B): Stream[B] =
      val self = this
      new Stream[B] {
        def head: B = zero
        def tail: Stream[B] = self.tail.scanLeft(f(zero, self.head))(f)
      } 

object Stream:
  def unfold[A, B](seed: A, f: A => B, next: A => A): Stream[B] =
    new Stream[B] {
      def head: B =
        f(seed)
      def tail: Stream[B] =
        unfold(next(seed), f, next)
  }

val ones: Stream[Int] =
    new Stream[Int] {
        def head: Int = 1
        def tail: Stream[Int] = ones
    }

println(ones.head)
println(ones.tail.head)

println(ones.take(5))

println(ones.map(_ * 2).take(5))

val alternating = Stream.unfold(
  true,
  x => if x then 1 else -1,
  x => !x
)
println(alternating.take(5))

println(alternating.filter(_ == 1). take(10))

println(ones.zip(ones.map(_ * 2)).take(6))

val naturals = ones.scanLeft(0)((b, a) => b + a)
println(naturals.take(5))
