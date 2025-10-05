// enum Bool:
//   case True
//   case False

//   def fold[A](t: A)(f: A): A =
//     this match
//       case True => t
//       case False => f

trait Bool:
  def `if`[A](t: A)(f: A): A

val True = new Bool {
  def `if`[A](t: A)(f: A): A = t
}

val False = new Bool {
  def `if`[A](t: A)(f: A): A = f
}

def and(l: Bool, r: Bool): Bool =
  new Bool {
    def `if`[A](t: A)(f: A): A =
      l.`if`(r)(False).`if`(t)(f)
  }

println("and")
println(and(True, True).`if`("yes")("no"))
println(and(True, False).`if`("yes")("no"))
println(and(False, True).`if`("yes")("no"))
println(and(False, False).`if`("yes")("no"))
println()

def or(l: Bool, r: Bool): Bool =
  new Bool {
    def `if`[A](t: A)(f: A): A =
      l.`if`(True)(r).`if`(t)(f)
  }

println("or")
println(or(True, True).`if`("yes")("no"))
println(or(True, False).`if`("yes")("no"))
println(or(False, True).`if`("yes")("no"))
println(or(False, False).`if`("yes")("no"))
println()

def not(b: Bool): Bool =
  new Bool {
    def `if`[A](t: A)(f: A): A =
      b.`if`(False)(True).`if`(t)(f)
  }

println("not")
println(not(True).`if`("yes")("no"))
println(not(False).`if`("yes")("no"))
println()
