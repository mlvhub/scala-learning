package cli

import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.schema.Schema.boolean
import smithy4s.schema.Schema.string
import smithy4s.schema.Schema.struct

final case class GetNumberFactInput(number: String, _type: Type, json: Boolean = true)

object GetNumberFactInput extends ShapeTag.Companion[GetNumberFactInput] {
  val id: ShapeId = ShapeId("cli", "GetNumberFactInput")

  val hints: Hints = Hints(
    smithy.api.Input(),
  ).lazily

  // constructor using the original order from the spec
  private def make(number: String, _type: Type, json: Boolean): GetNumberFactInput = GetNumberFactInput(number, _type, json)

  implicit val schema: Schema[GetNumberFactInput] = struct(
    string.validated(smithy.api.Pattern(s"^(([0-9]*)|(random))$$")).required[GetNumberFactInput]("number", _.number).addHints(smithy.api.HttpLabel()),
    Type.schema.required[GetNumberFactInput]("type", _._type).addHints(smithy.api.HttpLabel()),
    boolean.required[GetNumberFactInput]("json", _.json).addHints(smithy.api.Default(smithy4s.Document.fromBoolean(true)), smithy.api.HttpQuery("json")),
  )(make).withId(id).addHints(hints)
}
