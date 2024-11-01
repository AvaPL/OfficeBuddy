package io.github.avapl
package adapters.http.reservation

import adapters.auth.repository.PublicKeyRepository
import adapters.auth.service.ClaimsExtractorService
import adapters.http.ApiError
import adapters.http.PaginationInput
import adapters.http.SecuredApiEndpoint
import adapters.http.model.view.ApiPagination
import adapters.http.reservation.model.ApiCreateDeskReservation
import adapters.http.reservation.model.ApiDeskReservation
import adapters.http.reservation.model.ApiReservationState
import adapters.http.reservation.model.view.ApiDeskReservationListView
import adapters.http.reservation.model.view.ApiDeskReservationView
import adapters.http.reservation.model.view.ApiDeskView
import adapters.http.reservation.model.view.ApiUserView
import cats.MonadThrow
import cats.data.EitherT
import cats.effect.Clock
import cats.syntax.all._
import domain.model.account.Role
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.User
import domain.model.error.desk.DeskNotFound
import domain.model.error.reservation._
import domain.model.error.user.UserNotFound
import domain.repository.reservation.view.DeskReservationViewRepository
import domain.service.reservation.DeskReservationService
import io.github.avapl.domain.model.reservation.DeskReservation
import io.github.avapl.domain.model.reservation.view.DeskReservationListView
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.model.CommaSeparated
import sttp.tapir.model.Delimited
import sttp.tapir.server.ServerEndpoint

