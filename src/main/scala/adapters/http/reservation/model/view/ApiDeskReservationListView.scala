package io.github.avapl
package adapters.http.reservation.model.view

import adapters.http.model.view.ApiPagination
import derevo.derive
import domain.model.reservation.view.DeskReservationListView
import io.scalaland.chimney.dsl._
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("DeskReservationListView")
case class ApiDeskReservationListView(
  reservations: List[ApiDeskReservationView],
  pagination: ApiPagination
)

object ApiDeskReservationListView {

  def fromDomain(deskReservationListView: DeskReservationListView): ApiDeskReservationListView =
    deskReservationListView.transformInto[ApiDeskReservationListView]
}
