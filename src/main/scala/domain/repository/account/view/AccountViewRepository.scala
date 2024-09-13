package io.github.avapl
package domain.repository.account.view

import domain.model.account.Role
import domain.model.account.view.AccountListView
import java.util.UUID

trait AccountViewRepository[F[_]] {

  /**
   * @param textSearchQuery
   *   substring to search for in the account's first name, last name, or email (case insensitive)
   * @param officeId
   *   assigned office ID or managed office ID
   * @param roles
   *   list of allowed roles
   */
  def listAccounts(
    textSearchQuery: Option[String],
    officeId: Option[UUID],
    roles: Option[List[Role]],
    limit: Int,
    offset: Int
  ): F[AccountListView]
}
