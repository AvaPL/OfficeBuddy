package io.github.avapl
package adapters.http.parkingspot

import adapters.auth.repository.PublicKeyRepository
import adapters.auth.service.ClaimsExtractorService
import adapters.http.ApiError
import adapters.http.PaginationInput
import adapters.http.SecuredApiEndpoint
import adapters.http.model.view.ApiPagination
import adapters.http.parkingspot.model.ApiCreateParkingSpot
import adapters.http.parkingspot.model.ApiParkingSpot
import adapters.http.parkingspot.model.ApiUpdateParkingSpot
import adapters.http.parkingspot.model.view.ApiParkingSpotListView
import adapters.http.parkingspot.model.view.ApiParkingSpotView
import adapters.http.parkingspot.model.view.ApiReservableParkingSpotView
import cats.MonadThrow
import cats.effect.Clock
import cats.syntax.all._
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.User
import domain.model.error.office.OfficeNotFound
import domain.model.error.parkingspot.DuplicateParkingSpotNameForOffice
import domain.model.error.parkingspot.ParkingSpotNotFound
import domain.repository.parkingspot.view.ParkingSpotViewRepository
import domain.service.parkingspot.ParkingSpotService
import java.time.LocalDate
import java.util.UUID
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint

// TODO: Add tests
class ParkingSpotEndpoints[F[_]: Clock: MonadThrow](
  parkingSpotService: ParkingSpotService[F],
  parkingSpotViewRepository: ParkingSpotViewRepository[F],
  override val publicKeyRepository: PublicKeyRepository[F],
  override val claimsExtractor: ClaimsExtractorService
) extends SecuredApiEndpoint[F]
  with PaginationInput {

  override protected val apiEndpointName: String = "parking"

  val endpoints: List[ServerEndpoint[Any, F]] =
    createParkingSpotEndpoint ::
      readParkingSpotEndpoint ::
      updateParkingSpotEndpoint ::
      archiveParkingSpotEndpoint ::
      parkingSpotListViewEndpoint ::
      reservableParkingSpotViewListEndpoint ::
      Nil

  private lazy val createParkingSpotEndpoint =
    securedEndpoint(requiredRole = OfficeManager).post
      .summary("Create a parking spot")
      .description("Required role: office manager")
      .in(
        jsonBody[ApiCreateParkingSpot]
          .example(apiCreateParkingSpotExample)
      )
      .out(
        statusCode(StatusCode.Created) and jsonBody[ApiParkingSpot]
          .description("Parking spot created")
          .example(apiParkingSpotExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.BadRequest) and jsonBody[ApiError.BadRequest]
            .description("Office with the given ID was not found")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.Conflict) and jsonBody[ApiError.Conflict]
            .description("Parking spot with the given name already exists in the office")
        )
      )
      .serverLogic(_ => createParkingSpot)

  private def createParkingSpot(apiCreateParkingSpot: ApiCreateParkingSpot) =
    parkingSpotService
      .createParkingSpot(apiCreateParkingSpot.toDomain)
      .map(ApiParkingSpot.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case OfficeNotFound(officeId) =>
          ApiError.BadRequest(s"Office [id: $officeId] was not found").asLeft
        case DuplicateParkingSpotNameForOffice(name, officeId) =>
          ApiError
            .Conflict(
              s"Parking spot '${name.getOrElse("<unknown>")}' is already defined for office [id: ${officeId.getOrElse("<unknown>")}]"
            )
            .asLeft
      }

  private lazy val readParkingSpotEndpoint =
    securedEndpoint(requiredRole = User).get
      .summary("Find a parking spot by ID")
      .description("Required role: user")
      .in(path[UUID]("parkingSpotId"))
      .out(
        jsonBody[ApiParkingSpot]
          .description("Found parking spot")
          .example(apiParkingSpotExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Parking spot with the given ID was not found")
        )
      )
      .serverLogic(_ => readParkingSpot)

  private def readParkingSpot(parkingSpotId: UUID) =
    parkingSpotService
      .readParkingSpot(parkingSpotId)
      .map(ApiParkingSpot.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case ParkingSpotNotFound(parkingSpotId) =>
          ApiError.NotFound(s"Parking spot [id: $parkingSpotId] was not found").asLeft
      }

  private lazy val updateParkingSpotEndpoint =
    securedEndpoint(requiredRole = OfficeManager).patch
      .summary("Update a parking spot")
      .description("Required role: office manager")
      .in(
        path[UUID]("parkingSpotId") and jsonBody[ApiUpdateParkingSpot]
          .example(apiUpdateParkingSpotExample)
      )
      .out(
        jsonBody[ApiParkingSpot]
          .description("Updated parking spot")
          .example(apiParkingSpotExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.BadRequest) and jsonBody[ApiError.BadRequest]
            .description("Office with the given ID was not found")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Parking spot with the given ID was not found")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.Conflict) and jsonBody[ApiError.Conflict]
            .description("Parking spot with the given name already exists in the office")
        )
      )
      .serverLogic(_ => (updateParkingSpot _).tupled)

  private def updateParkingSpot(parkingSpotId: UUID, apiUpdateParkingSpot: ApiUpdateParkingSpot) =
    parkingSpotService
      .updateParkingSpot(parkingSpotId, apiUpdateParkingSpot.toDomain)
      .map(ApiParkingSpot.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case DuplicateParkingSpotNameForOffice(name, officeId) =>
          ApiError
            .Conflict(
              s"Parking spot '${name.getOrElse("<unknown>")}' is already defined for office [id: ${officeId.getOrElse("<unknown>")}]"
            )
            .asLeft
        case ParkingSpotNotFound(parkingSpotId) =>
          ApiError.NotFound(s"Parking spot [id: $parkingSpotId] was not found").asLeft
        case OfficeNotFound(officeId) =>
          ApiError.BadRequest(s"Office [id: $officeId] was not found").asLeft
      }

  private lazy val archiveParkingSpotEndpoint =
    securedEndpoint(requiredRole = OfficeManager).delete
      .summary("Archive a parking spot")
      .description(
        """Archives a parking spot. The parking spot is NOT deleted. The operation is idempotent ie. if the parking spot doesn't exist, the operation doesn't fail.
          |
          |Required role: office manager
          |""".stripMargin
      )
      .in(path[UUID]("parkingSpotId"))
      .out(
        statusCode(StatusCode.NoContent)
          .description("Parking spot archived or not found")
      )
      .serverLogic(_ => archiveParkingSpot)

  private def archiveParkingSpot(parkingSpotId: UUID) =
    parkingSpotService
      .archiveParkingSpot(parkingSpotId)
      .as(().asRight[ApiError])

  private lazy val parkingSpotListViewEndpoint =
    securedEndpoint(requiredRole = User).get
      .summary("Parking spots list view")
      .description(
        """List of parking spots assigned to an office to be displayed in the UI.
          |
          |Required role: user
          |""".stripMargin
      )
      .in("view" / "list")
      .in(
        query[UUID]("office_id")
          .description("Assigned office ID")
          .example(officeIdExample)
      )
      .in(paginationLimitAndOffset)
      .out(
        jsonBody[ApiParkingSpotListView]
          .description("List of parking spots")
          .example(apiParkingSpotListViewExample)
      )
      .serverLogic(_ => (parkingSpotListView _).tupled)

  private def parkingSpotListView(
    officeId: UUID,
    limit: Int,
    offset: Int
  ) =
    parkingSpotViewRepository
      .listParkingSpots(officeId, limit, offset)
      .map(ApiParkingSpotListView.fromDomain)
      .map(_.asRight[ApiError])

  private lazy val reservableParkingSpotViewListEndpoint =
    securedEndpoint(requiredRole = User).get
      .summary("Parking spots available for reservation")
      .description(
        """List of parking spots available for reservation in a given office in the specified time range.
          |
          |Required role: user
          |""".stripMargin
      )
      .in("view" / "reservable")
      .in(
        query[UUID]("office_id")
          .description("Assigned office ID")
          .example(officeIdExample)
      )
      .in(
        query[LocalDate]("reservation_from")
          .description("Reservation start date")
          .example(LocalDate.parse("2024-09-24"))
      )
      .in(
        query[LocalDate]("reservation_to")
          .description("Reservation end date")
          .example(LocalDate.parse("2024-09-27"))
      )
      .out(
        jsonBody[List[ApiReservableParkingSpotView]]
          .description("List of parking spots available for reservation")
          .example(apiReservableParkingSpotViewListExample)
      )
      .serverLogic(_ => (reservableParkingSpotsView _).tupled)

  private def reservableParkingSpotsView(
    officeId: UUID,
    reservationFrom: LocalDate,
    reservationTo: LocalDate
  ) =
    parkingSpotViewRepository
      .listParkingSpotsAvailableForReservation(officeId, reservationFrom, reservationTo)
      .map(_.map(ApiReservableParkingSpotView.fromDomain).asRight[ApiError])

  private lazy val apiParkingSpotExample = ApiParkingSpot(
    id = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b"),
    name = "P1",
    isAvailable = true,
    notes = List("Near the entrance"),
    isHandicapped = true,
    isUnderground = false,
    officeId = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298"),
    isArchived = false
  )

  private lazy val apiCreateParkingSpotExample = ApiCreateParkingSpot(
    name = "P1",
    isAvailable = true,
    notes = List("Near the entrance"),
    isHandicapped = true,
    isUnderground = false,
    officeId = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298")
  )

  private lazy val apiUpdateParkingSpotExample = ApiUpdateParkingSpot(
    name = Some("P1"),
    isAvailable = Some(true),
    notes = Some(List("Near the entrance")),
    isHandicapped = Some(true),
    isUnderground = Some(false),
    officeId = Some(UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298")),
    isArchived = Some(false)
  )

  private lazy val officeIdExample = UUID.fromString("d5c4921a-3f7f-474f-8cba-e6fc8cb6c545")

  private lazy val apiParkingSpotListViewExample = ApiParkingSpotListView(
    parkingSpots = List(
      ApiParkingSpotView(
        id = UUID.fromString("1a84cc45-11db-468b-bf69-125ed293f6c9"),
        name = "A1",
        isAvailable = true,
        isHandicapped = true,
        isUnderground = false
      ),
      ApiParkingSpotView(
        id = UUID.fromString("95af1416-0526-42af-8f25-71b09a6793d0"),
        name = "A2",
        isAvailable = true,
        isHandicapped = false,
        isUnderground = true
      ),
      ApiParkingSpotView(
        id = UUID.fromString("71659873-7921-4bbf-9cdc-e62bac4b6177"),
        name = "A3",
        isAvailable = false,
        isHandicapped = false,
        isUnderground = true
      )
    ),
    pagination = ApiPagination(
      limit = 3,
      offset = 0,
      hasMoreResults = true
    )
  )

  private lazy val apiReservableParkingSpotViewListExample = List(
    ApiReservableParkingSpotView(
      id = UUID.fromString("1a84cc45-11db-468b-bf69-125ed293f6c9"),
      name = "A1",
      isHandicapped = true,
      isUnderground = false
    ),
    ApiReservableParkingSpotView(
      id = UUID.fromString("95af1416-0526-42af-8f25-71b09a6793d0"),
      name = "A2",
      isHandicapped = false,
      isUnderground = true
    ),
    ApiReservableParkingSpotView(
      id = UUID.fromString("71659873-7921-4bbf-9cdc-e62bac4b6177"),
      name = "A3",
      isHandicapped = false,
      isUnderground = true
    )
  )
}
