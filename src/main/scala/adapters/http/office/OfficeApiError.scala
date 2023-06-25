package io.github.avapl
package adapters.http.office

import sttp.tapir.Schema

case class OfficeApiError(message: String)

object OfficeApiError {
  implicit val tapirSchema: Schema[OfficeApiError] = Schema.derived
}
