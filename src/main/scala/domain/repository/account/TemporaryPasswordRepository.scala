package io.github.avapl
package domain.repository.account

import java.util.UUID

/**
 * Represents a capability to set a temporary password for an account.
 */
trait TemporaryPasswordRepository[F[_]] {

  def setTemporaryPassword(accountId: UUID, temporaryPassword: String): F[Unit]
}
