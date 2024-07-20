package io.github.avapl
package adapters.http.reservation

import cats.effect.IO
import domain.model.error.desk.DeskNotFound
import domain.model.error.reservation.InvalidStateTransition
import domain.model.error.reservation.OverlappingReservations
import domain.model.error.reservation.ReservationNotFound
import domain.model.error.user.UserNotFound
import domain.model.reservation.DeskReservation
import domain.model.reservation.ReservationState
import domain.service.reservation.ReservationService
import io.circe.parser._
import io.circe.syntax._
import io.github.avapl.adapters.auth.model.PublicKey
import io.github.avapl.adapters.http.fixture.SecuredApiEndpointFixture
import io.github.avapl.domain.model.account.Role
import io.github.avapl.domain.model.account.Role.OfficeManager
import io.github.avapl.domain.model.account.Role.SuperAdmin
import io.github.avapl.domain.model.account.Role.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID
import org.mockito.ArgumentMatchersSugar
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import sttp.client3._
import sttp.client3.circe._
import sttp.client3.testing.SttpBackendStub
import sttp.model.StatusCode
import sttp.tapir.integ.cats.effect.CatsMonadError
import sttp.tapir.server.stub.TapirStubInterpreter
import weaver.SimpleIOSuite

object ReservationEndpointsSuite
  extends SimpleIOSuite
  with MockitoSugar
  with ArgumentMatchersSugar
  with MockitoCats
  with SecuredApiEndpointFixture {

  test(
    """GIVEN reserve desk endpoint
      | WHEN a reservation is POSTed and created by a user
      | THEN 201 Created and the created reservation is returned
      |""".stripMargin
  ) {
    val reservationToCreate = anyApiCreateDeskReservation
    val reservation = DeskReservation(
      id = anyReservationId,
      userId = reservationToCreate.userId,
      createdAt = anyCreatedAt,
      reservedFrom = reservationToCreate.reservedFrom.atStartOfDay(),
      reservedTo = reservationToCreate.reservedTo.atTime(LocalTime.MAX),
      state = ReservationState.Pending,
      notes = reservationToCreate.notes,
      deskId = reservationToCreate.deskId
    )
    val reservationService = whenF(mock[ReservationService[IO]].reserveDesk(any)) thenReturn reservation

    val response = sendRequest(
      reservationService,
      role = User,
      accountId = reservationToCreate.userId
    ) {
      basicRequest
        .post(uri"http://test.com/reservation/desk")
        .body(reservationToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiDeskReservation.fromDomain(reservation).asJson
    )
  }

  test(
    """GIVEN reserve desk endpoint
      | WHEN a reservation is POSTed and created by an office manager for another user
      | THEN 201 Created and the created reservation is returned
      |""".stripMargin
  ) {
    val reservationToCreate = anyApiCreateDeskReservation
    val reservation = DeskReservation(
      id = anyReservationId,
      userId = reservationToCreate.userId,
      createdAt = anyCreatedAt,
      reservedFrom = reservationToCreate.reservedFrom.atStartOfDay(),
      reservedTo = reservationToCreate.reservedTo.atTime(LocalTime.MAX),
      state = ReservationState.Pending,
      notes = reservationToCreate.notes,
      deskId = reservationToCreate.deskId
    )
    val reservationService = whenF(mock[ReservationService[IO]].reserveDesk(any)) thenReturn reservation

    val response = sendRequest(
      reservationService,
      role = OfficeManager,
      accountId = anyOfficeManagerId
    ) {
      basicRequest
        .post(uri"http://test.com/reservation/desk")
        .body(reservationToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiDeskReservation.fromDomain(reservation).asJson
    )
  }

  test(
    """GIVEN reserve desk endpoint
      | WHEN there is an attempt to create a reservation for a user by another user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val requesterId = UUID.fromString("10edac56-4725-4c06-92bb-0e8fee647426")
    val reservationUserId = UUID.fromString("a8907e2a-621b-494b-ba56-bd0bdc02aa15")
    val reservationToCreate = anyApiCreateDeskReservation.copy(userId = reservationUserId)
    val reservationService = mock[ReservationService[IO]]

    val response = sendRequest(
      reservationService,
      role = User,
      accountId = requesterId
    ) {
      basicRequest
        .post(uri"http://test.com/reservation/desk")
        .body(reservationToCreate)
    }

    for {
      response <- response
    } yield {
      verify(reservationService, never).reserveDesk(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN reserve desk endpoint
      | WHEN creating the reservation fails with DeskNotFound error
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationService =
      whenF(mock[ReservationService[IO]].reserveDesk(any)) thenFailWith DeskNotFound(anyDeskId)

    val response = sendRequest(reservationService) {
      basicRequest
        .post(uri"http://test.com/reservation/desk")
        .body(anyApiCreateDeskReservation)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN reserve desk endpoint
      | WHEN creating the reservation fails with UserNotFound error
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationService =
      whenF(mock[ReservationService[IO]].reserveDesk(any)) thenFailWith UserNotFound(anyUserId)

    val response = sendRequest(reservationService) {
      basicRequest
        .post(uri"http://test.com/reservation/desk")
        .body(anyApiCreateDeskReservation)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN reserve desk endpoint
      | WHEN creating the reservation fails with OverlappingReservations error
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val reservationService =
      whenF(mock[ReservationService[IO]].reserveDesk(any)) thenFailWith OverlappingReservations

    val response = sendRequest(reservationService) {
      basicRequest
        .post(uri"http://test.com/reservation/desk")
        .body(anyApiCreateDeskReservation)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN read desk reservation endpoint
      | WHEN an existing reservation is read by a user
      | THEN 200 OK and the read reservation is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId
    val reservation = anyDeskReservation.copy(id = reservationId)
    val reservationService = whenF(mock[ReservationService[IO]].readDeskReservation(any)) thenReturn reservation

    val response = sendRequest(reservationService, role = User) {
      basicRequest.get(uri"http://test.com/reservation/desk/$reservationId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiDeskReservation.fromDomain(reservation).asJson
    )
  }

  test(
    """GIVEN read desk reservation endpoint
      | WHEN the reservation is not found (ReservationNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].readDeskReservation(any)) thenFailWith ReservationNotFound(reservationId)

    val response = sendRequest(reservationService) {
      basicRequest.get(uri"http://test.com/reservation/desk/$reservationId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN cancel reservation endpoint
      | WHEN the reservation is cancelled by the owning user
      | THEN 204 NoContent is returned
      |""".stripMargin
  ) {
    val userId = anyUserId
    val reservation = anyDeskReservation.copy(userId = userId)
    val reservationService = mock[ReservationService[IO]]
    whenF(reservationService.readDeskReservation(any)) thenReturn reservation
    whenF(reservationService.cancelReservation(any)) thenReturn ()

    val response = sendRequest(
      reservationService,
      role = User,
      accountId = userId
    ) {
      basicRequest.put(uri"http://test.com/reservation/${reservation.id}/cancel")
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
    val reservationService = mock[ReservationService[IO]]
    whenF(reservationService.readDeskReservation(any)) thenReturn anyDeskReservation
    whenF(reservationService.cancelReservation(any)) thenReturn ()

    val response = sendRequest(
      reservationService,
      role = OfficeManager,
      accountId = anyOfficeManagerId
    ) {
      basicRequest.put(uri"http://test.com/reservation/$anyReservationId/cancel")
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
    val reservationService = mock[ReservationService[IO]]
    whenF(reservationService.readDeskReservation(any)) thenReturn reservation

    val requesterUserId = UUID.fromString("5df2a865-8dde-44b1-aeff-f80d86d8fd6d")
    val response = sendRequest(
      reservationService,
      role = User,
      accountId = requesterUserId
    ) {
      basicRequest.put(uri"http://test.com/reservation/${reservation.id}/cancel")
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
    val reservationId = anyReservationId
    val reservationService = mock[ReservationService[IO]]
    whenF(reservationService.readDeskReservation(any)) thenReturn anyDeskReservation
    whenF(reservationService.cancelReservation(any)) thenFailWith
      InvalidStateTransition(reservationId, ReservationState.Rejected, ReservationState.Cancelled)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/cancel")
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
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].readDeskReservation(any)) thenFailWith ReservationNotFound(reservationId)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/cancel")
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
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].confirmReservation(any)) thenReturn ()

    val response = sendRequest(reservationService, role = OfficeManager) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/confirm")
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
    val reservationId = anyReservationId
    val reservationService = mock[ReservationService[IO]]

    val response = sendRequest(reservationService, role = User) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/confirm")
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
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].confirmReservation(any)) thenFailWith
        InvalidStateTransition(reservationId, ReservationState.Rejected, ReservationState.Confirmed)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/confirm")
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
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].confirmReservation(any)) thenFailWith ReservationNotFound(reservationId)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/confirm")
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
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].rejectReservation(any)) thenReturn ()

    val response = sendRequest(reservationService, role = OfficeManager) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/reject")
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
    val reservationId = anyReservationId
    val reservationService = mock[ReservationService[IO]]

    val response = sendRequest(reservationService, role = User) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/reject")
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
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].rejectReservation(any)) thenFailWith
        InvalidStateTransition(reservationId, ReservationState.Cancelled, ReservationState.Rejected)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/reject")
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
    val reservationId = anyReservationId
    val reservationService =
      whenF(mock[ReservationService[IO]].rejectReservation(any)) thenFailWith ReservationNotFound(reservationId)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/reject")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  private def sendRequest(
    reservationService: ReservationService[IO],
    role: Role = SuperAdmin,
    accountId: UUID = anySuperAdminId
  )(
    request: Request[Either[String, String], Any]
  ) =
    sendSecuredApiEndpointRequest(request, role, accountId) { rolesExtractorService =>
      new ReservationEndpoints[IO](reservationService, publicKeyRepository, rolesExtractorService).endpoints
    }

  private def bodyJson(response: Response[Either[String, String]]) =
    response.body.flatMap(parse).toOption.get

  private lazy val anyDeskReservation = DeskReservation(
    id = anyReservationId,
    userId = anyUserId,
    createdAt = anyCreatedAt,
    reservedFrom = LocalDateTime.parse("2023-07-19T00:00:00"),
    reservedTo = LocalDateTime.parse("2023-07-20T23:59:59"),
    state = ReservationState.Pending,
    notes = "Please remove the duck from the desk, it scares me",
    deskId = anyDeskId
  )

  private lazy val anyApiCreateDeskReservation = ApiCreateDeskReservation(
    userId = anyUserId,
    reservedFrom = LocalDate.parse("2023-07-19"),
    reservedTo = LocalDate.parse("2023-07-20"),
    notes = "Please remove the duck from the desk, it scares me",
    deskId = anyDeskId
  )

  private lazy val anyUserId = UUID.fromString("0f0cdeb7-c6f0-4f1e-93a5-b3fd34506dc5")

  private lazy val anyOfficeManagerId = UUID.fromString("5cf7279b-1c70-416e-84c0-cb7674cd3c05")

  private lazy val anySuperAdminId = UUID.fromString("53eea01d-4129-4098-9fe5-f07d768c937e")

  private lazy val anyDeskId = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")

  private lazy val anyReservationId = UUID.fromString("dcd64f97-a57a-4b57-9e63-dbe56187b557")

  private lazy val anyCreatedAt = LocalDateTime.parse("2023-07-18T20:41:00")
}
