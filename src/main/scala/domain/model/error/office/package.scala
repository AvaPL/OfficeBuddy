package io.github.avapl
package domain.model.error

import java.util.UUID
import scala.util.control.NoStackTrace

package object office {

  case class OfficeNotFound(officeId: UUID) extends NoStackTrace
  case class DuplicateOfficeName(name: String) extends NoStackTrace
}
