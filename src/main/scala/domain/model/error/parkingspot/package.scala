package io.github.avapl
package domain.model.error

import java.util.UUID
import scala.util.control.NoStackTrace

package object parkingspot {

  case class ParkingSpotNotFound(parkingSpotId: UUID) extends NoStackTrace
  case class DuplicateParkingSpotNameForOffice(name: Option[String], officeId: Option[UUID]) extends NoStackTrace
}
