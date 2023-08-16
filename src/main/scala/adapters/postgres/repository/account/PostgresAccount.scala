package io.github.avapl
package adapters.postgres.repository.account

import domain.model.account.OfficeManagerAccount
import domain.model.account.SuperAdminAccount
import domain.model.account.UserAccount
import java.util.UUID
import skunk._
import skunk.Encoder
import skunk.codec.all._
import skunk.implicits._

sealed trait PostgresAccount

case class PostgresUserAccount(
  id: UUID,
  email: String,
  //
  isArchived: Boolean,
  //
  assignedOfficeId: Option[UUID]
) extends PostgresAccount

object PostgresUserAccount {

  lazy val encoder: Encoder[PostgresUserAccount] =
    (
      uuid *: // id
        varchar *: // email
        bool *: // is_archived
        uuid.opt // assigned_office_id
    ).contramap { postgresUserAccount =>
      postgresUserAccount.id *:
        postgresUserAccount.email *:
        postgresUserAccount.isArchived *:
        postgresUserAccount.assignedOfficeId *:
        EmptyTuple
    }

  lazy val decoder: Decoder[PostgresUserAccount] =
    (
      uuid *: // id
        varchar *: // email
        bool *: // is_archived
        uuid.opt // assigned_office_id
    ).map {
      case id *: email *: isArchived *: assignedOfficeId *: EmptyTuple =>
        PostgresUserAccount(id, email, isArchived, assignedOfficeId)
    }
}

case class PostgresOfficeManagerAccount(
  id: UUID,
  email: String,
  //
  isArchived: Boolean
) extends PostgresAccount

case class PostgresSuperAdminAccount(
  id: UUID,
  email: String,
  //
  isArchived: Boolean
) extends PostgresAccount

object PostgresAccount {

  def fromUserAccount(userAccount: UserAccount): PostgresUserAccount =
    PostgresUserAccount(
      id = userAccount.id,
      email = userAccount.email,
      isArchived = userAccount.isArchived,
      assignedOfficeId = userAccount.assignedOfficeId
    )

  def fromOfficeManagerAccount(officeManagerAccount: OfficeManagerAccount): PostgresOfficeManagerAccount =
    PostgresOfficeManagerAccount(
      id = officeManagerAccount.id,
      email = officeManagerAccount.email,
      isArchived = officeManagerAccount.isArchived
    )

  def fromSuperAdminAccount(superAdminAccount: SuperAdminAccount): PostgresSuperAdminAccount =
    PostgresSuperAdminAccount(
      id = superAdminAccount.id,
      email = superAdminAccount.email,
      isArchived = superAdminAccount.isArchived
    )
}
