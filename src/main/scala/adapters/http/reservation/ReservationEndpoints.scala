package io.github.avapl
package adapters.http.reservation

import adapters.auth.repository.PublicKeyRepository
import adapters.auth.service.ClaimsExtractorService
import adapters.http.ApiError
import adapters.http.SecuredApiEndpoint
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
import domain.service.reservation.ReservationService
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint

class ReservationEndpoints[F[_]: Clock: MonadThrow](
  reservationService: ReservationService[F],
  override val publicKeyRepository: PublicKeyRepository[F],
  override val rolesExtractor: ClaimsExtractorService
) extends SecuredApiEndpoint[F] {

  override protected val apiEndpointName: String = "reservation"

  val endpoints: List[ServerEndpoint[Any, F]] =
    reserveDeskEndpoint ::
      readDeskReservationEndpoint ::
      cancelReservationEndpoint ::
      confirmReservationEndpoint ::
      rejectReservationEndpoint ::
      Nil

  private lazy val reserveDeskEndpoint =
    securedEndpoint(requiredRole = User).post
      .summary("Reserve a desk")
      .in("desk")
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
      .reserveDesk(apiCreateDeskReservation.toDomain)
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
      .in("desk" / path[UUID]("reservationId"))
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
      .readDeskReservation(reservationId)
      .map(ApiDeskReservation.fromDomain)
      .map(_.asRight[ApiError])
      .recover(recoverOnNotFound)

  private lazy val recoverOnNotFound: PartialFunction[Throwable, Either[ApiError, Nothing]] = {
    case ReservationNotFound(reservationId) =>
      ApiError.NotFound(s"Reservation [id: $reservationId] was not found").asLeft
  }

  private lazy val cancelReservationEndpoint =
    securedEndpoint(requiredRole = User).put
      .summary("Cancel a reservation")
      .description(
        "Cancels a reservation. This operation has to be called by the reservation owner on a PENDING or CONFIRMED reservation."
      )
      .in(path[UUID]("reservationId") / "cancel")
      .out(
        statusCode(StatusCode.NoContent)
          .description("Reservation cancelled")
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.BadRequest) and jsonBody[ApiError.BadRequest]
            .description("The current reservation state doesn't allow cancelling it")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Reservation with the given ID was not found")
        )
      )
      .serverLogic { accessToken =>
        cancelReservation(
          requesterRoles = accessToken.roles,
          requesterAccountId = accessToken.accountId
        )
      }

  private def cancelReservation(
    requesterRoles: List[Role],
    requesterAccountId: UUID
  )(
    reservationId: UUID
  ): F[Either[ApiError, Unit]] = {
    for {
      requesterOwnsReservation <- ownsReservation(reservationId, requesterAccountId)
      hasPermission = requesterOwnsReservation || requesterRoles.exists(_.hasAccess(OfficeManager))
      result <-
        if (hasPermission) cancelReservation(reservationId)
        else EitherT.leftT[F, Unit].apply[ApiError](ApiError.Forbidden)
    } yield result
  }.value

  private def ownsReservation(reservationId: UUID, accountId: UUID) = EitherT {
    reservationService
      .readDeskReservation(reservationId)
      .map(_.userId)
      .map(_ == accountId)
      .map(_.asRight[ApiError])
      .recover(recoverOnNotFound)
  }

  private def cancelReservation(reservationId: UUID): EitherT[F, ApiError, Unit] = EitherT {
    reservationService
      .cancelReservation(reservationId)
      .map(_.asRight[ApiError])
      .recover(recoverOnNotFound)
      .recover(recoverOnInvalidStateTransition)
  }

  private lazy val recoverOnInvalidStateTransition: PartialFunction[Throwable, Either[ApiError, Nothing]] = {
    case InvalidStateTransition(reservationId, currentState, newState) =>
      ApiError.BadRequest(s"Reservation [id: $reservationId] cannot transition from $currentState to $newState").asLeft
  }

  private lazy val confirmReservationEndpoint =
    securedEndpoint(requiredRole = OfficeManager).put
      .summary("Confirm a reservation")
      .description(
        "Confirms a reservation. This operation has to be called by the office manager on a PENDING reservation."
      )
      .in(path[UUID]("reservationId") / "confirm")
      .out(
        statusCode(StatusCode.NoContent)
          .description("Reservation confirmed")
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.BadRequest) and jsonBody[ApiError.BadRequest]
            .description("The current reservation state doesn't allow confirming it")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Reservation with the given ID was not found")
        )
      )
      .serverLogic(_ => confirmReservation)

  private def confirmReservation(reservationId: UUID) =
    reservationService
      .confirmReservation(reservationId)
      .map(_.asRight[ApiError])
      .recover(recoverOnNotFound)
      .recover(recoverOnInvalidStateTransition)

  private lazy val rejectReservationEndpoint =
    securedEndpoint(requiredRole = OfficeManager).put
      .summary("Reject a reservation")
      .description(
        "Rejects a reservation. This operation has to be called by the office manager on a PENDING or CONFIRMED reservation."
      )
      .in(path[UUID]("reservationId") / "reject")
      .out(
        statusCode(StatusCode.NoContent)
          .description("Reservation rejected")
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.BadRequest) and jsonBody[ApiError.BadRequest]
            .description("The current reservation state doesn't allow rejecting it")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Reservation with the given ID was not found")
        )
      )
      .serverLogic(_ => rejectReservation)

  private def rejectReservation(reservationId: UUID) =
    reservationService
      .rejectReservation(reservationId)
      .map(_.asRight[ApiError])
      .recover(recoverOnNotFound)
      .recover(recoverOnInvalidStateTransition)

  private lazy val apiDeskReservationExample = ApiDeskReservation(
    id = UUID.fromString("dcd64f97-a57a-4b57-9e63-dbe56187b557"),
    userId = UUID.fromString("0f0cdeb7-c6f0-4f1e-93a5-b3fd34506dc5"),
    createdAt = LocalDateTime.parse("2023-07-18T20:41:00"),
    reservedFrom = LocalDateTime.parse("2023-07-19T00:00:00"),
    reservedTo = LocalDateTime.parse("2023-07-20T23:59:59"),
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
}
