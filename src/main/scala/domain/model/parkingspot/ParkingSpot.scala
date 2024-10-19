package io.github.avapl
package domain.model.parkingspot

import java.util.UUID
import io.scalaland.chimney.dsl._

case class ParkingSpot(
  id: UUID,
  name: String,
  isAvailable: Boolean,
  notes: List[String],
  //
  isHandicapped: Boolean,
  isUnderground: Boolean,
  //
  officeId: UUID,
  //
  isArchived: Boolean = false
)

case class CreateParkingSpot(
  name: String,
  isAvailable: Boolean,
  notes: List[String],
  //
  isHandicapped: Boolean,
  isUnderground: Boolean,
  //
  officeId: UUID
) {

  def toParkingSpot(parkingSpotId: UUID): ParkingSpot =
    this
      .into[ParkingSpot]
      .withFieldConst(_.id, parkingSpotId)
      .withFieldConst(_.isArchived, false)
      .transform
}

case class UpdateParkingSpot(
  name: Option[String] = None,
  isAvailable: Option[Boolean] = None,
  notes: Option[List[String]] = None,
  //
  isHandicapped: Option[Boolean] = None,
  isUnderground: Option[Boolean] = None,
  //
  officeId: Option[UUID] = None
)
