package io.github.avapl
package adapters.postgres.repository.desk

import cats.data.OptionT
import cats.effect.kernel.MonadCancelThrow
import cats.effect.kernel.Resource
import cats.syntax.all._
import domain.model.desk.Desk
import domain.model.desk.UpdateDesk
import domain.model.error.desk.DeskNotFound
import domain.model.error.desk.DuplicateDeskNameForOffice
import domain.model.error.office.OfficeNotFound
import domain.repository.desk.DeskRepository
import java.util.UUID
import scala.annotation.nowarn
import skunk._
import skunk.codec.all._
import skunk.data.Arr
import skunk.data.Completion
import skunk.exception.PostgresErrorException
import skunk.implicits._

class PostgresDeskRepository[F[_]: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends DeskRepository[F] {

  import PostgresDeskRepository._

  override def create(desk: Desk): F[Desk] =
    session.use { session =>
      for {
        sql <- session.prepare(createSql)
        _ <- sql
          .execute(desk)
          .recoverWith {
            case SqlState.ForeignKeyViolation(e) if e.constraintName.contains("desk_office_id_fkey") =>
              OfficeNotFound(desk.officeId).raiseError
            case SqlState.UniqueViolation(e) if e.constraintName.contains("desk_name_office_id_key") =>
              DuplicateDeskNameForOffice(desk.name.some, desk.officeId.some).raiseError
          }
      } yield desk
    }

  private lazy val createSql: Command[Desk] =
    sql"""
      INSERT INTO desk
      VALUES      ($deskEncoder)
    """.command

  override def read(deskId: UUID): F[Desk] =
    session.use { session =>
      for {
        sql <- session.prepare(readSql)
        desk <- OptionT(sql.option(deskId)).getOrRaise(DeskNotFound(deskId))
      } yield desk
    }

  private lazy val readSql: Query[UUID, Desk] =
    sql"""
      SELECT *   
      FROM   desk
      WHERE  id = $uuid
    """.query(deskDecoder)

  override def update(deskId: UUID, updateDesk: UpdateDesk): F[Desk] =
    for {
      _ <- updateOptionally(deskId, updateDesk)
      desk <- read(deskId)
    } yield desk

  private def updateOptionally(deskId: UUID, updateDesk: UpdateDesk) =
    updateSql(deskId, updateDesk) match {
      case Some(appliedFragment) =>
        session.use { session =>
          for {
            sql <- session.prepare(appliedFragment.fragment.command)
            _ <- sql
              .execute(appliedFragment.argument)
              .ensureOr {
                case Completion.Update(0) => DeskNotFound(deskId)
                case completion           => new RuntimeException(s"Expected 1 updated desk, but got: $completion")
              }(_ == Completion.Update(1))
              .recoverWith {
                case SqlState.ForeignKeyViolation(e) if e.constraintName.contains("desk_office_id_fkey") =>
                  OfficeNotFound(updateDesk.officeId.get).raiseError
                case SqlState.UniqueViolation(e) if e.constraintName.contains("desk_name_office_id_key") =>
                  val (name, officeId) = extractNameAndOfficeId(updateDesk.name, updateDesk.officeId, e)
                  DuplicateDeskNameForOffice(name, officeId).raiseError
              }
          } yield ()
        }
      case None => // Nothing to update
        ().pure[F]
    }

  private def updateSql(deskId: UUID, update: UpdateDesk): Option[AppliedFragment] = {
    val setName = sql"name = $varchar"
    val setIsAvailable = sql"is_available = $bool"
    val setNotes = sql"notes = ${_varchar}"
    val setIsStanding = sql"is_standing = $bool"
    val setMonitorsCount = sql"monitors_count = $int2"
    val setHasPhone = sql"has_phone = $bool"
    val setOfficeId = sql"office_id = $uuid"

    val appliedSets = List(
      update.name.map(setName),
      update.isAvailable.map(setIsAvailable),
      update.notes.map(Arr(_: _*)).map(setNotes),
      update.isStanding.map(setIsStanding),
      update.monitorsCount.map(setMonitorsCount),
      update.hasPhone.map(setHasPhone),
      update.officeId.map(setOfficeId)
    ).flatten

    Option.when(appliedSets.nonEmpty) {
      appliedSets.foldSmash(sql"UPDATE desk SET ".apply(Void), void", ", sql" WHERE id = $uuid".apply(deskId))
    }
  }

  private def extractNameAndOfficeId(
    updateName: Option[String],
    updateOfficeId: Option[UUID],
    exception: PostgresErrorException
  ): (Option[String], Option[UUID]) = {
    val (exceptionDetailName, exceptionDetailOfficeId) = exception.detail match {
      case Some(nameOfficeIdViolationDetailRegex(name, officeIdString)) =>
        val officeId = Either
          .catchOnly[IllegalArgumentException](UUID.fromString(officeIdString))
          .toOption
        (Some(name), officeId)
      case _ => (None, None)
    }
    (updateName.orElse(exceptionDetailName), updateOfficeId.orElse(exceptionDetailOfficeId))
  }

  private lazy val nameOfficeIdViolationDetailRegex =
    """Key \(name, office_id\)=\((?<nameValue>.+), (?<officeIdValue>.+?)\) already exists\.""".r

  override def archive(deskId: UUID): F[Unit] =
    session.use { session =>
      session
        .prepare(archiveSql)
        .flatMap { sql =>
          sql.execute(deskId)
        }
        .void
    }

  private lazy val archiveSql: Command[UUID] =
    sql"""
      UPDATE desk
      SET    is_archived = 'yes'
      WHERE id = $uuid
    """.command
}

object PostgresDeskRepository {

  private lazy val deskEncoder: Encoder[Desk] =
    (
      uuid *: // id
        varchar *: // name
        bool *: // is_available
        _varchar *: // notes
        bool *: // is_standing
        int2 *: // monitors_count
        bool *: // has_phone
        uuid *: // office_id
        bool // is_archived
    ).contramap { desk =>
      desk.id *:
        desk.name *:
        desk.isAvailable *:
        Arr(desk.notes: _*) *:
        desk.isStanding *:
        desk.monitorsCount *:
        desk.hasPhone *:
        desk.officeId *:
        desk.isArchived *:
        EmptyTuple
    }

  @nowarn("msg=match may not be exhaustive")
  private lazy val deskDecoder: Decoder[Desk] =
    (
      uuid *: // id
        varchar *: // name
        bool *: // is_available
        _varchar *: // notes
        bool *: // is_standing
        int2 *: // monitors_count
        bool *: // has_phone
        uuid *: // office_id
        bool // is_archived
    ).map {
      case id *: name *: isAvailable *: notes *: isStanding *: monitorsCount *: hasPhone *: officeId *: isArchived *: EmptyTuple =>
        Desk(id, name, isAvailable, notes.flattenTo(List), isStanding, monitorsCount, hasPhone, officeId, isArchived)
    }
}
