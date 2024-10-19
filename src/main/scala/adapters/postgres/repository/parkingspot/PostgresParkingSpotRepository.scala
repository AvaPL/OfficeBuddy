package io.github.avapl
package adapters.postgres.repository.parkingspot

import cats.data.OptionT
import cats.effect.kernel.MonadCancelThrow
import cats.effect.kernel.Resource
import cats.syntax.all._
import domain.model.error.office.OfficeNotFound
import domain.model.error.parkingspot.DuplicateParkingSpotNameForOffice
import domain.model.error.parkingspot.ParkingSpotNotFound
import domain.model.parkingspot.ParkingSpot
import domain.model.parkingspot.UpdateParkingSpot
import domain.repository.parkingspot.ParkingSpotRepository
import java.util.UUID
import scala.annotation.nowarn
import skunk._
import skunk.codec.all._
import skunk.data.Arr
import skunk.data.Completion
import skunk.exception.PostgresErrorException
import skunk.implicits._

class PostgresParkingSpotRepository[F[_]: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends ParkingSpotRepository[F] {

  import PostgresParkingSpotRepository._

  override def create(parkingSpot: ParkingSpot): F[ParkingSpot] =
    session.use { session =>
      for {
        sql <- session.prepare(createSql)
        _ <- sql
          .execute(parkingSpot)
          .recoverWith {
            case SqlState.ForeignKeyViolation(e) if e.constraintName.contains("parking_spot_office_id_fkey") =>
              OfficeNotFound(parkingSpot.officeId).raiseError
            case SqlState.UniqueViolation(e) if e.constraintName.contains("parking_spot_name_office_id_key") =>
              DuplicateParkingSpotNameForOffice(parkingSpot.name.some, parkingSpot.officeId.some).raiseError
          }
      } yield parkingSpot
    }

  private lazy val createSql: Command[ParkingSpot] =
    sql"""
      INSERT INTO parking_spot
      VALUES      ($parkingSpotEncoder)
    """.command

  override def read(parkingSpotId: UUID): F[ParkingSpot] =
    session.use { session =>
      for {
        sql <- session.prepare(readSql)
        parkingSpot <- OptionT(sql.option(parkingSpotId)).getOrRaise(ParkingSpotNotFound(parkingSpotId))
      } yield parkingSpot
    }

  private lazy val readSql: Query[UUID, ParkingSpot] =
    sql"""
      SELECT *
      FROM   parking_spot
      WHERE  id = $uuid
    """.query(parkingSpotDecoder)

  override def update(parkingSpotId: UUID, updateParkingSpot: UpdateParkingSpot): F[ParkingSpot] =
    for {
      _ <- updateOptionally(parkingSpotId, updateParkingSpot)
      parkingSpot <- read(parkingSpotId)
    } yield parkingSpot

  private def updateOptionally(parkingSpotId: UUID, updateParkingSpot: UpdateParkingSpot) =
    updateSql(parkingSpotId, updateParkingSpot) match {
      case Some(appliedFragment) =>
        session.use { session =>
          for {
            sql <- session.prepare(appliedFragment.fragment.command)
            _ <- sql
              .execute(appliedFragment.argument)
              .ensureOr {
                case Completion.Update(0) => ParkingSpotNotFound(parkingSpotId)
                case completion => new RuntimeException(s"Expected 1 updated parking spot, but got: $completion")
              }(_ == Completion.Update(1))
              .recoverWith {
                case SqlState.ForeignKeyViolation(e) if e.constraintName.contains("parking_spot_office_id_fkey") =>
                  OfficeNotFound(updateParkingSpot.officeId.get).raiseError
                case SqlState.UniqueViolation(e) if e.constraintName.contains("parking_spot_name_office_id_key") =>
                  val (name, officeId) = extractNameAndOfficeId(updateParkingSpot.name, updateParkingSpot.officeId, e)
                  DuplicateParkingSpotNameForOffice(name, officeId).raiseError
              }
          } yield ()
        }
      case None => // Nothing to update
        ().pure[F]
    }

  private def updateSql(parkingSpotId: UUID, update: UpdateParkingSpot): Option[AppliedFragment] = {
    val setName = sql"name = $varchar"
    val setIsAvailable = sql"is_available = $bool"
    val setNotes = sql"notes = ${_varchar}"
    val setIsHandicapped = sql"is_handicapped = $bool"
    val setIsUnderground = sql"is_underground = $bool"
    val setOfficeId = sql"office_id = $uuid"

    val appliedSets = List(
      update.name.map(setName),
      update.isAvailable.map(setIsAvailable),
      update.notes.map(Arr(_: _*)).map(setNotes),
      update.isHandicapped.map(setIsHandicapped),
      update.isUnderground.map(setIsUnderground),
      update.officeId.map(setOfficeId)
    ).flatten

    Option.when(appliedSets.nonEmpty) {
      appliedSets.foldSmash(
        sql"UPDATE parking_spot SET ".apply(Void),
        void", ",
        sql" WHERE id = $uuid".apply(parkingSpotId)
      )
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

  override def archive(parkingSpotId: UUID): F[Unit] =
    session.use { session =>
      session
        .prepare(archiveSql)
        .flatMap { sql =>
          sql.execute(parkingSpotId)
        }
        .void
    }

  private lazy val archiveSql: Command[UUID] =
    sql"""
      UPDATE parking_spot
      SET    is_archived = 'yes'
      WHERE id = $uuid
    """.command
}

object PostgresParkingSpotRepository {

  private lazy val parkingSpotEncoder: Encoder[ParkingSpot] =
    (
      uuid *: // id
        varchar *: // name
        bool *: // is_available
        _varchar *: // notes
        bool *: // is_handicapped
        bool *: // is_underground
        uuid *: // office_id
        bool // is_archived
    ).contramap { parkingSpot =>
      parkingSpot.id *:
        parkingSpot.name *:
        parkingSpot.isAvailable *:
        Arr(parkingSpot.notes: _*) *:
        parkingSpot.isHandicapped *:
        parkingSpot.isUnderground *:
        parkingSpot.officeId *:
        parkingSpot.isArchived *:
        EmptyTuple
    }

  @nowarn("msg=match may not be exhaustive")
  private lazy val parkingSpotDecoder: Decoder[ParkingSpot] =
    (
      uuid *: // id
        varchar *: // name
        bool *: // is_available
        _varchar *: // notes
        bool *: // is_handicapped
        bool *: // is_underground
        uuid *: // office_id
        bool // is_archived
    ).map {
      case id *: name *: isAvailable *: notes *: isHandicapped *: isUnderground *: officeId *: isArchived *: EmptyTuple =>
        ParkingSpot(id, name, isAvailable, notes.flattenTo(List), isHandicapped, isUnderground, officeId, isArchived)
    }
}
