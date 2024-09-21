package io.github.avapl
package adapters.http.desk.view

import adapters.http.model.view.ApiPagination
import derevo.derive
import domain.model.desk.view.DeskListView
import io.scalaland.chimney.dsl._
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("DeskListView")
case class ApiDeskListView(
  desks: List[ApiDeskView],
  pagination: ApiPagination
)

object ApiDeskListView {

  def fromDomain(deskListView: DeskListView): ApiDeskListView =
    deskListView.transformInto[ApiDeskListView]
}
