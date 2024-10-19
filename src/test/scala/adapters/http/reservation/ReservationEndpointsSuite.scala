package io.github.avapl
package adapters.http.reservation

import adapters.auth.repository.PublicKeyRepository
import adapters.auth.service.ClaimsExtractorService
import adapters.http.fixture.SecuredApiEndpointFixture
import cats.MonadThrow
import cats.effect.Clock
import cats.effect.IO
import domain.model.account.Role
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
import domain.model.error.reservation.InvalidStateTransition
import domain.model.error.reservation.ReservationNotFound
import domain.model.reservation.DeskReservation
import domain.model.reservation.ReservationState
import domain.service.reservation.DeskReservationService
import domain.service.reservation.ReservationService
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import org.mockito.ArgumentMatchersSugar
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import sttp.client3._
import sttp.model.StatusCode
import sttp.tapir.server.ServerEndpoint
import weaver.SimpleIOSuite

object ReservationEndpointsSuite
  extends SimpleIOSuite
  with MockitoSugar
  with ArgumentMatchersSugar
  with MockitoCats
  with SecuredApiEndpointFixture {

  test(
    """GIVEN cancel reservation endpoint
      | WHEN the reservation is cancelled by the owning user
      | THEN 204 NoContent is returned
      |""".stripMargin
  ) {
    val userId = anyUserId
    val reservation = anyDeskReservation.copy(userId = userId)
    val reservationService = mock[DeskReservationService[IO]]
    whenF(reservationService.readReservation(any)) thenReturn reservation
    whenF(reservationService.cancelReservation(any)) thenReturn ()

    val response = sendRequest(
      reservationService,
      role = User,
      requesterAccountId = userId
    ) {
      basicRequest.put(uri"http://test.com/reservation/$reservationEntityPathName/${reservation.id}/cancel")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NoContent)
  }

  test(
    """GIVEN cancel reservation endpoint
      | WHEN the reservation is cancelled by an office manager
      | THEN 204 NoContent is returned
      |""".stripMargin
  ) {
    val reservationService = mock[DeskReservationService[IO]]
    whenF(reservationService.readReservation(any)) thenReturn anyDeskReservation
    whenF(reservationService.cancelReservation(any)) thenReturn ()

    val response = sendRequest(
      reservationService,
      role = OfficeManager,
      requesterAccountId = anyOfficeManagerId
    ) {
      basicRequest.put(uri"http://test.com/reservation/$reservationEntityPathName/$anyReservationId1/cancel")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NoContent)
  }

  test(
    """GIVEN cancel reservation endpoint
      | WHEN the reservation is cancelled by another user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val reservationOwnerId = UUID.fromString("fd704d6b-d56d-426b-9972-c5ed1027f578")
    val reservation = anyDeskReservation.copy(userId = reservationOwnerId)
    val reservationService = mock[DeskReservationService[IO]]
    whenF(reservationService.readReservation(any)) thenReturn reservation

    val requesterUserId = UUID.fromString("5df2a865-8dde-44b1-aeff-f80d86d8fd6d")
    val response = sendRequest(
      reservationService,
      role = User,
      requesterAccountId = requesterUserId
    ) {
      basicRequest.put(uri"http://test.com/reservation/$reservationEntityPathName/${reservation.id}/cancel")
    }

    for {
      response <- response
    } yield {
      verify(reservationService, never).cancelReservation(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN cancel reservation endpoint
      | WHEN the state transition is invalid (InvalidStateTransition error)
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId1
    val reservationService = mock[DeskReservationService[IO]]
    whenF(reservationService.readReservation(any)) thenReturn anyDeskReservation
    whenF(reservationService.cancelReservation(any)) thenFailWith
      InvalidStateTransition(reservationId, ReservationState.Rejected, ReservationState.Cancelled)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationEntityPathName/$reservationId/cancel")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN cancel reservation endpoint
      | WHEN the reservation is not found (ReservationNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[DeskReservationService[IO]].readReservation(any)) thenFailWith ReservationNotFound(reservationId)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationEntityPathName/$reservationId/cancel")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN confirm reservation endpoint
      | WHEN the reservation is confirmed by an office manager
      | THEN 204 NoContent is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[DeskReservationService[IO]].confirmReservation(any)) thenReturn ()

    val response = sendRequest(reservationService, role = OfficeManager) {
      basicRequest.put(uri"http://test.com/reservation/$reservationEntityPathName/$reservationId/confirm")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NoContent)
  }

  test(
    """GIVEN confirm reservation endpoint
      | WHEN there is an attempt to confirm a reservation by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId1
    val reservationService = mock[DeskReservationService[IO]]

    val response = sendRequest(reservationService, role = User) {
      basicRequest.put(uri"http://test.com/reservation/$reservationEntityPathName/$reservationId/confirm")
    }

    for {
      response <- response
    } yield {
      verify(reservationService, never).confirmReservation(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN confirm reservation endpoint
      | WHEN the state transition is invalid (InvalidStateTransition error)
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[DeskReservationService[IO]].confirmReservation(any)) thenFailWith
        InvalidStateTransition(reservationId, ReservationState.Rejected, ReservationState.Confirmed)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationEntityPathName/$reservationId/confirm")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN confirm reservation endpoint
      | WHEN the reservation is not found (ReservationNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[DeskReservationService[IO]].confirmReservation(any)) thenFailWith ReservationNotFound(reservationId)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationEntityPathName/$reservationId/confirm")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN reject reservation endpoint
      | WHEN the reservation is rejected by an office manager
      | THEN 204 NoContent is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[DeskReservationService[IO]].rejectReservation(any)) thenReturn ()

    val response = sendRequest(reservationService, role = OfficeManager) {
      basicRequest.put(uri"http://test.com/reservation/$reservationEntityPathName/$reservationId/reject")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NoContent)
  }

  test(
    """GIVEN reject reservation endpoint
      | WHEN there is an attempt to reject a reservation by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId1
    val reservationService = mock[DeskReservationService[IO]]

    val response = sendRequest(reservationService, role = User) {
      basicRequest.put(uri"http://test.com/reservation/$reservationEntityPathName/$reservationId/reject")
    }

    for {
      response <- response
    } yield {
      verify(reservationService, never).rejectReservation(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN reject reservation endpoint
      | WHEN the state transition is invalid (InvalidStateTransition error)
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[DeskReservationService[IO]].rejectReservation(any)) thenFailWith
        InvalidStateTransition(reservationId, ReservationState.Cancelled, ReservationState.Rejected)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationEntityPathName/$reservationId/reject")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN reject reservation endpoint
      | WHEN the reservation is not found (ReservationNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[DeskReservationService[IO]].rejectReservation(any)) thenFailWith ReservationNotFound(reservationId)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationEntityPathName/$reservationId/reject")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  private def sendRequest(
    deskReservationService: DeskReservationService[IO],
    role: Role = SuperAdmin,
    requesterAccountId: UUID = anySuperAdminId
  )(
    request: Request[Either[String, String], Any]
  ) = {
    // aliases to avoid shadowing
    val _reservationEntityPathName = reservationEntityPathName
    val _publicKeyRepository = publicKeyRepository

    sendSecuredApiEndpointRequest(request, role, requesterAccountId) { claimsExtractorService =>
      new ReservationEndpoints[IO, DeskReservation] {
        override protected val reservationService: ReservationService[IO, DeskReservation] = deskReservationService
        override implicit protected lazy val clock: Clock[IO] = implicitly
        override implicit protected lazy val monadThrow: MonadThrow[IO] = implicitly
        override protected lazy val reservationEntityPathName: String = _reservationEntityPathName
        override protected lazy val reservationEntityEndpoints: List[ServerEndpoint[Any, IO]] = Nil
        override protected val claimsExtractor: ClaimsExtractorService = claimsExtractorService
        override protected val publicKeyRepository: PublicKeyRepository[IO] = _publicKeyRepository
      }.endpoints
    }
  }

  private lazy val reservationEntityPathName = "desk"

  private lazy val anyDeskReservation = DeskReservation(
    id = anyReservationId1,
    userId = anyUserId,
    createdAt = anyCreatedAt,
    reservedFromDate = LocalDate.parse("2023-07-19"),
    reservedToDate = LocalDate.parse("2023-07-20"),
    state = ReservationState.Pending,
    notes = "Please remove the duck from the desk, it scares me",
    deskId = anyDeskId
  )

  private lazy val anyUserId = UUID.fromString("0f0cdeb7-c6f0-4f1e-93a5-b3fd34506dc5")

  private lazy val anyOfficeManagerId = UUID.fromString("5cf7279b-1c70-416e-84c0-cb7674cd3c05")

  private lazy val anySuperAdminId = UUID.fromString("53eea01d-4129-4098-9fe5-f07d768c937e")

  private lazy val anyDeskId = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")

  private lazy val anyReservationId1 = UUID.fromString("dcd64f97-a57a-4b57-9e63-dbe56187b557")

  private lazy val anyCreatedAt = LocalDateTime.parse("2023-07-18T20:41:00")
}
