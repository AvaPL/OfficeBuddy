package io.github.avapl
package domain.model.error

import java.util.UUID
import scala.util.control.NoStackTrace

package object account {

  case class DuplicateAccountEmail(email: String) extends NoStackTrace
  case class AccountNotFound(id: UUID) extends NoStackTrace
}
