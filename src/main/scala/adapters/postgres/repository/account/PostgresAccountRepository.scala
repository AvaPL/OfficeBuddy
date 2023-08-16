package io.github.avapl
package adapters.postgres.repository.account

import cats.effect.kernel.MonadCancelThrow
import cats.effect.kernel.Resource
import cats.syntax.all._
import domain.model.error.account.DuplicateAccountEmail
import domain.model.error.account.AccountNotFound
import domain.model.error.office.OfficeNotFound

import cats.data.OptionT
import skunk._
import skunk.codec.all._
import skunk.implicits._

import java.util.UUID

class PostgresAccountRepository[F[_]: MonadCancelThrow](
  session: Resource[F, Session[F]]
) {

  def createUser(user: PostgresUserAccount): F[PostgresUserAccount] =
    session.use { session =>
      for {
        sql <- session.prepare(createUserSql)
        _ <- sql
          .execute(user)
          .recoverWith {
            case SqlState.ForeignKeyViolation(e) if e.constraintName.contains("account_assigned_office_id_fkey") =>
              OfficeNotFound(user.assignedOfficeId.get).raiseError
            case SqlState.UniqueViolation(e) if e.constraintName.contains("account_email_key") =>
              DuplicateAccountEmail(user.email).raiseError
          }
      } yield user
    }

  private lazy val createUserSql: Command[PostgresUserAccount] =
    sql"""
      INSERT INTO account
      VALUES      (${PostgresUserAccount.encoder})
    """.command

  def readUser(userId: UUID): F[PostgresUserAccount]  =
    session.use {session =>
      for {
        sql <- session.prepare(readUserSql)
        user <- OptionT(sql.option(userId)).getOrRaise(AccountNotFound(userId))
      } yield user
    }

  private lazy val readUserSql: Query[UUID, PostgresUserAccount] =
    sql"""
      SELECT *
      FROM   account
      WHERE  id = $uuid
    """.query(PostgresUserAccount.decoder)

  // TODO: Remove commented out code
//  def updateUserAssignedOffice(userId: UUID, officeId: Option[UUID]): F[UserAccount]
//
//  def createOfficeManager(officeManager: OfficeManagerAccount): F[OfficeManagerAccount]
//  def readOfficeManager(officeManagerId: UUID): F[OfficeManagerAccount]
//  def updateOfficeManagerManagedOffices(officeManagerId: UUID, officeIds: List[UUID]): F[OfficeManagerAccount]
//
//  def createSuperAdmin(superAdmin: SuperAdminAccount): F[SuperAdminAccount]
//  def readSuperAdmin(superAdminId: UUID): F[SuperAdminAccount]
//
//  def updateAccountRoles(accountId: UUID, roles: List[Role]): F[Account]
//  def archiveAccount(accountId: UUID): F[Unit]
}

object PostgresAccountRepository {}
