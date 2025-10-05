package cli

import smithy4s.Endpoint
import smithy4s.Hints
import smithy4s.Schema
import smithy4s.Service
import smithy4s.ShapeId
import smithy4s.Transformation
import smithy4s.kinds.PolyFunction5
import smithy4s.kinds.toPolyFunction5.const5
import smithy4s.schema.OperationSchema

trait NumbersGen[F[_, _, _, _, _]] {
  self =>

  /** HTTP GET /{number}/{type} */
  def getNumberFact(number: String, _type: Type, json: Boolean = true): F[GetNumberFactInput, Nothing, GetNumberFactOutput, Nothing, Nothing]

  final def transform: Transformation.PartiallyApplied[NumbersGen[F]] = Transformation.of[NumbersGen[F]](this)
}

object NumbersGen extends Service.Mixin[NumbersGen, NumbersOperation] {

  val id: ShapeId = ShapeId("cli", "Numbers")
  val version: String = ""

  val hints: Hints = Hints(
    alloy.SimpleRestJson(),
  ).lazily

  def apply[F[_]](implicit F: Impl[F]): F.type = F

  object ErrorAware {
    def apply[F[_, _]](implicit F: ErrorAware[F]): F.type = F
    type Default[F[+_, +_]] = Constant[smithy4s.kinds.stubs.Kind2[F]#toKind5]
  }

  val endpoints: Vector[smithy4s.Endpoint[NumbersOperation, _, _, _, _, _]] = Vector(
    NumbersOperation.GetNumberFact,
  )

  def input[I, E, O, SI, SO](op: NumbersOperation[I, E, O, SI, SO]): I = op.input
  def ordinal[I, E, O, SI, SO](op: NumbersOperation[I, E, O, SI, SO]): Int = op.ordinal
  override def endpoint[I, E, O, SI, SO](op: NumbersOperation[I, E, O, SI, SO]) = op.endpoint
  class Constant[P[-_, +_, +_, +_, +_]](value: P[Any, Nothing, Nothing, Nothing, Nothing]) extends NumbersOperation.Transformed[NumbersOperation, P](reified, const5(value))
  type Default[F[+_]] = Constant[smithy4s.kinds.stubs.Kind1[F]#toKind5]
  def reified: NumbersGen[NumbersOperation] = NumbersOperation.reified
  def mapK5[P[_, _, _, _, _], P1[_, _, _, _, _]](alg: NumbersGen[P], f: PolyFunction5[P, P1]): NumbersGen[P1] = new NumbersOperation.Transformed(alg, f)
  def fromPolyFunction[P[_, _, _, _, _]](f: PolyFunction5[NumbersOperation, P]): NumbersGen[P] = new NumbersOperation.Transformed(reified, f)
  def toPolyFunction[P[_, _, _, _, _]](impl: NumbersGen[P]): PolyFunction5[NumbersOperation, P] = NumbersOperation.toPolyFunction(impl)

}

sealed trait NumbersOperation[Input, Err, Output, StreamedInput, StreamedOutput] {
  def run[F[_, _, _, _, _]](impl: NumbersGen[F]): F[Input, Err, Output, StreamedInput, StreamedOutput]
  def ordinal: Int
  def input: Input
  def endpoint: Endpoint[NumbersOperation, Input, Err, Output, StreamedInput, StreamedOutput]
}

object NumbersOperation {

  object reified extends NumbersGen[NumbersOperation] {
    def getNumberFact(number: String, _type: Type, json: Boolean = true): GetNumberFact = GetNumberFact(GetNumberFactInput(number, _type, json))
  }
  class Transformed[P[_, _, _, _, _], P1[_ ,_ ,_ ,_ ,_]](alg: NumbersGen[P], f: PolyFunction5[P, P1]) extends NumbersGen[P1] {
    def getNumberFact(number: String, _type: Type, json: Boolean = true): P1[GetNumberFactInput, Nothing, GetNumberFactOutput, Nothing, Nothing] = f[GetNumberFactInput, Nothing, GetNumberFactOutput, Nothing, Nothing](alg.getNumberFact(number, _type, json))
  }

  def toPolyFunction[P[_, _, _, _, _]](impl: NumbersGen[P]): PolyFunction5[NumbersOperation, P] = new PolyFunction5[NumbersOperation, P] {
    def apply[I, E, O, SI, SO](op: NumbersOperation[I, E, O, SI, SO]): P[I, E, O, SI, SO] = op.run(impl) 
  }
  final case class GetNumberFact(input: GetNumberFactInput) extends NumbersOperation[GetNumberFactInput, Nothing, GetNumberFactOutput, Nothing, Nothing] {
    def run[F[_, _, _, _, _]](impl: NumbersGen[F]): F[GetNumberFactInput, Nothing, GetNumberFactOutput, Nothing, Nothing] = impl.getNumberFact(input.number, input._type, input.json)
    def ordinal: Int = 0
    def endpoint: smithy4s.Endpoint[NumbersOperation,GetNumberFactInput, Nothing, GetNumberFactOutput, Nothing, Nothing] = GetNumberFact
  }
  object GetNumberFact extends smithy4s.Endpoint[NumbersOperation,GetNumberFactInput, Nothing, GetNumberFactOutput, Nothing, Nothing] {
    val schema: OperationSchema[GetNumberFactInput, Nothing, GetNumberFactOutput, Nothing, Nothing] = Schema.operation(ShapeId("cli", "GetNumberFact"))
      .withInput(GetNumberFactInput.schema)
      .withOutput(GetNumberFactOutput.schema)
      .withHints(smithy.api.Http(method = smithy.api.NonEmptyString("GET"), uri = smithy.api.NonEmptyString("/{number}/{type}"), code = 200), smithy.api.Readonly())
    def wrap(input: GetNumberFactInput): GetNumberFact = GetNumberFact(input)
  }
}

