package io.github.avapl
package adapters.postgres.repository.account

import cats.syntax.all._
import domain.model.account.OfficeManagerAccount
import domain.model.account.SuperAdminAccount
import domain.model.account.UserAccount

import java.util.UUID
import skunk._
import skunk.Encoder
import skunk.codec.all._
import skunk.implicits._

import scala.annotation.nowarn

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
        varchar *: // type
        uuid.opt // assigned_office_id
    ).contramap { postgresUserAccount =>
      postgresUserAccount.id *:
        postgresUserAccount.email *:
        postgresUserAccount.isArchived *:
        "User" *:
        postgresUserAccount.assignedOfficeId *:
        EmptyTuple
    }

  @nowarn("msg=match may not be exhaustive")
  lazy val decoder: Decoder[PostgresUserAccount] =
    (
      uuid *: // id
        varchar *: // email
        bool *: // is_archived
        varchar *: // type
        uuid.opt // assigned_office_id
    ).map {
      case id *: email *: isArchived *: _ *: assignedOfficeId *: EmptyTuple =>
        PostgresUserAccount(id, email, isArchived, assignedOfficeId)
    }
}

case class PostgresOfficeManagerAccount(
  id: UUID,
  email: String,
  //
  isArchived: Boolean
) extends PostgresAccount

object PostgresOfficeManagerAccount {

  lazy val encoder: Encoder[PostgresOfficeManagerAccount] =
    (
      uuid *: // id
        varchar *: // email
        bool *: // is_archived
        varchar *: // type
        uuid.opt // assigned_office_id
    ).contramap { postgresOfficeManagerAccount =>
      postgresOfficeManagerAccount.id *:
        postgresOfficeManagerAccount.email *:
        postgresOfficeManagerAccount.isArchived *:
        "OfficeManager" *:
        Option.empty[UUID] *:
        EmptyTuple
    }

  @nowarn("msg=match may not be exhaustive")
  lazy val decoder: Decoder[PostgresOfficeManagerAccount] =
    (
      uuid *: // id
        varchar *: // email
        bool *: // is_archived
        varchar *: // type
        uuid.opt // assigned_office_id
    ).map {
      case id *: email *: isArchived *: _ *: _ *: EmptyTuple =>
        PostgresOfficeManagerAccount(id, email, isArchived)
    }
}

case class PostgresSuperAdminAccount(
  id: UUID,
  email: String,
  //
  isArchived: Boolean
) extends PostgresAccount

object PostgresSuperAdminAccount {

  lazy val encoder: Encoder[PostgresSuperAdminAccount] =
    (
      uuid *: // id
        varchar *: // email
        bool *: // is_archived
        varchar *: // type
        uuid.opt // assigned_office_id
      ).contramap { postgresSuperAdminAccount =>
      postgresSuperAdminAccount.id *:
        postgresSuperAdminAccount.email *:
        postgresSuperAdminAccount.isArchived *:
        "SuperAdmin" *:
        Option.empty[UUID] *:
        EmptyTuple
    }

  @nowarn("msg=match may not be exhaustive")
  lazy val decoder: Decoder[PostgresSuperAdminAccount] =
    (
      uuid *: // id
        varchar *: // email
        bool *: // is_archived
        varchar *: // type
        uuid.opt // assigned_office_id
      ).map {
      case id *: email *: isArchived *: _ *: _ *: EmptyTuple =>
        PostgresSuperAdminAccount(id, email, isArchived)
    }
}

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
