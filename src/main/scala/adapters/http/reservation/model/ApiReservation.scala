package io.github.avapl
package adapters.http.reservation.model

import derevo.derive
import domain.model.reservation.{CreateDeskReservation, CreateParkingSpotReservation, DeskReservation, ParkingSpotReservation}

import io.scalaland.chimney.dsl._

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Desk reservation")
case class ApiDeskReservation(
  id: UUID,
  userId: UUID,
  createdAt: LocalDateTime,
  reservedFromDate: LocalDate,
  reservedToDate: LocalDate,
  state: ApiReservationState,
  notes: String,
  //
  deskId: UUID
) {

  lazy val toDomain: DeskReservation =
    this.transformInto[DeskReservation]
}

object ApiDeskReservation {

  def fromDomain(deskReservation: DeskReservation): ApiDeskReservation =
    deskReservation.transformInto[ApiDeskReservation]
}

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Desk reservation (create)")
case class ApiCreateDeskReservation(
  userId: UUID,
  reservedFrom: LocalDate,
  reservedTo: LocalDate,
  notes: String,
  //
  deskId: UUID
) {

  lazy val toDomain: CreateDeskReservation =
    this.transformInto[CreateDeskReservation]
}

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Parking spot reservation")
case class ApiParkingSpotReservation(
  id: UUID,
  userId: UUID,
  createdAt: LocalDateTime,
  reservedFromDate: LocalDate,
  reservedToDate: LocalDate,
  state: ApiReservationState,
  notes: String,
  //
  parkingSpotId: UUID,
  plateNumber: String
)

object ApiParkingSpotReservation {

  def fromDomain(parkingSpotReservation: ParkingSpotReservation): ApiParkingSpotReservation =
    parkingSpotReservation.transformInto[ApiParkingSpotReservation]
}

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("Parking spot reservation (create)")
case class ApiCreateParkingSpotReservation(
  userId: UUID,
  reservedFrom: LocalDate,
  reservedTo: LocalDate,
  notes: String,
  //
  parkingSpotId: UUID,
  plateNumber: String
) {

  lazy val toDomain: CreateParkingSpotReservation =
    this.transformInto[CreateParkingSpotReservation]
}