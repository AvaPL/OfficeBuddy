package io.github.avapl
package adapters.http

import sttp.tapir.Schema

sealed trait ApiError {
  def message: String
}

object ApiError {

  case class BadRequest(message: String) extends ApiError

  object BadRequest {
    implicit val tapirSchema: Schema[BadRequest] = Schema.derived
  }

  case class NotFound(message: String) extends ApiError

  object NotFound {
    implicit val tapirSchema: Schema[NotFound] = Schema.derived
  }

  case class Conflict(message: String) extends ApiError

  object Conflict {
    implicit val tapirSchema: Schema[Conflict] = Schema.derived
  }

  case class InternalServerError(message: String) extends ApiError

  object InternalServerError {
    implicit val tapirSchema: Schema[InternalServerError] = Schema.derived
  }
}
