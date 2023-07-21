package io.github.avapl
package domain.service.reservation

import cats.ApplicativeThrow
import cats.MonadThrow
import cats.effect.Clock
import cats.syntax.all._
import domain.model.error.reservation.InvalidStateTransition
import domain.model.reservation.CreateDeskReservation
import domain.model.reservation.DeskReservation
import domain.model.reservation.ReservationState
import domain.repository.reservation.ReservationRepository
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import util.FUUID

// TODO: Add unit tests
class ReservationService[F[_]: MonadThrow: Clock: FUUID](
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
    updateReservationState(reservationId, ReservationState.Cancelled)

  private def updateReservationState(reservationId: UUID, newState: ReservationState): F[Unit] =
    for {
      _ <- validateReservationStateTransition(reservationId, newState)
      _ <- reservationRepository.updateReservationState(reservationId, newState)
    } yield ()

  private def validateReservationStateTransition(reservationId: UUID, newState: ReservationState) =
    for {
      currentState <- reservationRepository.readReservationState(reservationId)
      _ <- ApplicativeThrow[F].raiseWhen {
        !isValidStateTransition(currentState, newState)
      } {
        InvalidStateTransition(reservationId, currentState, newState)
      }
    } yield ()

  private def isValidStateTransition(currentState: ReservationState, newState: ReservationState) = {
    import ReservationState._
    val validStateTransitions: Set[ReservationState] = currentState match {
      case Pending   => Set(Pending, Cancelled, Confirmed, Rejected)
      case Confirmed => Set(Confirmed, Cancelled, Rejected)
      case Cancelled => Set(Cancelled)
      case Rejected  => Set(Rejected)
    }
    validStateTransitions.contains(newState)
  }

  def confirmReservation(reservationId: UUID): F[Unit] =
    updateReservationState(reservationId, ReservationState.Confirmed)

  def rejectReservation(reservationId: UUID): F[Unit] =
    updateReservationState(reservationId, ReservationState.Rejected)
}
