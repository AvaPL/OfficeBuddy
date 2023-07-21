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
import skunk._
import skunk.codec.all._
import skunk.data.Arr
import skunk.data.Completion
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
              DuplicateDeskNameForOffice(desk.name, desk.officeId).raiseError
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
    session.use { session =>
      for {
        sql <- session.prepare(updateSql)
        _ <- sql
          .execute(deskId *: updateDesk *: EmptyTuple)
          .ensureOr {
            case Completion.Update(0) => DeskNotFound(deskId)
            case completion           => new RuntimeException(s"Expected 1 updated desk, but got: $completion")
          }(_ == Completion.Update(1))
          .recoverWith {
            case SqlState.ForeignKeyViolation(e) if e.constraintName.contains("desk_office_id_fkey") =>
              OfficeNotFound(updateDesk.officeId).raiseError
            case SqlState.UniqueViolation(e) if e.constraintName.contains("desk_name_office_id_key") =>
              DuplicateDeskNameForOffice(updateDesk.name, updateDesk.officeId).raiseError
          }
        desk <- read(deskId)
      } yield desk
    }

  private lazy val updateSql: Command[UUID *: UpdateDesk *: EmptyTuple] =
    sql"""
      UPDATE desk   
      SET    name = $varchar,
             is_available = $bool,
             notes = ${_varchar},
             is_standing = $bool,
             monitors_count = $int2,
             has_phone = $bool,
             office_id = $uuid
      WHERE id = $uuid
    """.command
      .contramap {
        case deskId *: updateDesk *: EmptyTuple =>
          updateDesk.name *:
            updateDesk.isAvailable *:
            Arr(updateDesk.notes: _*) *:
            updateDesk.isStanding *:
            updateDesk.monitorsCount *:
            updateDesk.hasPhone *:
            updateDesk.officeId *:
            deskId *:
            EmptyTuple
      }

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
        uuid // office_id
    ).contramap { desk =>
      desk.id *:
        desk.name *:
        desk.isAvailable *:
        Arr(desk.notes: _*) *:
        desk.isStanding *:
        desk.monitorsCount *:
        desk.hasPhone *:
        desk.officeId *:
        EmptyTuple
    }

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
