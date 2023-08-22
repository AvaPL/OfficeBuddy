package io.github.avapl
package adapters.postgres.repository.account

import cats.data.NonEmptyList
import cats.data.OptionT
import cats.effect.kernel.MonadCancelThrow
import cats.effect.kernel.Resource
import cats.syntax.all._
import domain.model.account.Role
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
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
      WHERE  id = $uuid AND
             type = 'User'
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

  def createOfficeManager(officeManager: PostgresOfficeManagerAccount): F[PostgresOfficeManagerAccount] =
    session.use { session =>
      for {
        sql <- session.prepare(createOfficeManagerSql)
        _ <- sql
          .execute(officeManager)
          .recoverWith {
            case SqlState.UniqueViolation(e) if e.constraintName.contains("account_email_key") =>
              DuplicateAccountEmail(officeManager.email).raiseError
          }
      } yield officeManager
    }

  private lazy val createOfficeManagerSql: Command[PostgresOfficeManagerAccount] =
    sql"""
      INSERT INTO account
      VALUES      (${PostgresOfficeManagerAccount.encoder})
    """.command

  def readOfficeManager(officeManagerId: UUID): F[PostgresOfficeManagerAccount] =
    session.use { session =>
      for {
        sql <- session.prepare(readOfficeManagerSql)
        user <- OptionT(sql.option(officeManagerId)).getOrRaise(AccountNotFound(officeManagerId))
      } yield user
    }

  private lazy val readOfficeManagerSql: Query[UUID, PostgresOfficeManagerAccount] =
    sql"""
      SELECT *
      FROM   account
      WHERE  id = $uuid AND
             type = 'OfficeManager'
    """.query(PostgresOfficeManagerAccount.decoder)

  def createSuperAdmin(superAdmin: PostgresSuperAdminAccount): F[PostgresSuperAdminAccount] =
    session.use { session =>
      for {
        sql <- session.prepare(createSuperAdminSql)
        _ <- sql
          .execute(superAdmin)
          .recoverWith {
            case SqlState.UniqueViolation(e) if e.constraintName.contains("account_email_key") =>
              DuplicateAccountEmail(superAdmin.email).raiseError
          }
      } yield superAdmin
    }

  private lazy val createSuperAdminSql: Command[PostgresSuperAdminAccount] =
    sql"""
      INSERT INTO account
      VALUES      (${PostgresSuperAdminAccount.encoder})
    """.command

  def readSuperAdmin(superAdminId: UUID): F[PostgresSuperAdminAccount] =
    session.use { session =>
      for {
        sql <- session.prepare(readSuperAdminSql)
        user <- OptionT(sql.option(superAdminId)).getOrRaise(AccountNotFound(superAdminId))
      } yield user
    }

  private lazy val readSuperAdminSql: Query[UUID, PostgresSuperAdminAccount] =
    sql"""
      SELECT *
      FROM   account
      WHERE  id = $uuid AND
             type = 'SuperAdmin'
    """.query(PostgresSuperAdminAccount.decoder)

  def updateRoles(accountId: UUID, roles: NonEmptyList[Role]): F[PostgresAccount] = {
    val (_, newType, readAccount) = roles
      .map {
        case SuperAdmin    => (0, "SuperAdmin", readSuperAdmin _)
        case OfficeManager => (1, "OfficeManager", readOfficeManager _)
        case User          => (2, "User", readUser _)
      }
      .sortBy {
        case (roleOrder, _, _) => roleOrder
      }
      .head

    session.use { session =>
      for {
        sql <- session.prepare(updateRolesSql)
        _ <- sql
          .execute(newType *: accountId *: EmptyTuple)
          .ensureOr {
            case Completion.Update(0) => AccountNotFound(accountId)
            case completion           => new RuntimeException(s"Expected 1 updated account, but got: $completion")
          }(_ == Completion.Update(1))
        account <- readAccount(accountId)
      } yield account
    }
  }

  private lazy val updateRolesSql: Command[String *: UUID *: EmptyTuple] =
    sql"""
      UPDATE account
      SET    type = $varchar
      WHERE id = $uuid
    """.command

  def archive(accountId: UUID): F[Unit] =
    session.use { session =>
      session
        .prepare(archiveSql)
        .flatMap { sql =>
          sql.execute(accountId)
        }
        .void
    }

  private lazy val archiveSql: Command[UUID] =
    sql"""
      UPDATE account
      SET    is_archived = 'yes'
      WHERE id = $uuid
    """.command

  def readAccountEmail(accountId: UUID): F[String] =
    session.use { session =>
      for {
        sql <- session.prepare(readAccountEmailSql)
        email <- OptionT(sql.option(accountId)).getOrRaise(AccountNotFound(accountId))
      } yield email
    }

  private lazy val readAccountEmailSql: Query[UUID, String] =
    sql"""
      SELECT email   
      FROM   account
      WHERE id = $uuid
    """.query(varchar)
}
