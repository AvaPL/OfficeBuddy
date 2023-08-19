package io.github.avapl
package adapters.postgres.repository.account

import cats.data.OptionT
import cats.effect.kernel.MonadCancelThrow
import cats.effect.kernel.Resource
import cats.syntax.all._
import domain.model.error.account.AccountNotFound
import domain.model.error.account.DuplicateAccountEmail
import domain.model.error.office.OfficeNotFound
import java.util.UUID
import skunk._
import skunk.codec.all._
import skunk.data.Completion
import skunk.implicits._

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

  def readUser(userId: UUID): F[PostgresUserAccount] =
    session.use { session =>
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

  def updateUserAssignedOffice(userId: UUID, officeId: Option[UUID]): F[PostgresUserAccount] =
    session.use { session =>
      for {
        sql <- session.prepare(updateUserAssignedOfficeSql)
        _ <- sql
          .execute(officeId *: userId *: EmptyTuple)
          .ensureOr {
            case Completion.Update(0) => AccountNotFound(userId)
            case completion           => new RuntimeException(s"Expected 1 updated user, but got: $completion")
          }(_ == Completion.Update(1))
          .recoverWith {
            case SqlState.ForeignKeyViolation(e) if e.constraintName.contains("account_assigned_office_id_fkey") =>
              OfficeNotFound(officeId.get).raiseError
          }
        user <- readUser(userId)
      } yield user
    }

  private lazy val updateUserAssignedOfficeSql: Command[Option[UUID] *: UUID *: EmptyTuple] =
    sql"""
      UPDATE account
      SET    assigned_office_id = ${uuid.opt}
      WHERE id = $uuid
    """.command

  // TODO: Remove commented out code
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
