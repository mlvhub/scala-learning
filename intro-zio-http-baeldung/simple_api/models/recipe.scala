package simple_api.models

import zio.json.*
import io.getquill._

case class Recipe(id: Long, name: String, ingredients: List[String])

object Recipe:
  given JsonEncoder[Recipe] = DeriveJsonEncoder.gen[Recipe]
  given JsonDecoder[Recipe] = DeriveJsonDecoder.gen[Recipe]
