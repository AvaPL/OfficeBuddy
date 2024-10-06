package io.github.avapl
package adapters.postgres.repository.office

import adapters.postgres.repository._uuid
import adapters.postgres.repository.SessionOps
import cats.data.OptionT
import cats.effect.Resource
import cats.effect.kernel.MonadCancelThrow
import cats.syntax.all._
import domain.model.error.account.AccountNotFound
import domain.model.error.office.DuplicateOfficeName
import domain.model.error.office.OfficeNotFound
import domain.model.office.Address
import domain.model.office.Office
import domain.model.office.UpdateOffice
import domain.repository.office.OfficeRepository
import java.util.UUID
import scala.annotation.nowarn
import skunk._
import skunk.codec.all._
import skunk.data.Arr
import skunk.data.Completion
import skunk.exception.PostgresErrorException
import skunk.implicits._

class PostgresOfficeRepository[F[_]: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends OfficeRepository[F] {

  import PostgresOfficeRepository._

  override def create(office: Office): F[Office] =
    session.use { session =>
      for {
        sql <- session.prepare(createSql)
        _ <- sql
          .execute(office)
          .recoverWith {
            case SqlState.UniqueViolation(e) if e.constraintName.contains("office_name_key") =>
              DuplicateOfficeName(office.name).raiseError
          }
      } yield office
    }

  private lazy val createSql: Command[Office] =
    sql"""
      INSERT INTO office
      VALUES      ($officeEncoder)
    """.command

  override def read(officeId: UUID): F[Office] =
    session.use { session =>
      for {
        sql <- session.prepare(readSql)
        office <- OptionT(sql.option(officeId)).getOrRaise(OfficeNotFound(officeId))
      } yield office
    }

  private lazy val readSql: Query[UUID, Office] =
    sql"""
      SELECT id, name, notes, address_line_1, address_line_2, postal_code, city, country, is_archived, office_manager_ids
      FROM   office
      LEFT JOIN (
        SELECT office_id, ARRAY_AGG(account_id) AS office_manager_ids
        FROM   account_managed_office
        WHERE  office_id = $uuid
        GROUP BY office_id
      ) AS account ON office.id = account.office_id
      WHERE id = $uuid
    """
      .query(officeDecoder)
      .contramap(id => id *: id *: EmptyTuple)

  override def update(officeId: UUID, updateOffice: UpdateOffice): F[Office] =
    for {
      _ <- updateOptionally(officeId, updateOffice)
      office <- read(officeId)
    } yield office

  private def updateOptionally(officeId: UUID, updateOffice: UpdateOffice) =
    updateSql(officeId, updateOffice) match {
      case Some(appliedFragment) =>
        session.use { session =>
          for {
            sql <- session.prepare(appliedFragment.fragment.command)
            _ <- sql
              .execute(appliedFragment.argument)
              .ensureOr {
                case Completion.Update(0) => OfficeNotFound(officeId)
                case completion           => new RuntimeException(s"Expected 1 updated office, but got: $completion")
              }(_ == Completion.Update(1))
              .recoverWith {
                case SqlState.UniqueViolation(e)
                    if e.constraintName.contains("office_name_key") && updateOffice.name.isDefined =>
                  DuplicateOfficeName(updateOffice.name.get).raiseError
              }
          } yield ()
        }
      case None => // Nothing to update
        ().pure[F]
    }

  private def updateSql(officeId: UUID, update: UpdateOffice): Option[AppliedFragment] = {
    val setName = sql"name = $varchar"
    val setNotes = sql"notes = ${_varchar}"
    val setAddressLine1 = sql"address_line_1 = $varchar"
    val setAddressLine2 = sql"address_line_2 = $varchar"
    val setPostalCode = sql"postal_code = $varchar"
    val setCity = sql"city = $varchar"
    val setCountry = sql"country = $varchar"

    val appliedSets = List(
      update.name.map(setName),
      update.notes.map(Arr(_: _*)).map(setNotes),
      update.address.addressLine1.map(setAddressLine1),
      update.address.addressLine2.map(setAddressLine2),
      update.address.postalCode.map(setPostalCode),
      update.address.city.map(setCity),
      update.address.country.map(setCountry)
    ).flatten

    Option.when(appliedSets.nonEmpty) {
      appliedSets.foldSmash(sql"UPDATE office SET ".apply(Void), void", ", sql" WHERE id = $uuid".apply(officeId))
    }
  }

  // TODO: Ensure that officeManagerIds contains only office managers or admins (maybe via a DB constraint?)
  def updateOfficeManagers(officeId: UUID, officeManagerIds: List[UUID]): F[Office] = {
    val accountIdToOfficeId = officeManagerIds.map(_ -> officeId)
    session.use { session =>
      for {
        delete <- session.prepare(deleteManagedOfficesSql)
        insert <- session.prepareIf(accountIdToOfficeId.nonEmpty)(insertOfficeManagersSql(accountIdToOfficeId))
        _ <- session.transaction // TODO: Transactions should be handled on the service level, not repository level
          .use { _ =>
            delete.execute(officeId) *>
              insert.execute(accountIdToOfficeId)
          }
          .recoverWith {
            case SqlState.ForeignKeyViolation(e)
                if e.constraintName.contains("account_managed_office_office_id_fkey") =>
              OfficeNotFound(officeId).raiseError
            case SqlState.ForeignKeyViolation(e)
                if e.constraintName.contains("account_managed_office_account_id_fkey") =>
              recoverOnAccountNotFound(e, officeManagerIds)
          }
        office <- read(officeId)
      } yield office
    }
  }

  private lazy val deleteManagedOfficesSql: Command[UUID] =
    sql"""
      DELETE FROM account_managed_office
      WHERE office_id = $uuid
    """.command

  private def insertOfficeManagersSql(accountIdToOfficeId: List[(UUID, UUID)]): Command[accountIdToOfficeId.type] = {
    val encoder = (uuid ~ uuid).values.list(accountIdToOfficeId)
    sql"""
      INSERT INTO account_managed_office
      VALUES      $encoder
    """.command
  }

  private def recoverOnAccountNotFound[T](e: PostgresErrorException, accountIds: List[UUID]): F[T] = {
    extractAccountId(e) match {
      case Some(accountId) => AccountNotFound(accountId)
      case None =>
        new RuntimeException(
          s"One of the accounts was not found, but couldn't determine which one [ids: ${accountIds.mkString(", ")}]"
        )
    }
  }.raiseError

  /**
   * Extracts the accountId from the exception detail.
   */
  private def extractAccountId(exception: PostgresErrorException) =
    exception.detail match {
      case Some(accountIdViolationDetailRegex(accountIdString)) =>
        Either
          .catchOnly[IllegalArgumentException](UUID.fromString(accountIdString))
          .toOption
      case _ => None
    }

  private lazy val accountIdViolationDetailRegex =
    """Key \(account_id\)=\((?<accountIdValue>.+?)\) is not present in table "account"\.""".r

  override def archive(officeId: UUID): F[Unit] =
    session.use { session =>
      session
        .prepare(archiveSql)
        .flatMap { sql =>
          sql.execute(officeId)
        }
        .void
    }

  private lazy val archiveSql: Command[UUID] =
    sql"""
      UPDATE office
      SET    is_archived = 'yes'
      WHERE id = $uuid
    """.command
}

object PostgresOfficeRepository {

  private lazy val officeEncoder: Encoder[Office] =
    (
      uuid *: // id
        varchar *: // name
        _varchar *: // notes
        varchar *: // address_line_1
        varchar *: // address_line_2
        varchar *: // postal_code
        varchar *: // city
        varchar *: // country
        bool // is_archived
    ).contramap { office =>
      office.id *:
        office.name *:
        Arr(office.notes: _*) *:
        office.address.addressLine1 *:
        office.address.addressLine2 *:
        office.address.postalCode *:
        office.address.city *:
        office.address.country *:
        office.isArchived *:
        EmptyTuple
    }

  @nowarn("msg=match may not be exhaustive")
  private lazy val officeDecoder: Decoder[Office] =
    (
      uuid *: // id
        varchar *: // name
        _varchar *: // notes
        varchar *: // address_line_1
        varchar *: // address_line_2
        varchar *: // postal_code
        varchar *: // city
        varchar *: // country
        bool *: // is_archived
        _uuid.opt // office_manager_ids
    ).map {
      case id *: name *: notes *: addressLine1 *: addressLine2 *: postalCode *: city *: country *: isArchived *: officeManagerIds *: EmptyTuple =>
        val address = Address(addressLine1, addressLine2, postalCode, city, country)
        Office(id, name, notes.flattenTo(List), address, isArchived, officeManagerIds.getOrElse(Nil))
    }
}
