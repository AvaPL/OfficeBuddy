package io.github.avapl
package adapters.http

import derevo.derive
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

sealed trait ApiError {
  def message: String
}

object ApiError {

  @derive(circeEncoder, circeDecoder, tapirSchema)
  case class BadRequest(message: String) extends ApiError

  case object Unauthorized extends ApiError {
    val message: String = "Unauthorized"
  }

  case object Forbidden extends ApiError {
    val message: String = "Forbidden"
  }

  @derive(circeEncoder, circeDecoder, tapirSchema)
  case class NotFound(message: String) extends ApiError

  @derive(circeEncoder, circeDecoder, tapirSchema)
  case class Conflict(message: String) extends ApiError

  @derive(circeEncoder, circeDecoder, tapirSchema)
  case class InternalServerError(message: String) extends ApiError
}
