package io.github.avapl
package domain.service.reservation

import cats.ApplicativeThrow
import cats.MonadThrow
import cats.effect.Clock
import cats.syntax.all._
import domain.model.error.reservation.InvalidStateTransition
import domain.model.reservation.CreateDeskReservation
import domain.model.reservation.DeskReservation
import domain.model.reservation.Reservation
import domain.model.reservation.ReservationState
import domain.repository.reservation.ReservationRepository
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import util.FUUID

sealed abstract class ReservationService[
  F[_]: MonadThrow,
  R <: Reservation
](
  reservationRepository: ReservationRepository[F, R]
) {

  protected def toReservation(createReservation: R#CreateReservation): F[R]

  def reserve(createReservation: R#CreateReservation): F[R] =
    for {
      reservation <- toReservation(createReservation)
      createdReservation <- reservationRepository.createReservation(reservation)
    } yield createdReservation

  def readReservation(reservationId: UUID): F[R] =
    reservationRepository.readReservation(reservationId)

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

class DeskReservationService[F[_]: Clock: FUUID: MonadThrow](
  reservationRepository: ReservationRepository[F, DeskReservation]
) extends ReservationService[F, DeskReservation](reservationRepository) {

  override protected def toReservation(createReservation: CreateDeskReservation): F[DeskReservation] =
    for {
      reservationId <- FUUID[F].randomUUID()
      createdAt <- nowUtc
    } yield createReservation.toDeskReservation(reservationId, createdAt)

  private lazy val nowUtc =
    Clock[F].realTimeInstant.map(LocalDateTime.ofInstant(_, ZoneOffset.UTC))
}
