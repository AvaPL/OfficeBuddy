package io.github.avapl
package domain.service.reservation

import domain.model.reservation.CreateDeskReservation
import domain.model.reservation.DeskReservation
import java.util.UUID

class ReservationService[F[_]] {

  def reserveDesk(createDeskReservation: CreateDeskReservation): F[DeskReservation] = ???

  def readDeskReservation(reservationId: UUID): F[DeskReservation] = ???

  def cancelReservation(reservationId: UUID): F[Unit] = ???

  def confirmReservation(reservationId: UUID): F[Unit] = ???

  def rejectReservation(reservationId: UUID): F[Unit] = ???
}
