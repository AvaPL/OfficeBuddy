package io.github.avapl
package adapters.postgres.repository.office.view

import cats.effect.Concurrent
import cats.effect.Resource
import cats.effect.kernel.MonadCancelThrow
import cats.syntax.all._
import domain.model.office.view.AddressView
import domain.model.office.view.OfficeListView
import domain.model.office.view.OfficeView
import domain.repository.office.view.OfficeViewRepository
import domain.model.view.Pagination
import scala.annotation.nowarn
import skunk._
import skunk.Query
import skunk.Session
import skunk.codec.all._
import skunk.implicits._

class PostgresOfficeViewRepository[F[_]: Concurrent: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends OfficeViewRepository[F] {

  import PostgresOfficeViewRepository._

  override def listOffices(limit: Int, offset: Int): F[OfficeListView] =
    session.use { session =>
      for {
        sql <- session.prepare(listOfficesSql)
        chunkSize = limit + 1 // Take one more element to determine if there are more elements to fetch
        officesViews <- sql.stream((chunkSize, offset), chunkSize).compile.toList
      } yield {
        val hasMoreResults = officesViews.size > limit
        val pagination = Pagination(limit, offset, hasMoreResults)
        OfficeListView(
          offices = officesViews.take(limit),
          pagination = pagination
        )
      }
    }
}

object PostgresOfficeViewRepository {

  // TODO: Populate counter values
  private lazy val listOfficesSql: Query[Int *: Int *: EmptyTuple, OfficeView] =
    sql"""
      SELECT   *, 0, 0, 0, 0, 0
      FROM     office
      WHERE    is_archived = 'no'
      ORDER BY name
      LIMIT    $int4
      OFFSET   $int4
    """.query(officeViewDecoder)

  @nowarn("msg=match may not be exhaustive")
  private lazy val officeViewDecoder: Decoder[OfficeView] =
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
        int4 *: // assigned_accounts_count
        int4 *: // desks_count
        int4 *: // parking_spots_count
        int4 *: // rooms_count
        int4 // active_reservations_count
    ).map {
      case id *: name *: notes *: addressLine1 *: addressLine2 *: postalCode *: city *: country *: _ *: assignedAccountsCount *: desksCount *: parkingSpotsCount *: roomsCount *: activeReservationsCount *: EmptyTuple =>
        val address = AddressView(addressLine1, addressLine2, postalCode, city, country)
        OfficeView(
          id,
          name,
          notes.flattenTo(List),
          address,
          officeManagers = Nil, // TODO: Return office managers
          assignedAccountsCount,
          desksCount,
          parkingSpotsCount,
          roomsCount,
          activeReservationsCount
        )
    }
}
