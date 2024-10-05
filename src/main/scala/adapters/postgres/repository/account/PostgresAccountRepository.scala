package io.github.avapl
package adapters.postgres.repository.account

import adapters.postgres.repository._uuid
import adapters.postgres.repository.SessionOps
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
import skunk.exception.PostgresErrorException
import skunk.implicits._

class PostgresAccountRepository[F[_]: MonadCancelThrow](
  session: Resource[F, Session[F]]
) {

  import PostgresAccountRepository._
  import PostgresAccountRepository.PostgresAccountType._

  def create(account: Account): F[Account] = {
    val managedOfficeIds = managedOfficeIdsFromDomain(account)
    val accountIdToOfficeId = managedOfficeIds.map(account.id -> _)
    session.use { session =>
      for {
        insertAccount <- session.prepare(insertAccountSql)
        insertManagedOffices <- session.prepareIf(accountIdToOfficeId.nonEmpty)(
          insertManagedOfficesSql(accountIdToOfficeId)
        )
        _ <- session.transaction
          .use { _ =>
            insertAccount.execute(account) *>
              insertManagedOffices.execute(accountIdToOfficeId)
          }
          .recoverWith {
            case SqlState.ForeignKeyViolation(e) if e.constraintName.contains("account_assigned_office_id_fkey") =>
              OfficeNotFound(account.assignedOfficeId.get).raiseError
            case SqlState.UniqueViolation(e) if e.constraintName.contains("account_email_key") =>
              DuplicateAccountEmail(account.email).raiseError
            case SqlState.ForeignKeyViolation(e)
                if e.constraintName.contains("account_managed_office_office_id_fkey") =>
              recoverOnManagedOfficeNotFound(e, managedOfficeIds)
          }
      } yield account
    }
  }

  private def recoverOnManagedOfficeNotFound[T](e: PostgresErrorException, managedOfficeIds: List[UUID]): F[T] = {
    extractOfficeId(e) match {
      case Some(officeId) => OfficeNotFound(officeId)
      case None =>
        new RuntimeException(
          s"One of the offices was not found, but couldn't determine which one [ids: ${managedOfficeIds.mkString(", ")}]"
        )
    }
  }.raiseError

  private def managedOfficeIdsFromDomain(account: Account) =
    account match {
      case superAdmin: SuperAdminAccount       => superAdmin.managedOfficeIds
      case officeManager: OfficeManagerAccount => officeManager.managedOfficeIds
      case _                                   => Nil
    }

  private lazy val insertAccountSql: Command[Account] =
    sql"""
      INSERT INTO account
      VALUES      ($accountEncoder)
    """.command

  /**
   * Extracts the officeId from the exception detail.
   */
  private def extractOfficeId(exception: PostgresErrorException) =
    exception.detail match {
      case Some(officeIdViolationDetailRegex(officeIdString)) =>
        Either
          .catchOnly[IllegalArgumentException](UUID.fromString(officeIdString))
          .toOption
      case _ => None
    }

  private lazy val officeIdViolationDetailRegex =
    """Key \(office_id\)=\((?<officeIdValue>.+?)\) is not present in table "office"\.""".r

  def read(accountId: UUID): F[Account] =
    session.use { session =>
      for {
        sql <- session.prepare(readAccountSql)
        account <- OptionT(sql.option(accountId)).getOrRaise(AccountNotFound(accountId))
      } yield account
    }

  private lazy val readAccountSql: Query[UUID, Account] =
    sql"""
      SELECT id, first_name, last_name, email, is_archived, type, assigned_office_id, managed_office_ids
      FROM   account
      LEFT JOIN (
        SELECT account_id, ARRAY_AGG(office_id) AS managed_office_ids
        FROM   account_managed_office
        WHERE  account_id = $uuid
        GROUP BY account_id
      ) AS office ON account.id = office.account_id
      WHERE id = $uuid
    """
      .query(accountDecoder)
      .contramap(id => id *: id *: EmptyTuple)

  def updateAssignedOffice(accountId: UUID, officeId: Option[UUID]): F[Account] =
    session.use { session =>
      for {
        sql <- session.prepare(updateAssignedOfficeSql)
        _ <- sql
          .execute(officeId *: accountId *: EmptyTuple)
          .recoverWith {
            case SqlState.ForeignKeyViolation(e) if e.constraintName.contains("account_assigned_office_id_fkey") =>
              OfficeNotFound(officeId.get).raiseError
          }
          .ensureOr {
            case Completion.Update(0) => AccountNotFound(accountId)
            case completion           => new RuntimeException(s"Expected 1 updated account, but got: $completion")
          }(_ == Completion.Update(1))
        account <- read(accountId)
      } yield account
    }

  private lazy val updateAssignedOfficeSql: Command[Option[UUID] *: UUID *: EmptyTuple] =
    sql"""
      UPDATE account
      SET    assigned_office_id = ${uuid.opt}
      WHERE id = $uuid
    """.command

  def updateManagedOffices(accountId: UUID, managedOfficeIds: List[UUID]): F[Account] = {
    val accountIdToOfficeId = managedOfficeIds.map(accountId -> _)
    session.use { session =>
      for {
        delete <- session.prepare(deleteManagedOfficesSql)
        insert <- session.prepareIf(accountIdToOfficeId.nonEmpty)(insertManagedOfficesSql(accountIdToOfficeId))
        _ <- session.transaction
          .use { _ =>
            delete.execute(accountId) *>
              insert.execute(accountIdToOfficeId)
          }
          .recoverWith {
            case SqlState.ForeignKeyViolation(e)
                if e.constraintName.contains("account_managed_office_account_id_fkey") =>
              AccountNotFound(accountId).raiseError
            case SqlState.ForeignKeyViolation(e)
                if e.constraintName.contains("account_managed_office_office_id_fkey") =>
              recoverOnManagedOfficeNotFound(e, managedOfficeIds)
          }
        account <- read(accountId)
      } yield account
    }
  }

  private lazy val deleteManagedOfficesSql: Command[UUID] =
    sql"""
      DELETE FROM account_managed_office
      WHERE account_id = $uuid
    """.command

  private def insertManagedOfficesSql(accountIdToOfficeId: List[(UUID, UUID)]): Command[accountIdToOfficeId.type] = {
    val encoder = (uuid ~ uuid).values.list(accountIdToOfficeId)
    sql"""
      INSERT INTO account_managed_office
      VALUES      $encoder
    """.command
  }

  def updateRole(accountId: UUID, role: Role): F[Account] = {
    session.use { session =>
      for {
        updateRole <- session.prepare(updateRoleSql)
        removeManagedOffices <- session.prepareIf(role == Role.User)(deleteManagedOfficesSql)
        _ <- session.transaction.use { _ =>
          updateRole
            .execute(PostgresAccountType.fromDomain(role) *: accountId *: EmptyTuple)
            .ensureOr {
              case Completion.Update(0) => AccountNotFound(accountId)
              case completion           => new RuntimeException(s"Expected 1 updated account, but got: $completion")
            }(_ == Completion.Update(1)) *>
            removeManagedOffices.execute(accountId)
        }
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

  private lazy val accountEncoder: Encoder[Account] =
    (
      uuid *: // id
        varchar *: // first_name
        varchar *: // last_name
        varchar *: // email
        bool *: // is_archived
        PostgresAccountType.accountTypeCodec *: // type
        uuid.opt // assigned_office_id
    ).contramap { account =>
      account.id *:
        account.firstName *:
        account.lastName *:
        account.email *:
        account.isArchived *:
        PostgresAccountType.fromDomain(account.role) *:
        account.assignedOfficeId *:
        EmptyTuple
    }

  @nowarn("msg=match may not be exhaustive")
  private lazy val accountDecoder: Decoder[Account] =
    (
      uuid *: // id
        varchar *: // first_name
        varchar *: // last_name
        varchar *: // email
        bool *: // is_archived
        PostgresAccountType.accountTypeCodec *: // type
        uuid.opt *: // assigned_office_id
        _uuid.opt // managed_office_ids
    ).map {
      case id *: firstName *: lastName *: email *: isArchived *: accountType *: assignedOfficeId *: managedOfficeIds *: EmptyTuple =>
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
              assignedOfficeId = assignedOfficeId,
              managedOfficeIds = managedOfficeIds.getOrElse(Nil)
            )
          case PostgresAccountType.SuperAdmin =>
            SuperAdminAccount(
              id = id,
              firstName = firstName,
              lastName = lastName,
              email = email,
              isArchived = isArchived,
              assignedOfficeId = assignedOfficeId,
              managedOfficeIds = managedOfficeIds.getOrElse(Nil)
            )
        }
    }
}
