trait Monad[F[_]]:
  def pure[A](a: A): F[A]
  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]
  def map[A, B](value: F[A])(func: A => B): F[B] =
    flatMap(value)(func.andThen(pure))

// Test with Option monad
given optionMonad: Monad[Option] with
  def pure[A](a: A): Option[A] = Some(a)
  def flatMap[A, B](value: Option[A])(func: A => Option[B]): Option[B] =
    value.flatMap(func)

// Test with List monad
given listMonad: Monad[List] with
  def pure[A](a: A): List[A] = List(a)
  def flatMap[A, B](value: List[A])(func: A => List[B]): List[B] =
    value.flatMap(func)

// Helper function to use the monad
def testMap: Unit =
  println("Testing map implementation:")
  
  // Test with Option
  println("\n=== Option Monad ===")
  val opt1 = optionMonad.map(Some(5))(_ * 2)
  println(s"map(Some(5))(_ * 2) = $opt1") // Should be Some(10)
  
  val opt2 = optionMonad.map(None)((x: Int) => x * 2)
  println(s"map(None)(_ * 2) = $opt2") // Should be None
  
  val opt3 = optionMonad.map(Some("hello"))(_.toUpperCase)
  println(s"map(Some(\"hello\"))(_.toUpperCase) = $opt3") // Should be Some(HELLO)
  
  // Test composition: map(map(x)(f))(g) should equal map(x)(f andThen g)
  val opt4 = optionMonad.map(optionMonad.map(Some(3))(_ + 1))(_ * 2)
  val opt5 = optionMonad.map(Some(3))(x => (x + 1) * 2)
  println(s"Composition test: $opt4 == $opt5") // Both should be Some(8)
  
  // Test with List
  println("\n=== List Monad ===")
  val list1 = listMonad.map(List(1, 2, 3))(_ * 2)
  println(s"map(List(1,2,3))(_ * 2) = $list1") // Should be List(2, 4, 6)
  
  val list2 = listMonad.map(List.empty[Int])(_ * 2)
  println(s"map(List.empty)(_ * 2) = $list2") // Should be List()
  
  val list3 = listMonad.map(List("a", "b", "c"))(_.toUpperCase)
  println(s"map(List(\"a\",\"b\",\"c\"))(_.toUpperCase) = $list3") // Should be List(A, B, C)
  
  // Test composition with List
  val list4 = listMonad.map(listMonad.map(List(1, 2))(_ + 1))(_ * 2)
  val list5 = listMonad.map(List(1, 2))(x => (x + 1) * 2)
  println(s"Composition test: $list4 == $list5") // Both should be List(4, 6)
  
  // Test identity law: map(x)(identity) should equal x
  println("\n=== Identity Law ===")
  val opt6 = optionMonad.map(Some(42))(identity)
  println(s"map(Some(42))(identity) = $opt6") // Should be Some(42)
  
  val list6 = listMonad.map(List(1, 2, 3))(identity)
  println(s"map(List(1,2,3))(identity) = $list6") // Should be List(1, 2, 3)

testMap