class DeskReservationEndpoints[F[_]](
  override val reservationService: DeskReservationService[F],
  reservationViewRepository: DeskReservationViewRepository[F],
  override val publicKeyRepository: PublicKeyRepository[F],
  override val claimsExtractor: ClaimsExtractorService
)(
  override implicit val clock: Clock[F],
  override implicit val monadThrow: MonadThrow[F]
) extends ReservationEndpoints[F, DeskReservation]
  with PaginationInput {

  override protected lazy val reservationEntityPathName: String = "desk"

  override protected lazy val reservationEntityEndpoints: List[ServerEndpoint[Any, F]] =
    reserveDeskEndpoint ::
      readDeskReservationEndpoint ::
      deskReservationListViewEndpoint ::
      Nil

  private lazy val reserveDeskEndpoint =
    securedEndpoint(requiredRole = User).post
      .summary("Reserve a desk")
      .in(reservationEntityPathName)
      .in(
        jsonBody[ApiCreateDeskReservation]
          .example(apiCreateDeskReservationExample)
      )
      .out(
        statusCode(StatusCode.Created) and jsonBody[ApiDeskReservation]
          .description("Desk reservation created")
          .example(apiDeskReservationExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.BadRequest) and jsonBody[ApiError.BadRequest]
            .description("Desk or user with the given ID was not found")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.Conflict) and jsonBody[ApiError.Conflict]
            .description("Desk reservation overlaps with another reservation")
        )
      )
      .serverLogic { accessToken =>
        reserveDesk(
          requesterRoles = accessToken.roles,
          requesterAccountId = accessToken.accountId
        )
      }

  private def reserveDesk(requesterRoles: List[Role], requesterAccountId: UUID)(
    apiCreateDeskReservation: ApiCreateDeskReservation
  ): F[Either[ApiError, ApiDeskReservation]] = {
    val hasPermission =
      requesterAccountId == apiCreateDeskReservation.userId ||
        requesterRoles.exists(_.hasAccess(OfficeManager))
    if (hasPermission) reserveDesk(apiCreateDeskReservation)
    else ApiError.Forbidden.asLeft.pure[F].widen
  }

  private def reserveDesk(apiCreateDeskReservation: ApiCreateDeskReservation): F[Either[ApiError, ApiDeskReservation]] =
    reservationService
      .reserve(apiCreateDeskReservation.toDomain)
      .map(ApiDeskReservation.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case DeskNotFound(deskId) =>
          ApiError.BadRequest(s"Desk [id: $deskId] was not found").asLeft
        case UserNotFound(userId) =>
          ApiError.BadRequest(s"User [id: $userId] was not found").asLeft
        case OverlappingReservations =>
          ApiError.Conflict("There is an overlapping reservation, try another reservation date range").asLeft
      }

  private lazy val readDeskReservationEndpoint =
    securedEndpoint(requiredRole = User).get
      .summary("Find a desk reservation by ID")
      .in(reservationEntityPathName / path[UUID]("reservationId"))
      .out(
        jsonBody[ApiDeskReservation]
          .description("Found reservation")
          .example(apiDeskReservationExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Desk reservation with the given ID was not found")
        )
      )
      .serverLogic(_ => readDeskReservation)

  private def readDeskReservation(reservationId: UUID) =
    reservationService
      .readReservation(reservationId)
      .map(ApiDeskReservation.fromDomain)
      .map(_.asRight[ApiError])
      .recover(recoverOnNotFound)

  private lazy val deskReservationListViewEndpoint =
    securedEndpoint(requiredRole = User).get
      .summary("Desk reservations list view")
      .in(reservationEntityPathName / "view" / "list")
      .in(
        query[UUID]("office_id")
          .description("Desk's office ID")
          .example(officeIdExample)
      )
      .in(
        query[LocalDate]("reservation_from")
          .description("Reservation start date lower bound (inclusive)")
          .example(reservedFromExample)
      )
      .in(
        query[Option[CommaSeparated[ApiReservationState]]]("reservation_states")
          .description("List of allowed reservation states")
          .example(Some(reservationStatesExample))
      )
      .in(
        query[Option[UUID]]("user_id")
          .description("Reservation's assigned user ID")
          .example(Some(userIdExample))
      )
      .in(paginationLimitAndOffset)
      .out(
        jsonBody[ApiDeskReservationListView]
          .description("List of desk reservations")
          .example(apiDeskReservationListViewExample)
      )
      .serverLogic(_ => (deskReservationsListView _).tupled)

  private def deskReservationsListView(
    officeId: UUID,
    reservedFrom: LocalDate,
    reservationStates: Option[CommaSeparated[ApiReservationState]],
    userId: Option[UUID],
    limit: Int,
    offset: Int
  ): F[Either[ApiError, ApiDeskReservationListView]] = {
    val domainReservationStates = reservationStates.collect {
      case Delimited(apiReservationStates) if apiReservationStates.nonEmpty => apiReservationStates.map(_.toDomain)
    }
    reservationViewRepository
      .listDeskReservations(officeId, reservedFrom, domainReservationStates, userId, limit, offset)
      .map(ApiDeskReservationListView.fromDomain)
      .map(_.asRight[ApiError])
  }

  private lazy val apiDeskReservationExample = ApiDeskReservation(
    id = UUID.fromString("dcd64f97-a57a-4b57-9e63-dbe56187b557"),
    userId = UUID.fromString("0f0cdeb7-c6f0-4f1e-93a5-b3fd34506dc5"),
    createdAt = LocalDateTime.parse("2023-07-18T20:41:00"),
    reservedFromDate = LocalDate.parse("2023-07-19"),
    reservedToDate = LocalDate.parse("2023-07-20"),
    state = ApiReservationState.Pending,
    notes = "Please remove the duck from the desk, it scares me",
    deskId = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")
  )

  private lazy val apiCreateDeskReservationExample = ApiCreateDeskReservation(
    userId = UUID.fromString("0f0cdeb7-c6f0-4f1e-93a5-b3fd34506dc5"),
    reservedFrom = LocalDate.parse("2023-07-19"),
    reservedTo = LocalDate.parse("2023-07-20"),
    notes = "Please remove the duck from the desk, it scares me",
    deskId = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")
  )

  private lazy val officeIdExample = UUID.fromString("451aec8c-d7ec-4ff9-a091-cc6c944aefb1")
  private lazy val reservedFromExample = LocalDate.parse("2024-09-24")
  private lazy val reservationStatesExample: CommaSeparated[ApiReservationState] =
    Delimited(List(ApiReservationState.Pending, ApiReservationState.Confirmed))
  private lazy val userIdExample = UUID.fromString("8b7a9bdd-b729-4427-83c3-6eaee3c97171")

  private lazy val apiDeskReservationListViewExample = ApiDeskReservationListView(
    reservations = List(
      ApiDeskReservationView(
        id = UUID.fromString("dcd64f97-a57a-4b57-9e63-dbe56187b557"),
        reservedFromDate = LocalDate.parse("2024-09-24"),
        reservedToDate = LocalDate.parse("2024-09-27"),
        state = ApiReservationState.Confirmed,
        notes = "Please remove the duck from the desk, it scares me",
        user = ApiUserView(
          id = UUID.fromString("a28420c3-da66-4f87-bac3-a399bafa2756"),
          firstName = "John",
          lastName = "Doe",
          email = "john.doe@example.com"
        ),
        desk = ApiDeskView(
          id = UUID.fromString("2215a152-cc29-4086-b5a8-7aef2a96867b"),
          name = "102.1"
        )
      ),
      ApiDeskReservationView(
        id = UUID.fromString("4ccb446a-7e7a-4d6e-8e11-3736c8315ccd"),
        reservedFromDate = LocalDate.parse("2024-09-28"),
        reservedToDate = LocalDate.parse("2024-09-28"),
        state = ApiReservationState.Pending,
        notes = "I won't join lunch",
        user = ApiUserView(
          id = UUID.fromString("a28420c3-da66-4f87-bac3-a399bafa2756"),
          firstName = "John",
          lastName = "Doe",
          email = "john.doe@example.com"
        ),
        desk = ApiDeskView(
          id = UUID.fromString("2215a152-cc29-4086-b5a8-7aef2a96867b"),
          name = "102.1"
        )
      ),
      ApiDeskReservationView(
        id = UUID.fromString("71ba2b69-eb1f-4939-b06b-025487891812"),
        reservedFromDate = LocalDate.parse("2024-09-29"),
        reservedToDate = LocalDate.parse("2024-09-30"),
        state = ApiReservationState.Cancelled,
        notes = "",
        user = ApiUserView(
          id = UUID.fromString("8bb1e5d6-a74d-4cae-953b-5ec4e0d04cd4"),
          firstName = "Jane",
          lastName = "Smith",
          email = "jane.smith@example.com"
        ),
        desk = ApiDeskView(
          id = UUID.fromString("2bbc890f-d5d4-406d-978c-f233245a24ac"),
          name = "102.2"
        )
      )
    ),
    pagination = ApiPagination(
      limit = 3,
      offset = 0,
      hasMoreResults = true
    )
  )
}
