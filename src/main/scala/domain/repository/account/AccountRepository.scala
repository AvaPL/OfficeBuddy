package io.github.avapl
package domain.repository.account

import domain.model.account._
import java.util.UUID

trait AccountRepository[F[_]] {

  def create(account: Account): F[Account]
  def read(accountId: UUID): F[Account]
  def updateAssignedOffice(accountId: UUID, officeId: Option[UUID]): F[Account]
  def updateManagedOffices(accountId: UUID, managedOfficeIds: List[UUID]): F[Account]
  def updateRole(accountId: UUID, role: Role): F[Account]
  def archive(accountId: UUID): F[Unit]
}
