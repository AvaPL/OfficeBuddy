package io.github.avapl
package adapters.postgres.repository.desk.view

import cats.effect.kernel.Concurrent
import cats.effect.kernel.MonadCancelThrow
import cats.effect.kernel.Resource
import cats.syntax.all._
import domain.model.desk.view.DeskListView
import domain.model.desk.view.DeskView
import domain.model.view.Pagination
import domain.repository.desk.view.DeskViewRepository
import java.util.UUID
import scala.annotation.nowarn
import skunk._
import skunk.codec.all._
import skunk.implicits._

class PostgresDeskViewRepository[F[_]: Concurrent: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends DeskViewRepository[F] {

  import PostgresDeskViewRepository._

  override def listDesks(officeId: UUID, limit: Int, offset: Int): F[DeskListView] =
    session.use { session =>
      for {
        sql <- session.prepare(listDesksSql)
        chunkSize = limit + 1 // Take one more element to determine if there are more elements to fetch
        deskViews <- sql.stream((officeId, chunkSize, offset), chunkSize).compile.toList
      } yield {
        val hasMoreResults = deskViews.size > limit
        val pagination = Pagination(limit, offset, hasMoreResults)
        DeskListView(
          desks = deskViews.take(limit),
          pagination = pagination
        )
      }
    }
}

object PostgresDeskViewRepository {

  private lazy val listDesksSql: Query[UUID *: Int *: Int *: EmptyTuple, DeskView] =
    sql"""
      SELECT   *
      FROM     desk
      WHERE    office_id = $uuid
        AND    is_archived = 'no'
      ORDER BY name
      LIMIT    $int4
      OFFSET   $int4
    """.query(deskViewDecoder)

  @nowarn("msg=match may not be exhaustive")
  private lazy val deskViewDecoder: Decoder[DeskView] =
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
      case id *: name *: isAvailable *: notes *: isStanding *: monitorsCount *: hasPhone *: _ *: _ *: EmptyTuple =>
        DeskView(id, name, isAvailable, notes.flattenTo(List), isStanding, monitorsCount, hasPhone)
    }
}
