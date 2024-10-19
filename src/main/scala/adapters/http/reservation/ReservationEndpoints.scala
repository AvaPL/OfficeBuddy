package io.github.avapl
package adapters.http.reservation

import adapters.http.ApiError
import adapters.http.SecuredApiEndpoint
import cats.MonadThrow
import cats.data.EitherT
import cats.effect.Clock
import cats.syntax.all._
import domain.model.account.Role
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.User
import domain.model.error.reservation._
import domain.model.reservation.Reservation
import domain.service.reservation.ReservationService
import java.util.UUID
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint

trait ReservationEndpoints[
  F[_],
  R <: Reservation
] extends SecuredApiEndpoint[F] {

  protected def reservationService: ReservationService[F, R]
  implicit protected def clock: Clock[F]
  implicit protected def monadThrow: MonadThrow[F]

  protected def reservationEntityPathName: String
  protected def reservationEntityEndpoints: List[ServerEndpoint[Any, F]]

  override protected val apiEndpointName: String = "reservation"

  val endpoints: List[ServerEndpoint[Any, F]] =
    cancelReservationEndpoint ::
      confirmReservationEndpoint ::
      rejectReservationEndpoint ::
      reservationEntityEndpoints

  protected lazy val recoverOnNotFound: PartialFunction[Throwable, Either[ApiError, Nothing]] = {
    case ReservationNotFound(reservationId) =>
      ApiError.NotFound(s"Reservation [id: $reservationId] was not found").asLeft
  }

  private lazy val cancelReservationEndpoint =
    securedEndpoint(requiredRole = User).put
      .summary("Cancel a reservation")
      .in(reservationEntityPathName)
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
      .readReservation(reservationId)
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
      .in(reservationEntityPathName)
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
      .in(reservationEntityPathName)
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
}
