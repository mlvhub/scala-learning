package cli

import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.schema.Schema.boolean
import smithy4s.schema.Schema.string
import smithy4s.schema.Schema.struct

final case class GetNumberFactOutput(text: String, found: Boolean)

object GetNumberFactOutput extends ShapeTag.Companion[GetNumberFactOutput] {
  val id: ShapeId = ShapeId("cli", "GetNumberFactOutput")

  val hints: Hints = Hints(
    smithy.api.Output(),
  ).lazily

  // constructor using the original order from the spec
  private def make(text: String, found: Boolean): GetNumberFactOutput = GetNumberFactOutput(text, found)

  implicit val schema: Schema[GetNumberFactOutput] = struct(
    string.required[GetNumberFactOutput]("text", _.text),
    boolean.required[GetNumberFactOutput]("found", _.found),
  )(make).withId(id).addHints(hints)
}
