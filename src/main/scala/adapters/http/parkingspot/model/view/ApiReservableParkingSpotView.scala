package io.github.avapl
package adapters.http.parkingspot.model.view

import derevo.derive
import domain.model.parkingspot.view.ReservableParkingSpotView
import io.scalaland.chimney.dsl._
import java.util.UUID
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("ReservableParkingSpotView")
case class ApiReservableParkingSpotView(
  id: UUID,
  name: String,
  isHandicapped: Boolean,
  isUnderground: Boolean
)

object ApiReservableParkingSpotView {

  def fromDomain(reservableParkingSpotView: ReservableParkingSpotView): ApiReservableParkingSpotView =
    reservableParkingSpotView.transformInto[ApiReservableParkingSpotView]
}
