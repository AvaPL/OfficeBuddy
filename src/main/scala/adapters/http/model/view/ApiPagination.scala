package io.github.avapl
package adapters.http.model.view

import derevo.derive
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Pagination")
case class ApiPagination(
  limit: Int,
  offset: Int,
  hasMoreResults: Boolean
)
