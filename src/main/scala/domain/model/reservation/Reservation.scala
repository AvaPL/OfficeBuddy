package io.github.avapl
package domain.model.reservation

import java.time.OffsetDateTime
import java.util.UUID

sealed trait Reservation {

  def id: UUID
  def userId: UUID
  def createdAt: OffsetDateTime
  def reservedFrom: OffsetDateTime
  def reservedTo: OffsetDateTime
  def state: ReservationState
  def notes: List[String]
}

case class DeskReservation(
  id: UUID,
  userId: UUID,
  createdAt: OffsetDateTime,
  reservedFrom: OffsetDateTime,
  reservedTo: OffsetDateTime,
  state: ReservationState,
  notes: List[String],
  //
  deskId: UUID
) extends Reservation

case class ParkingSpotReservation(
  id: UUID,
  userId: UUID,
  createdAt: OffsetDateTime,
  reservedFrom: OffsetDateTime,
  reservedTo: OffsetDateTime,
  state: ReservationState,
  notes: List[String],
  //
  parkingSpotId: UUID,
  plateNumber: String
) extends Reservation

case class MeetingRoomReservation(
  id: UUID,
  userId: UUID,
  createdAt: OffsetDateTime,
  reservedFrom: OffsetDateTime,
  reservedTo: OffsetDateTime,
  state: ReservationState,
  notes: List[String],
  //
  meetingRoomId: UUID
) extends Reservation
