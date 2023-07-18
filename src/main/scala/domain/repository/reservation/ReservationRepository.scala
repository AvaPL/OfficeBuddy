package io.github.avapl
package domain.repository.reservation

import domain.model.reservation.{DeskReservation, ReservationState}

import java.util.UUID

trait ReservationRepository[F[_]] {

  def createDeskReservation(deskReservation: DeskReservation): F[DeskReservation]
  def readDeskReservation(reservationId: UUID): F[DeskReservation]
  def updateReservationState(reservationId: UUID, newState: ReservationState): F[Unit]
}
