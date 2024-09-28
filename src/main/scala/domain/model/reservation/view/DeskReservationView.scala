package io.github.avapl
package domain.model.reservation.view

import domain.model.reservation.ReservationState
import java.time.LocalDate
import java.util.UUID

case class DeskReservationView(
  id: UUID,
  reservedFromDate: LocalDate,
  reservedToDate: LocalDate,
  state: ReservationState,
  notes: String,
  user: UserView,
  desk: DeskView
)
