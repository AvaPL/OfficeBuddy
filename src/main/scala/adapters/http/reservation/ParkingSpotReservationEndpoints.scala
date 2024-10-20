package io.github.avapl
package adapters.http.reservation

import adapters.auth.repository.PublicKeyRepository
import adapters.auth.service.ClaimsExtractorService
import adapters.http.ApiError
import adapters.http.PaginationInput
import adapters.http.model.view.ApiPagination
import adapters.http.reservation.model.ApiCreateParkingSpotReservation
import adapters.http.reservation.model.ApiParkingSpotReservation
import adapters.http.reservation.model.ApiReservationState
import adapters.http.reservation.model.view.ApiParkingSpotReservationListView
import adapters.http.reservation.model.view.ApiParkingSpotReservationView
import adapters.http.reservation.model.view.ApiParkingSpotView
import adapters.http.reservation.model.view.ApiUserView
import cats.MonadThrow
import cats.effect.Clock
import cats.syntax.all._
import domain.model.account.Role
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.User
import domain.model.error.parkingspot.ParkingSpotNotFound
import domain.model.error.reservation._
import domain.model.error.user.UserNotFound
import domain.model.reservation.ParkingSpotReservation
import domain.repository.reservation.view.ParkingSpotReservationViewRepository
import domain.service.reservation.ParkingSpotReservationService
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.model.CommaSeparated
import sttp.tapir.model.Delimited
import sttp.tapir.server.ServerEndpoint

