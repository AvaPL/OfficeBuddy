package io.github.avapl
package domain.model.reservation

import io.scalaland.chimney.dsl._
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.UUID

sealed trait Reservation {

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
  reservedFrom: LocalDateTime,
  reservedTo: LocalDateTime,
  state: ReservationState,
  notes: String,
  //
  deskId: UUID
) extends Reservation

case class CreateDeskReservation(
  userId: UUID,
  reservedFrom: LocalDateTime,
  reservedTo: LocalDateTime,
  notes: String,
  //
  deskId: UUID
) {

  def toDeskReservation(reservationId: UUID, createdAt: LocalDateTime): DeskReservation =
    this
      .into[DeskReservation]
      .withFieldConst(_.id, reservationId)
      .withFieldConst(_.createdAt, createdAt)
      .withFieldConst(_.state, ReservationState.Pending)
      .transform
}

case class ParkingSpotReservation(
  id: UUID,
  userId: UUID,
  createdAt: LocalDateTime,
  reservedFrom: LocalDateTime,
  reservedTo: LocalDateTime,
  state: ReservationState,
  notes: String,
  //
  parkingSpotId: UUID,
  plateNumber: String
) extends Reservation

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
