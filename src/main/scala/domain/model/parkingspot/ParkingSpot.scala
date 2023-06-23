package io.github.avapl
package domain.model.parkingspot

import java.util.UUID

case class ParkingSpot(
  id: UUID,
  name: String,
  isAvailable: Boolean,
  notes: List[String],
  //
  isHandicapped: Boolean,
  isUnderground: Boolean,
  //
  officeId: UUID
)
