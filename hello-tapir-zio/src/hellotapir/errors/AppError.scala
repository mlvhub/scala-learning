package hellotapir.errors

import zio.http.Status.Forbidden
import zio.json.*

sealed trait AppError
final case class Unauthorized(message: String) extends AppError
final case class NotFound(message: String) extends AppError
final case class InternalServerError(message: String) extends AppError

object AppError:
  given unauthorizedEncoder: JsonEncoder[Unauthorized] =
    DeriveJsonEncoder.gen[Unauthorized]
  given unauthorizedDecoder: JsonDecoder[Unauthorized] =
    DeriveJsonDecoder.gen[Unauthorized]

  given notFoundEncoder: JsonEncoder[NotFound] =
    DeriveJsonEncoder.gen[NotFound]
  given notFoundDecoder: JsonDecoder[NotFound] =
    DeriveJsonDecoder.gen[NotFound]

  given internalServerErrorEncoder: JsonEncoder[InternalServerError] =
    DeriveJsonEncoder.gen[InternalServerError]
  given internalServerErrorDecoder: JsonDecoder[InternalServerError] =
    DeriveJsonDecoder.gen[InternalServerError]
