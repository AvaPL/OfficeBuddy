package io.github.avapl
package adapters.postgres.repository.account.view

import adapters.postgres.repository._uuid
import adapters.postgres.repository.account.PostgresAccountRepository.PostgresAccountType
import cats.effect.kernel.Concurrent
import cats.effect.kernel.MonadCancelThrow
import cats.effect.kernel.Resource
import cats.syntax.all._
import domain.model.account.Role
import domain.model.account.view.AccountListView
import domain.model.account.view.AccountView
import domain.model.view.Pagination
import domain.repository.account.view.AccountViewRepository
import java.util.UUID
import scala.annotation.nowarn
import skunk._
import skunk.Session
import skunk.codec.all._
import skunk.data.Arr
import skunk.data.Type
import skunk.implicits._

class PostgresAccountViewRepository[F[_]: Concurrent: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends AccountViewRepository[F] {

  import PostgresAccountViewRepository._

  override def listAccounts(
    textSearchQuery: Option[String],
    officeId: Option[UUID],
    roles: Option[List[Role]],
    limit: Int,
    offset: Int
  ): F[AccountListView] = {
    val accountTypes = roles.map(_.map(PostgresAccountType.fromDomain))
    val chunkSize = limit + 1 // Take one more element to determine if there are more elements to fetch
    val appliedFragment = listAccountsSql(textSearchQuery, officeId, accountTypes, chunkSize, offset)
    session.use { session =>
      for {
        sql <- session.prepare(appliedFragment.fragment.query(accountViewDecoder))
        accountsViews <- sql.stream(appliedFragment.argument, chunkSize).compile.toList
      } yield {
        val hasMoreResults = accountsViews.size > limit
        val pagination = Pagination(limit, offset, hasMoreResults)
        AccountListView(
          accounts = accountsViews.take(limit),
          pagination = pagination
        )
      }
    }
  }
}

object PostgresAccountViewRepository {

  private def listAccountsSql(
    textSearchQuery: Option[String],
    officeId: Option[UUID],
    accountTypes: Option[List[PostgresAccountType]],
    limit: Int,
    offset: Int
  ): AppliedFragment = {
    val select = sql"""
      SELECT   *
      FROM     account
      WHERE    is_archived = 'no'
    """

    val textSearchFilter = sql"""
      AND (
        (first_name || ' ' || last_name) ILIKE $varchar
        OR email ILIKE $varchar
      )
    """
    val officeIdFilter = sql"""
      AND (
        assigned_office_id = $uuid
        OR $uuid = ANY(managed_office_ids)
      )
    """
    val rolesFilter = sql"AND type = ANY(${_accountTypeCodec})"

    val appliedFilters = List(
      textSearchQuery.map(q => s"%$q%").map(q => textSearchFilter(q, q)),
      officeId.map(id => officeIdFilter(id, id)),
      accountTypes.map(rolesFilter)
    ).flatten.fold(AppliedFragment.empty)(_ |+| _)

    val orderByLimitOffset = sql"""
      ORDER BY first_name, last_name, email
      LIMIT    $int4
      OFFSET   $int4
    """

    select(Void) |+| appliedFilters |+| orderByLimitOffset(limit, offset)
  }

  private lazy val _accountTypeCodec: Codec[List[PostgresAccountType]] =
    Codec
      .array[PostgresAccountType](
        _.value,
        PostgresAccountType.withValueEither(_).left.map(_.getMessage),
        Type._varchar
      )
      .imap(_.flattenTo(List))(Arr(_: _*))

  @nowarn("msg=match may not be exhaustive")
  private lazy val accountViewDecoder: Decoder[AccountView] =
    (
      uuid *: // id
        varchar *: // first_name
        varchar *: // last_name
        varchar *: // email
        bool *: // is_archived
        PostgresAccountType.accountTypeCodec *: // type
        uuid.opt *: // assigned_office_id
        _uuid // managed_office_ids
    ).map {
      case id *: email *: firstName *: lastName *: _ *: accountType *: assignedOfficeId *: managedOfficeIds *: EmptyTuple =>
        AccountView(
          id = id,
          firstName = firstName,
          lastName = lastName,
          email = email,
          role = accountType.toDomain,
          assignedOfficeId = assignedOfficeId,
          managedOfficeIds = managedOfficeIds
        )
    }
}