// TODO: Use
// TODO: Add unit tests
class ParkingSpotReservationEndpoints[F[_]](
  override val reservationService: ParkingSpotReservationService[F],
  reservationViewRepository: ParkingSpotReservationViewRepository[F],
  override val publicKeyRepository: PublicKeyRepository[F],
  override val claimsExtractor: ClaimsExtractorService
)(
  override implicit val clock: Clock[F],
  override implicit val monadThrow: MonadThrow[F]
) extends ReservationEndpoints[F, ParkingSpotReservation]
  with PaginationInput {

  override protected lazy val reservationEntityPathName: String = "parking"

  override protected lazy val reservationEntityEndpoints: List[ServerEndpoint[Any, F]] =
    reserveParkingSpotEndpoint ::
      readParkingSpotReservationEndpoint ::
      parkingSpotReservationListViewEndpoint ::
      Nil

  private lazy val reserveParkingSpotEndpoint =
    securedEndpoint(requiredRole = User).post
      .summary("Reserve a parking spot")
      .in(reservationEntityPathName)
      .in(
        jsonBody[ApiCreateParkingSpotReservation]
          .example(apiCreateParkingSpotReservationExample)
      )
      .out(
        statusCode(StatusCode.Created) and jsonBody[ApiParkingSpotReservation]
          .description("Parking spot reservation created")
          .example(apiParkingSpotReservationExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.BadRequest) and jsonBody[ApiError.BadRequest]
            .description("Parking spot or user with the given ID was not found")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.Conflict) and jsonBody[ApiError.Conflict]
            .description("Parking spot reservation overlaps with another reservation")
        )
      )
      .serverLogic { accessToken =>
        reserveParkingSpot(
          requesterRoles = accessToken.roles,
          requesterAccountId = accessToken.accountId
        )
      }

  private def reserveParkingSpot(requesterRoles: List[Role], requesterAccountId: UUID)(
    apiCreateParkingSpotReservation: ApiCreateParkingSpotReservation
  ): F[Either[ApiError, ApiParkingSpotReservation]] = {
    val hasPermission =
      requesterAccountId == apiCreateParkingSpotReservation.userId ||
        requesterRoles.exists(_.hasAccess(OfficeManager))
    if (hasPermission) reserveParkingSpot(apiCreateParkingSpotReservation)
    else ApiError.Forbidden.asLeft.pure[F].widen
  }

  private def reserveParkingSpot(
    apiCreateParkingSpotReservation: ApiCreateParkingSpotReservation
  ): F[Either[ApiError, ApiParkingSpotReservation]] =
    reservationService
      .reserve(apiCreateParkingSpotReservation.toDomain)
      .map(ApiParkingSpotReservation.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case ParkingSpotNotFound(parkingSpotId) =>
          ApiError.BadRequest(s"Parking spot [id: $parkingSpotId] was not found").asLeft
        case UserNotFound(userId) =>
          ApiError.BadRequest(s"User [id: $userId] was not found").asLeft
        case OverlappingReservations =>
          ApiError.Conflict("There is an overlapping reservation, try another reservation date range").asLeft
      }

  private lazy val readParkingSpotReservationEndpoint =
    securedEndpoint(requiredRole = User).get
      .summary("Find a parking spot reservation by ID")
      .in(reservationEntityPathName / path[UUID]("reservationId"))
      .out(
        jsonBody[ApiParkingSpotReservation]
          .description("Found reservation")
          .example(apiParkingSpotReservationExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Parking spot reservation with the given ID was not found")
        )
      )
      .serverLogic(_ => readParkingSpotReservation)

  private def readParkingSpotReservation(reservationId: UUID) =
    reservationService
      .readReservation(reservationId)
      .map(ApiParkingSpotReservation.fromDomain)
      .map(_.asRight[ApiError])
      .recover(recoverOnNotFound)

  private lazy val parkingSpotReservationListViewEndpoint =
    securedEndpoint(requiredRole = User).get
      .summary("Parking spot reservations list view")
      .in(reservationEntityPathName / "view" / "list")
      .in(
        query[UUID]("office_id")
          .description("Parking spot's office ID")
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
      .in(
        query[Option[String]]("plate_number")
          .description("Reservation's plate number")
          .example(Some("DSW 58100"))
      )
      .in(paginationLimitAndOffset)
      .out(
        jsonBody[ApiParkingSpotReservationListView]
          .description("List of parking spot reservations")
          .example(apiParkingSpotReservationListViewExample)
      )
      .serverLogic(_ => (parkingSpotReservationsListView _).tupled)

  private def parkingSpotReservationsListView(
    officeId: UUID,
    reservedFrom: LocalDate,
    reservationStates: Option[CommaSeparated[ApiReservationState]],
    userId: Option[UUID],
    plateNumber: Option[String],
    limit: Int,
    offset: Int
  ): F[Either[ApiError, ApiParkingSpotReservationListView]] = {
    val domainReservationStates = reservationStates.collect {
      case Delimited(apiReservationStates) if apiReservationStates.nonEmpty => apiReservationStates.map(_.toDomain)
    }
    reservationViewRepository
      .listParkingSpotReservations(officeId, reservedFrom, domainReservationStates, userId, plateNumber, limit, offset)
      .map(ApiParkingSpotReservationListView.fromDomain)
      .map(_.asRight[ApiError])
  }

  private lazy val apiParkingSpotReservationExample = ApiParkingSpotReservation(
    id = UUID.fromString("dcd64f97-a57a-4b57-9e63-dbe56187b557"),
    userId = UUID.fromString("0f0cdeb7-c6f0-4f1e-93a5-b3fd34506dc5"),
    createdAt = LocalDateTime.parse("2023-07-18T20:41:00"),
    reservedFromDate = LocalDate.parse("2023-07-19"),
    reservedToDate = LocalDate.parse("2023-07-20"),
    state = ApiReservationState.Pending,
    notes = "Please remove the duck from the parking spot, why is it even there?",
    parkingSpotId = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b"),
    plateNumber = "DSW 58100"
  )

  private lazy val apiCreateParkingSpotReservationExample = ApiCreateParkingSpotReservation(
    userId = UUID.fromString("0f0cdeb7-c6f0-4f1e-93a5-b3fd34506dc5"),
    reservedFrom = LocalDate.parse("2023-07-19"),
    reservedTo = LocalDate.parse("2023-07-20"),
    notes = "Please remove the duck from the parking spot, why is it even there?",
    parkingSpotId = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b"),
    plateNumber = "DSW 58100"
  )

  private lazy val officeIdExample = UUID.fromString("451aec8c-d7ec-4ff9-a091-cc6c944aefb1")
  private lazy val reservedFromExample = LocalDate.parse("2024-09-24")
  private lazy val reservationStatesExample: CommaSeparated[ApiReservationState] =
    Delimited(List(ApiReservationState.Pending, ApiReservationState.Confirmed))
  private lazy val userIdExample = UUID.fromString("8b7a9bdd-b729-4427-83c3-6eaee3c97171")

  private lazy val apiParkingSpotReservationListViewExample = ApiParkingSpotReservationListView(
    reservations = List(
      ApiParkingSpotReservationView(
        id = UUID.fromString("dcd64f97-a57a-4b57-9e63-dbe56187b557"),
        reservedFromDate = LocalDate.parse("2024-09-24"),
        reservedToDate = LocalDate.parse("2024-09-27"),
        state = ApiReservationState.Confirmed,
        notes = "Please remove the duck from the parking spot, why is it even there?",
        user = ApiUserView(
          id = UUID.fromString("a28420c3-da66-4f87-bac3-a399bafa2756"),
          firstName = "John",
          lastName = "Doe",
          email = "john.doe@example.com"
        ),
        parkingSpot = ApiParkingSpotView(
          id = UUID.fromString("2215a152-cc29-4086-b5a8-7aef2a96867b"),
          name = "A15"
        ),
        plateNumber = "DW M3678"
      ),
      ApiParkingSpotReservationView(
        id = UUID.fromString("4ccb446a-7e7a-4d6e-8e11-3736c8315ccd"),
        reservedFromDate = LocalDate.parse("2024-09-28"),
        reservedToDate = LocalDate.parse("2024-09-28"),
        state = ApiReservationState.Pending,
        notes = "Will try no to hit anyone this time",
        user = ApiUserView(
          id = UUID.fromString("a28420c3-da66-4f87-bac3-a399bafa2756"),
          firstName = "John",
          lastName = "Doe",
          email = "john.doe@example.com"
        ),
        parkingSpot = ApiParkingSpotView(
          id = UUID.fromString("2215a152-cc29-4086-b5a8-7aef2a96867b"),
          name = "B5"
        ),
        plateNumber = "DDZ 999XD"
      ),
      ApiParkingSpotReservationView(
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
        parkingSpot = ApiParkingSpotView(
          id = UUID.fromString("2bbc890f-d5d4-406d-978c-f233245a24ac"),
          name = "E1"
        ),
        plateNumber = "ZKL 43522"
      )
    ),
    pagination = ApiPagination(
      limit = 3,
      offset = 0,
      hasMoreResults = true
    )
  )
}
