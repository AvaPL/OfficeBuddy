package io.github.avapl
package domain.repository.reservation

import domain.model.reservation.Reservation
import domain.model.reservation.ReservationState
import java.util.UUID

trait ReservationRepository[F[_], R <: Reservation] {

  def createReservation(deskReservation: R): F[R]
  def readReservation(reservationId: UUID): F[R]

  def readReservationState(reservationId: UUID): F[ReservationState]
  def updateReservationState(
    reservationId: UUID,
    newState: ReservationState
  ): F[Unit] // TODO: Return the updated reservation?
}
