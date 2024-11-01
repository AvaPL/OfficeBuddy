package io.github.avapl
package domain.model.reservation

import io.scalaland.chimney.dsl._
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

/**
 * Represents a generic reservation.
 *
 * All dates are assumed to be in the UTC. It's assumed that every client request will convert the dates selected by the
 * user to UTC before sending them to the server.
 */
sealed trait Reservation {

  type CreateReservation

  def id: UUID
  def userId: UUID
  def createdAt: LocalDateTime
  def reservedFrom: LocalDateTime
  def reservedTo: LocalDateTime
  def state: ReservationState
  def notes: String
}

case class DeskReservation(
  id: UUID,
  userId: UUID,
  createdAt: LocalDateTime,
  reservedFromDate: LocalDate,
  reservedToDate: LocalDate,
  state: ReservationState,
  notes: String,
  //
  deskId: UUID
) extends Reservation {

  override type CreateReservation = CreateDeskReservation

  override lazy val reservedFrom: LocalDateTime = reservedFromDate.atStartOfDay()
  override lazy val reservedTo: LocalDateTime = reservedToDate.atTime(LocalTime.MAX)
}

case class CreateDeskReservation(
  userId: UUID,
  reservedFrom: LocalDate,
  reservedTo: LocalDate,
  notes: String,
  //
  deskId: UUID
) {

  def toDeskReservation(reservationId: UUID, createdAt: LocalDateTime): DeskReservation =
    this
      .into[DeskReservation]
      .withFieldConst(_.id, reservationId)
      .withFieldConst(_.createdAt, createdAt)
      .withFieldComputed(_.reservedFromDate, _.reservedFrom)
      .withFieldComputed(_.reservedToDate, _.reservedTo)
      .withFieldConst(_.state, ReservationState.Pending)
      .transform
}

case class ParkingSpotReservation(
  id: UUID,
  userId: UUID,
  createdAt: LocalDateTime,
  reservedFromDate: LocalDate,
  reservedToDate: LocalDate,
  state: ReservationState,
  notes: String,
  //
  parkingSpotId: UUID,
  plateNumber: String
) extends Reservation {

  override type CreateReservation = CreateParkingSpotReservation

  override lazy val reservedFrom: LocalDateTime = reservedFromDate.atStartOfDay()
  override lazy val reservedTo: LocalDateTime = reservedToDate.atTime(LocalTime.MAX)
}

case class CreateParkingSpotReservation(
  userId: UUID,
  reservedFrom: LocalDate,
  reservedTo: LocalDate,
  notes: String,
  //
  parkingSpotId: UUID,
  plateNumber: String
) {

  def toParkingSpotReservation(reservationId: UUID, createdAt: LocalDateTime): ParkingSpotReservation =
    this
      .into[ParkingSpotReservation]
      .withFieldConst(_.id, reservationId)
      .withFieldConst(_.createdAt, createdAt)
      .withFieldComputed(_.reservedFromDate, _.reservedFrom)
      .withFieldComputed(_.reservedToDate, _.reservedTo)
      .withFieldConst(_.state, ReservationState.Pending)
      .transform
}

case class MeetingRoomReservation(
  id: UUID,
  userId: UUID,
  createdAt: LocalDateTime,
  reservedFrom: LocalDateTime,
  reservedTo: LocalDateTime,
  state: ReservationState,
  notes: String,
  //
  meetingRoomId: UUID
) extends Reservation
