package io.github.avapl
package adapters.postgres.repository.account

import adapters.postgres.repository._uuid
import cats.data.OptionT
import cats.effect.kernel.MonadCancelThrow
import cats.effect.kernel.Resource
import cats.syntax.all._
import domain.model.account._
import domain.model.error.account.AccountNotFound
import domain.model.error.account.DuplicateAccountEmail
import domain.model.error.office.OfficeNotFound
import enumeratum.values.StringEnum
import enumeratum.values.StringEnumEntry
import io.scalaland.chimney.dsl._
import java.util.UUID
import scala.annotation.nowarn
import skunk._
import skunk.codec.all._
import skunk.data.Completion
import skunk.implicits._

class PostgresAccountRepository[F[_]: MonadCancelThrow](
  session: Resource[F, Session[F]]
) {

  import PostgresAccountRepository._
  import PostgresAccountRepository.PostgresAccountType._

  def create(account: Account): F[Account] =
    session.use { session =>
      for {
        sql <- session.prepare(createAccountSql)
        _ <- sql
          .execute(account)
          .recoverWith {
            case SqlState.ForeignKeyViolation(e) if e.constraintName.contains("account_assigned_office_id_fkey") =>
              OfficeNotFound(account.assignedOfficeId.get).raiseError
            case SqlState.UniqueViolation(e) if e.constraintName.contains("account_email_key") =>
              DuplicateAccountEmail(account.email).raiseError
          }
      } yield account
    }

  private lazy val createAccountSql: Command[Account] =
    sql"""
      INSERT INTO account
      VALUES      ($accountEncoder)
    """.command

  def read(accountId: UUID): F[Account] =
    session.use { session =>
      for {
        sql <- session.prepare(readAccountSql)
        account <- OptionT(sql.option(accountId)).getOrRaise(AccountNotFound(accountId))
      } yield account
    }

  private lazy val readAccountSql: Query[UUID, Account] =
    sql"""
      SELECT *
      FROM   account
      WHERE  id = $uuid
    """.query(accountDecoder)

  def updateAssignedOffice(accountId: UUID, officeId: Option[UUID]): F[Account] =
    session.use { session =>
      for {
        sql <- session.prepare(updateAssignedOfficeSql)
        _ <- sql
          .execute(officeId *: accountId *: EmptyTuple)
          .ensureOr {
            case Completion.Update(0) => AccountNotFound(accountId)
            case completion           => new RuntimeException(s"Expected 1 updated account, but got: $completion")
          }(_ == Completion.Update(1))
          .recoverWith {
            case SqlState.ForeignKeyViolation(e) if e.constraintName.contains("account_assigned_office_id_fkey") =>
              OfficeNotFound(officeId.get).raiseError
          }
        account <- read(accountId)
      } yield account
    }

  private lazy val updateAssignedOfficeSql: Command[Option[UUID] *: UUID *: EmptyTuple] =
    sql"""
      UPDATE account
      SET    assigned_office_id = ${uuid.opt}
      WHERE id = $uuid
    """.command

  // TODO: Add tests
  def updateManagedOffices(accountId: UUID, managedOfficeIds: List[UUID]): F[Account] =
    session.use { session =>
      for {
        sql <- session.prepare(updateManagedOfficesSql)
        _ <- sql
          .execute(managedOfficeIds *: accountId *: EmptyTuple)
          .ensureOr {
            case Completion.Update(0) => AccountNotFound(accountId)
            case completion           => new RuntimeException(s"Expected 1 updated account, but got: $completion")
          }(_ == Completion.Update(1))
        // TODO: Add a constraint on the managed_office_ids column to ensure that the office_id exists and recover here with proper error
        account <- read(accountId)
      } yield account
    }

  private lazy val updateManagedOfficesSql: Command[List[UUID] *: UUID *: EmptyTuple] =
    sql"""
      UPDATE account
      SET    managed_office_ids = ${_uuid}
      WHERE id = $uuid
    """.command

  def updateRole(accountId: UUID, role: Role): F[Account] = {
    session.use { session =>
      for {
        sql <- session.prepare(updateRoleSql)
        _ <- sql
          .execute(PostgresAccountType.fromDomain(role) *: accountId *: EmptyTuple)
          .ensureOr {
            case Completion.Update(0) => AccountNotFound(accountId)
            case completion           => new RuntimeException(s"Expected 1 updated account, but got: $completion")
          }(_ == Completion.Update(1))
        account <- read(accountId)
      } yield account
    }
  }

  private lazy val updateRoleSql: Command[PostgresAccountType *: UUID *: EmptyTuple] =
    sql"""
      UPDATE account
      SET    type = $accountTypeCodec
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

object PostgresAccountRepository {

  sealed abstract class PostgresAccountType(val value: String) extends StringEnumEntry {
    lazy val toDomain: Role = this.transformInto[Role]
  }

  object PostgresAccountType extends StringEnum[PostgresAccountType] {

    case object User extends PostgresAccountType("User")
    case object OfficeManager extends PostgresAccountType("OfficeManager")
    case object SuperAdmin extends PostgresAccountType("SuperAdmin")

    override val values: IndexedSeq[PostgresAccountType] = findValues

    lazy val accountTypeCodec: Codec[PostgresAccountType] =
      varchar.imap[PostgresAccountType](PostgresAccountType.withValue)(_.value)

    def fromDomain(role: Role): PostgresAccountType =
      role.transformInto[PostgresAccountType]
  }

  lazy val accountEncoder: Encoder[Account] =
    (
      uuid *: // id
        varchar *: // first_name
        varchar *: // last_name
        varchar *: // email
        bool *: // is_archived
        PostgresAccountType.accountTypeCodec *: // type
        uuid.opt *: // assigned_office_id
        _uuid // managed_office_ids
    ).contramap { account =>
      account.id *:
        account.email *:
        account.firstName *:
        account.lastName *:
        account.isArchived *:
        PostgresAccountType.fromDomain(account.role) *:
        account.assignedOfficeId *:
        managedOfficeIdsFromDomain(account) *:
        EmptyTuple
    }

  private def managedOfficeIdsFromDomain(account: Account) =
    account match {
      case officeManager: OfficeManagerAccount => officeManager.managedOfficeIds
      case _                                   => Nil
    }

  @nowarn("msg=match may not be exhaustive")
  lazy val accountDecoder: Decoder[Account] =
    (
      uuid *: // id
        varchar *: // first_name
        varchar *: // last_name
        varchar *: // email
        bool *: // is_archived
        PostgresAccountType.accountTypeCodec *: // type
        uuid.opt *: // assigned_office_id
        _uuid // managed_office_ids
    ).map {
      case id *: email *: firstName *: lastName *: isArchived *: accountType *: assignedOfficeId *: managedOfficeIds *: EmptyTuple =>
        accountType match {
          case PostgresAccountType.User =>
            UserAccount(
              id = id,
              firstName = firstName,
              lastName = lastName,
              email = email,
              isArchived = isArchived,
              assignedOfficeId = assignedOfficeId
            )
          case PostgresAccountType.OfficeManager =>
            OfficeManagerAccount(
              id = id,
              firstName = firstName,
              lastName = lastName,
              email = email,
              isArchived = isArchived,
              managedOfficeIds = managedOfficeIds
            )
          case PostgresAccountType.SuperAdmin =>
            SuperAdminAccount(
              id = id,
              firstName = firstName,
              lastName = lastName,
              email = email,
              isArchived = isArchived,
              assignedOfficeId = assignedOfficeId,
              managedOfficeIds = managedOfficeIds
            )
        }
    }
}
