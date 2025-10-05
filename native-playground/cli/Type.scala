package cli

import smithy4s.Enumeration
import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.schema.EnumTag
import smithy4s.schema.Schema.enumeration

sealed abstract class Type(_value: String, _name: String, _intValue: Int, _hints: Hints) extends Enumeration.Value {
  override type EnumType = Type
  override val value: String = _value
  override val name: String = _name
  override val intValue: Int = _intValue
  override val hints: Hints = _hints
  override def enumeration: Enumeration[EnumType] = Type
  @inline final def widen: Type = this
}
object Type extends Enumeration[Type] with ShapeTag.Companion[Type] {
  val id: ShapeId = ShapeId("cli", "Type")

  val hints: Hints = Hints.empty

  case object TRIVIA extends Type("trivia", "TRIVIA", 0, Hints.empty)
  case object MATH extends Type("math", "MATH", 1, Hints.empty)
  case object DATE extends Type("date", "DATE", 2, Hints.empty)
  case object YEAR extends Type("year", "YEAR", 3, Hints.empty)

  val values: List[Type] = List(
    TRIVIA,
    MATH,
    DATE,
    YEAR,
  )
  val tag: EnumTag[Type] = EnumTag.ClosedStringEnum
  implicit val schema: Schema[Type] = enumeration(tag, values).withId(id).addHints(hints)
}
