package object cli {
  type Numbers[F[_]] = smithy4s.kinds.FunctorAlgebra[NumbersGen, F]
  val Numbers = NumbersGen


}