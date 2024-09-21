package io.github.avapl
package domain.model.error

import java.util.UUID
import scala.util.control.NoStackTrace

package object desk {

  case class DeskNotFound(deskId: UUID) extends NoStackTrace
  case class DuplicateDeskNameForOffice(name: Option[String], officeId: Option[UUID]) extends NoStackTrace
}
