package io.github.avapl
package domain.model.parkingspot.view

import java.util.UUID

case class ReservableParkingSpotView(
  id: UUID,
  name: String,
  isHandicapped: Boolean,
  isUnderground: Boolean
)
