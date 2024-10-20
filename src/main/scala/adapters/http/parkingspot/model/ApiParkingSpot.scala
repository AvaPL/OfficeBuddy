package io.github.avapl
package adapters.http.parkingspot.model

import derevo.derive
import domain.model.parkingspot.CreateParkingSpot
import domain.model.parkingspot.ParkingSpot
import domain.model.parkingspot.UpdateParkingSpot
import io.scalaland.chimney.dsl._
import java.util.UUID
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("ParkingSpot")
case class ApiParkingSpot(
  id: UUID,
  name: String,
  isAvailable: Boolean,
  notes: List[String],
  //
  isHandicapped: Boolean,
  isUnderground: Boolean,
  //
  officeId: UUID,
  //
  isArchived: Boolean
)

object ApiParkingSpot {

  def fromDomain(parkingSpot: ParkingSpot): ApiParkingSpot =
    parkingSpot.transformInto[ApiParkingSpot]
}

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("ParkingSpot (create)")
case class ApiCreateParkingSpot(
  name: String,
  isAvailable: Boolean,
  notes: List[String],
  //
  isHandicapped: Boolean,
  isUnderground: Boolean,
  //
  officeId: UUID
) {

  lazy val toDomain: CreateParkingSpot =
    this.transformInto[CreateParkingSpot]
}

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("ParkingSpot (update)")
case class ApiUpdateParkingSpot(
  name: Option[String],
  isAvailable: Option[Boolean],
  notes: Option[List[String]],
  //
  isHandicapped: Option[Boolean],
  isUnderground: Option[Boolean],
  //
  officeId: Option[UUID],
  //
  isArchived: Option[Boolean]
) {

  lazy val toDomain: UpdateParkingSpot =
    this.transformInto[UpdateParkingSpot]
}
