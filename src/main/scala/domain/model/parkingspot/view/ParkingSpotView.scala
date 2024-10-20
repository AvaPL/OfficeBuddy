package io.github.avapl
package domain.model.parkingspot.view

import java.util.UUID

case class ParkingSpotView(
  id: UUID,
  name: String,
  isAvailable: Boolean,
  isHandicapped: Boolean,
  isUnderground: Boolean
)
