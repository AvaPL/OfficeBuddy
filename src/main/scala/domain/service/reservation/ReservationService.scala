package io.github.avapl
package domain.service.reservation

import cats.FlatMap
import cats.effect.Clock
import cats.implicits._
import domain.model.reservation.CreateDeskReservation
import domain.model.reservation.DeskReservation
import domain.model.reservation.ReservationState
import domain.repository.reservation.ReservationRepository
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import util.FUUID

// TODO: Add unit tests
class ReservationService[F[_]: Clock: FlatMap: FUUID](
  reservationRepository: ReservationRepository[F]
) {

  def reserveDesk(createDeskReservation: CreateDeskReservation): F[DeskReservation] =
    for {
      reservationId <- FUUID[F].randomUUID()
      createdAt <- nowUtc
      reservation = createDeskReservation.toDeskReservation(reservationId, createdAt)
      createdReservation <- reservationRepository.createDeskReservation(reservation)
    } yield createdReservation

  private lazy val nowUtc =
    Clock[F].realTimeInstant.map(LocalDateTime.ofInstant(_, ZoneOffset.UTC))

  def readDeskReservation(reservationId: UUID): F[DeskReservation] =
    reservationRepository.readDeskReservation(reservationId)

  def cancelReservation(reservationId: UUID): F[Unit] =
    reservationRepository.updateReservationState(reservationId, ReservationState.Cancelled)

  def confirmReservation(reservationId: UUID): F[Unit] =
    reservationRepository.updateReservationState(reservationId, ReservationState.Confirmed)

  def rejectReservation(reservationId: UUID): F[Unit] =
    reservationRepository.updateReservationState(reservationId, ReservationState.Rejected)
}
