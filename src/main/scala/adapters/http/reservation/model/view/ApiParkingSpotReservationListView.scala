package io.github.avapl
package adapters.http.reservation.model.view

import adapters.http.model.view.ApiPagination
import derevo.derive
import domain.model.reservation.view.ParkingSpotReservationListView
import io.scalaland.chimney.dsl._
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("ParkingSpotReservationListView")
case class ApiParkingSpotReservationListView(
  reservations: List[ApiParkingSpotReservationView],
  pagination: ApiPagination
)

object ApiParkingSpotReservationListView {

  def fromDomain(parkingSpotReservationListView: ParkingSpotReservationListView): ApiParkingSpotReservationListView =
    parkingSpotReservationListView.transformInto[ApiParkingSpotReservationListView]
}
