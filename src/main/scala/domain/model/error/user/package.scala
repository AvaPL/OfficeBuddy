package io.github.avapl
package domain.model.error

import java.util.UUID
import scala.util.control.NoStackTrace

package object user {

  case class UserNotFound(userId: UUID) extends NoStackTrace
}
