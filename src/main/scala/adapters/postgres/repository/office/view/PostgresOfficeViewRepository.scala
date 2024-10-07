package io.github.avapl
package adapters.postgres.repository.office.view

import cats.effect.Concurrent
import cats.effect.Resource
import cats.effect.kernel.MonadCancelThrow
import cats.syntax.all._
import domain.model.office.view.AddressView
import domain.model.office.view.OfficeListView
import domain.model.office.view.OfficeManagerView
import domain.model.office.view.OfficeView
import domain.model.view.Pagination
import domain.repository.office.view.OfficeViewRepository

import java.time.{LocalDate, LocalDateTime}
import java.util.UUID
import scala.annotation.nowarn
import skunk._
import skunk.Query
import skunk.Session
import skunk.codec.all._
import skunk.data.Type
import skunk.implicits._

class PostgresOfficeViewRepository[F[_]: Concurrent: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends OfficeViewRepository[F] {

  import PostgresOfficeViewRepository._

  override def listOffices(now: LocalDateTime, limit: Int, offset: Int): F[OfficeListView] =
    session.use { session =>
      for {
        sql <- session.prepare(listOfficesSql)
        chunkSize = limit + 1 // Take one more element to determine if there are more elements to fetch
        officesViews <- sql.stream((now, chunkSize, offset), chunkSize).compile.toList
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

  // TODO: Populate parking spots count
  // TODO: Populate rooms count
  private lazy val listOfficesSql: Query[LocalDateTime *: Int *: Int *: EmptyTuple, OfficeView] =
    sql"""
      SELECT id,
             name, 
             notes, 
             address_line_1, 
             address_line_2,
             postal_code,
             city,
             country,
             office_managers,
             COALESCE(assigned_accounts_count, 0),
             COALESCE(desks_count, 0),
             0, 
             0, 
             COALESCE(active_reservations_count, 0)
      FROM office

      -- Office managers
      LEFT JOIN (
        SELECT office_id, ARRAY_AGG((a.id, a.first_name, a.last_name, a.email)) AS office_managers
        FROM   account_managed_office amo
        LEFT JOIN account a ON amo.account_id = a.id
        WHERE a.is_archived = 'no'
        GROUP BY office_id
      ) AS om ON office.id = om.office_id

      -- Assigned accounts count
      LEFT JOIN (
        SELECT   assigned_office_id, COUNT(*)::int4 AS assigned_accounts_count
        FROM     account
        WHERE    is_archived = 'no'
          AND    assigned_office_id IS NOT NULL
        GROUP BY assigned_office_id
      ) AS aa ON office.id = aa.assigned_office_id
      
      -- Desks count
      LEFT JOIN (
        SELECT   office_id, COUNT(*)::int4 AS desks_count
        FROM     desk
        WHERE    is_archived = 'no'
        GROUP BY office_id
      ) AS d ON office.id = d.office_id
      
      -- Parking spots count
      -- To be implemented
      
      -- Rooms count
      -- To be implemented
      
      -- Active reservations count
      LEFT JOIN (
        SELECT   office_id, COUNT(*)::int4 AS active_reservations_count
        FROM     reservation
        LEFT JOIN desk ON reservation.desk_id = desk.id
        WHERE    $timestamp <= reserved_to
          AND    state IN ('Pending', 'Confirmed')
        GROUP BY office_id
      ) AS r ON office.id = r.office_id

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
        _officeManagerDecoder.opt *: // office_managers
        int4 *: // assigned_accounts_count
        int4 *: // desks_count
        int4 *: // parking_spots_count
        int4 *: // rooms_count
        int4 // active_reservations_count
    ).map {
      case id *: name *: notes *: addressLine1 *: addressLine2 *: postalCode *: city *: country *: officeManagers *: assignedAccountsCount *: desksCount *: parkingSpotsCount *: roomsCount *: activeReservationsCount *: EmptyTuple =>
        val address = AddressView(addressLine1, addressLine2, postalCode, city, country)
        OfficeView(
          id,
          name,
          notes.flattenTo(List),
          address,
          officeManagers.getOrElse(Nil),
          assignedAccountsCount,
          desksCount,
          parkingSpotsCount,
          roomsCount,
          activeReservationsCount
        )
    }

  private lazy val _officeManagerDecoder = {
    val officeViewRegex = "\\((?<id>.*?),(?<firstName>.*),(?<lastName>.*),(?<email>.*)\\)".r
    Codec
      .array[Option[OfficeManagerView]](
        encode = _ => ???,
        {
          case officeViewRegex(id, firstName, lastName, email) =>
            def unquote(string: String) = string.stripPrefix("\"").stripSuffix("\"")

            Either
              .catchOnly[IllegalArgumentException](UUID.fromString(id))
              .leftMap(_.getMessage)
              .map { id =>
                OfficeManagerView(
                  id = id,
                  firstName = unquote(firstName),
                  lastName = unquote(lastName),
                  email = unquote(email)
                ).some
              }
          case s => s"Invalid office view format: $s".asLeft
        },
        Type._record
      )
      .asDecoder
      .map(_.flattenTo(List).flatten)
  }
}
