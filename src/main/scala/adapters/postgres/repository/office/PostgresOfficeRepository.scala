package io.github.avapl
package adapters.postgres.repository.office

import cats.effect.Resource
import cats.effect.kernel.MonadCancelThrow
import cats.syntax.all._
import domain.model.office.Address
import domain.model.office.Office
import domain.repository.office.OfficeRepository
import java.util.UUID
import skunk._
import skunk.codec.all._
import skunk.implicits._

// TODO: Add integration tests
class PostgresOfficeRepository[F[_]: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends OfficeRepository[F] {

  import PostgresOfficeRepository._

  override def create(office: Office): F[UUID] = ???

  override def read(officeId: UUID): F[Office] =
    session.use { session =>
      session
        .prepare(readSql)
        .flatMap { sql =>
          sql.unique(officeId)
        }
    }

  private lazy val readSql: Query[UUID, Office] =
    sql"""
      SELECT id, name, notes, address_line_1, address_line_2, postal_code, city, country
      FROM   office
      WHERE  id = $uuid
    """.query(officeDecoder)

  override def update(office: Office): F[Unit] = ???

  override def delete(officeId: UUID): F[Unit] = ???
}

object PostgresOfficeRepository {

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
