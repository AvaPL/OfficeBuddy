package io.github.avapl
package adapters.http.parkingspot.model.view

import adapters.http.model.view.ApiPagination
import derevo.derive
import domain.model.parkingspot.view.ParkingSpotListView
import io.scalaland.chimney.dsl._
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("ParkingSpotListView")
case class ApiParkingSpotListView(
  parkingSpots: List[ApiParkingSpotView],
  pagination: ApiPagination
)

object ApiParkingSpotListView {

  def fromDomain(parkingSpotListView: ParkingSpotListView): ApiParkingSpotListView =
    parkingSpotListView.transformInto[ApiParkingSpotListView]
}
