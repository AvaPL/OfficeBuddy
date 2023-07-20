package io.github.avapl
package adapters.http.reservation

import derevo.derive
import domain.model.reservation.CreateDeskReservation
import domain.model.reservation.DeskReservation
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
  reservedFrom: LocalDateTime,
  reservedTo: LocalDateTime,
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
