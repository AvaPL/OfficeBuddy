package io.github.avapl
package adapters.postgres.repository.office

import cats.data.OptionT
import cats.effect.Resource
import cats.effect.kernel.MonadCancelThrow
import cats.syntax.all._
import domain.model.office.Address
import domain.model.office.Office
import domain.repository.office.OfficeRepository
import domain.repository.office.OfficeRepository.DuplicateOfficeName
import domain.repository.office.OfficeRepository.OfficeNotFound
import java.util.UUID
import skunk._
import skunk.codec.all._
import skunk.data.Arr
import skunk.data.Completion
import skunk.implicits._

class PostgresOfficeRepository[F[_]: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends OfficeRepository[F] {

  import PostgresOfficeRepository._

  override def create(office: Office): F[Unit] =
    session.use { session =>
      session
        .prepare(createSql)
        .flatMap { sql =>
          sql
            .execute(office)
            .recoverWith {
              case SqlState.UniqueViolation(e) if e.constraintName.contains("office_name_key") =>
                DuplicateOfficeName(office.name).raiseError
            }
        }
        .void
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
      SELECT *
      FROM   office
      WHERE  id = $uuid
    """.query(officeDecoder)

  override def update(office: Office): F[Unit] =
    session.use { session =>
      for {
        sql <- session.prepare(updateSql)
        _ <- sql
          .execute(office)
          .ensureOr {
            case Completion.Update(0) => OfficeNotFound(office.id)
            case completion           => new RuntimeException(s"Expected 1 updated office, but got: $completion")
          }(_ == Completion.Update(1))
      } yield ()
    }

  private lazy val updateSql: Command[Office] =
    sql"""
      UPDATE office
      SET    name = $varchar,
             notes = ${_varchar},
             address_line_1 = $varchar,
             address_line_2 = $varchar,
             postal_code = $varchar,
             city = $varchar,
             country = $varchar
      WHERE  id = $uuid
    """.command
      .contramap { office =>
        office.name *:
          Arr(office.notes: _*) *:
          office.address.addressLine1 *:
          office.address.addressLine2 *:
          office.address.postalCode *:
          office.address.city *:
          office.address.country *:
          office.id *:
          EmptyTuple
      }

  override def delete(officeId: UUID): F[Unit] =
    session.use { session =>
      session
        .prepare(deleteSql)
        .flatMap { sql =>
          sql.execute(officeId)
        }
        .void
    }

  private lazy val deleteSql: Command[UUID] =
    sql"""
      DELETE FROM office
      WHERE       id = $uuid
    """.command
}

object PostgresOfficeRepository {

  private val officeEncoder: Encoder[Office] =
    (
      uuid *: // id
        varchar *: // name
        _varchar *: // notes
        varchar *: // address_line_1
        varchar *: // address_line_2
        varchar *: // postal_code
        varchar *: // city
        varchar // country
    ).contramap { office =>
      office.id *:
        office.name *:
        Arr(office.notes: _*) *:
        office.address.addressLine1 *:
        office.address.addressLine2 *:
        office.address.postalCode *:
        office.address.city *:
        office.address.country *:
        EmptyTuple
    }

  private val officeDecoder: Decoder[Office] =
    (
      uuid *: // id
        varchar *: // name
        _varchar *: // notes
        varchar *: // address_line_1
        varchar *: // address_line_2
        varchar *: // postal_code
        varchar *: // city
        varchar // country
    ).map {
      case id *: name *: notes *: addressLine1 *: addressLine2 *: postalCode *: city *: country *: EmptyTuple =>
        val address = Address(addressLine1, addressLine2, postalCode, city, country)
        Office(id, name, notes.flattenTo(List), address)
    }
}
