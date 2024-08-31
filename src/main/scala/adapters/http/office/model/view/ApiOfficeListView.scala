package io.github.avapl
package adapters.http.office.model.view

import derevo.derive
import domain.model.office.view.OfficeListView
import io.scalaland.chimney.dsl._
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("OfficeListView")
case class ApiOfficeListView(
  offices: List[ApiOfficeView],
  pagination: ApiPagination
)

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Pagination")
case class ApiPagination(
  limit: Int,
  offset: Int,
  hasMoreResults: Boolean
)

object ApiOfficeListView {

  def fromDomain(officeListView: OfficeListView): ApiOfficeListView =
    officeListView.transformInto[ApiOfficeListView]
}
