package io.github.avapl
package adapters.http.reservation

import derevo.derive
import domain.model.reservation.CreateDeskReservation
import domain.model.reservation.DeskReservation
import domain.model.reservation.ReservationState
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
  state: String, // TODO: Use enumeration instead of String
  notes: String,
  //
  deskId: UUID
) {

  lazy val toDomain: DeskReservation =
    this
      .into[DeskReservation]
      .withFieldComputed(_.state, r => ReservationState.withNameInsensitive(r.state))
      .transform
}

object ApiDeskReservation {

  def fromDomain(deskReservation: DeskReservation): ApiDeskReservation =
    deskReservation
      .into[ApiDeskReservation]
      .withFieldComputed(_.state, _.state.entryName)
      .transform
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
