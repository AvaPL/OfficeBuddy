package io.github.avapl
package adapters.http.reservation

import adapters.auth.model.PublicKey
import adapters.http.fixture.SecuredApiEndpointFixture
import adapters.http.reservation.model.ApiCreateDeskReservation
import adapters.http.reservation.model.ApiDeskReservation
import adapters.http.reservation.model.ApiReservationState
import adapters.http.reservation.model.view.ApiDeskReservationListView

import cats.effect.IO
import domain.model.account.Role
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
import domain.model.error.desk.DeskNotFound
import domain.model.error.reservation.InvalidStateTransition
import domain.model.error.reservation.OverlappingReservations
import domain.model.error.reservation.ReservationNotFound
import domain.model.error.user.UserNotFound
import domain.model.reservation.DeskReservation
import domain.model.reservation.ReservationState
import domain.model.reservation.view.DeskReservationListView
import domain.model.reservation.view.DeskReservationView
import domain.model.reservation.view.DeskView
import domain.model.reservation.view.UserView
import domain.model.view.Pagination
import domain.repository.reservation.view.ReservationViewRepository
import domain.service.reservation.{DeskReservationService, ReservationService}

import io.circe.parser._
import io.circe.syntax._

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import org.mockito.ArgumentMatchersSugar
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import sttp.client3._
import sttp.client3.circe._
import sttp.model.StatusCode
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
      id = anyReservationId1,
      userId = reservationToCreate.userId,
      createdAt = anyCreatedAt,
      reservedFromDate = reservationToCreate.reservedFrom,
      reservedToDate = reservationToCreate.reservedTo,
      state = ReservationState.Pending,
      notes = reservationToCreate.notes,
      deskId = reservationToCreate.deskId
    )
    val reservationService = whenF(mock[DeskReservationService[IO]].reserve(any)) thenReturn reservation

    val response = sendRequest(
      reservationService,
      role = User,
      requesterAccountId = reservationToCreate.userId
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
      id = anyReservationId1,
      userId = reservationToCreate.userId,
      createdAt = anyCreatedAt,
      reservedFromDate = reservationToCreate.reservedFrom,
      reservedToDate = reservationToCreate.reservedTo,
      state = ReservationState.Pending,
      notes = reservationToCreate.notes,
      deskId = reservationToCreate.deskId
    )
    val reservationService = whenF(mock[DeskReservationService[IO]].reserve(any)) thenReturn reservation

    val response = sendRequest(
      reservationService,
      role = OfficeManager,
      requesterAccountId = anyOfficeManagerId
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
    val reservationService = mock[DeskReservationService[IO]]

    val response = sendRequest(
      reservationService,
      role = User,
      requesterAccountId = requesterId
    ) {
      basicRequest
        .post(uri"http://test.com/reservation/desk")
        .body(reservationToCreate)
    }

    for {
      response <- response
    } yield {
      verify(reservationService, never).reserve(any)
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
      whenF(mock[DeskReservationService[IO]].reserve(any)) thenFailWith DeskNotFound(anyDeskId)

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
      whenF(mock[DeskReservationService[IO]].reserve(any)) thenFailWith UserNotFound(anyUserId)

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
      whenF(mock[DeskReservationService[IO]].reserve(any)) thenFailWith OverlappingReservations

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
    val reservationId = anyReservationId1
    val reservation = anyDeskReservation.copy(id = reservationId)
    val reservationService = whenF(mock[DeskReservationService[IO]].readReservation(any)) thenReturn reservation

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
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[DeskReservationService[IO]].readReservation(any)) thenFailWith ReservationNotFound(reservationId)

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
    val reservationService = mock[DeskReservationService[IO]]
    whenF(reservationService.readReservation(any)) thenReturn reservation
    whenF(reservationService.cancelReservation(any)) thenReturn ()

    val response = sendRequest(
      reservationService,
      role = User,
      requesterAccountId = userId
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
    val reservationService = mock[DeskReservationService[IO]]
    whenF(reservationService.readReservation(any)) thenReturn anyDeskReservation
    whenF(reservationService.cancelReservation(any)) thenReturn ()

    val response = sendRequest(
      reservationService,
      role = OfficeManager,
      requesterAccountId = anyOfficeManagerId
    ) {
      basicRequest.put(uri"http://test.com/reservation/$anyReservationId1/cancel")
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
    val reservationId = anyReservationId1
    val reservationService = mock[DeskReservationService[IO]]
    whenF(reservationService.readReservation(any)) thenReturn anyDeskReservation
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
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[DeskReservationService[IO]].readReservation(any)) thenFailWith ReservationNotFound(reservationId)

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
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[DeskReservationService[IO]].confirmReservation(any)) thenReturn ()

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
    val reservationId = anyReservationId1
    val reservationService = mock[DeskReservationService[IO]]

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
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[DeskReservationService[IO]].confirmReservation(any)) thenFailWith
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
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[DeskReservationService[IO]].confirmReservation(any)) thenFailWith ReservationNotFound(reservationId)

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
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[DeskReservationService[IO]].rejectReservation(any)) thenReturn ()

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
    val reservationId = anyReservationId1
    val reservationService = mock[DeskReservationService[IO]]

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
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[DeskReservationService[IO]].rejectReservation(any)) thenFailWith
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
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[DeskReservationService[IO]].rejectReservation(any)) thenFailWith ReservationNotFound(reservationId)

    val response = sendRequest(reservationService) {
      basicRequest.put(uri"http://test.com/reservation/$reservationId/reject")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN view list desk reservation endpoint
      | WHEN the endpoint is called with filters, limit and offset
      | THEN 200 OK and the list of reservations is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ReservationViewRepository[IO]]
    val deskReservationListView = DeskReservationListView(
      reservations = List(
        anyDeskReservationView.copy(id = anyReservationId1),
        anyDeskReservationView.copy(id = anyReservationId2)
      ),
      pagination = Pagination(
        limit = 10,
        offset = 0,
        hasMoreResults = false
      )
    )
    whenF(
      reservationViewRepository.listDeskReservations(any, any, any, any, any, any)
    ) thenReturn deskReservationListView

    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/desk/view/list"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "reservation_from" -> "2024-09-24",
            "reservation_states" -> List(ApiReservationState.Pending, ApiReservationState.Confirmed)
              .map(_.entryName)
              .mkString(","),
            "user_id" -> anyUserId.toString,
            "limit" -> "10",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiDeskReservationListView.fromDomain(deskReservationListView).asJson
    )
  }

  test(
    """GIVEN view list desk reservation endpoint
      | WHEN the endpoint is called with office ID and reservedFrom
      | THEN 200 OK and the list of reservations is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ReservationViewRepository[IO]]
    val deskReservationListView = DeskReservationListView(
      reservations = List(
        anyDeskReservationView.copy(id = anyReservationId1),
        anyDeskReservationView.copy(id = anyReservationId2)
      ),
      pagination = Pagination(
        limit = 10,
        offset = 0,
        hasMoreResults = false
      )
    )
    whenF(
      reservationViewRepository.listDeskReservations(any, any, any, any, any, any)
    ) thenReturn deskReservationListView

    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/desk/view/list"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "reservation_from" -> "2024-09-24",
            "limit" -> "10",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiDeskReservationListView.fromDomain(deskReservationListView).asJson
    )
  }

  test(
    """GIVEN view list desk reservation endpoint
      | WHEN the endpoint is called without office ID
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ReservationViewRepository[IO]]
    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/desk/view/list"
          .withParams(
            "reservation_from" -> "2024-09-24",
            "limit" -> "10",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list desk reservation endpoint
      | WHEN office_id is not a valid UUID
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ReservationViewRepository[IO]]
    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/desk/view/list"
          .withParams(
            "office_id" -> "not a UUID",
            "reservation_from" -> "2024-09-24",
            "limit" -> "10",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list desk reservation endpoint
      | WHEN the endpoint is called without reservedFrom
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ReservationViewRepository[IO]]
    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/desk/view/list"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "limit" -> "10",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list desk reservation endpoint
      | WHEN reservation_from is not a valid date
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ReservationViewRepository[IO]]
    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/desk/view/list"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "reservation_from" -> "not a date",
            "limit" -> "10",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list desk reservation endpoint
      | WHEN reservation_states contains an invalid state
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ReservationViewRepository[IO]]
    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/desk/view/list"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "reservation_from" -> "2024-09-24",
            "reservation_states" -> s"${ApiReservationState.Confirmed},InvalidState",
            "limit" -> "10",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list desk reservation endpoint
      | WHEN user_id is not a valid UUID
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ReservationViewRepository[IO]]
    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/desk/view/list"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "reservation_from" -> "2024-09-24",
            "user_id" -> "not a UUID",
            "limit" -> "10",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  private def sendRequest(
    reservationService: DeskReservationService[IO],
    role: Role = SuperAdmin,
    requesterAccountId: UUID = anySuperAdminId
  )(
    request: Request[Either[String, String], Any]
  ) = {
    val reservationViewRepository = mock[ReservationViewRepository[IO]]
    sendSecuredApiEndpointRequest(request, role, requesterAccountId) { claimsExtractorService =>
      new ReservationEndpoints[IO](
        reservationService,
        reservationViewRepository,
        publicKeyRepository,
        claimsExtractorService
      ).endpoints
    }
  }

  private def sendViewRequest(
    reservationViewRepository: ReservationViewRepository[IO]
  )(
    request: Request[Either[String, String], Any]
  ): IO[Response[Either[PublicKey, PublicKey]]] = {
    val reservationService = mock[DeskReservationService[IO]]
    sendSecuredApiEndpointRequest(request, role = User, accountId = anyUserId) { claimsExtractorService =>
      new ReservationEndpoints[IO](
        reservationService,
        reservationViewRepository,
        publicKeyRepository,
        claimsExtractorService
      ).endpoints
    }
  }

  private def bodyJson(response: Response[Either[String, String]]) =
    response.body.flatMap(parse).toOption.get

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

  private lazy val anyReservationId1 = UUID.fromString("dcd64f97-a57a-4b57-9e63-dbe56187b557")
  private lazy val anyReservationId2 = UUID.fromString("2b6cae88-393a-4222-9ddd-052aff904b94")

  private lazy val anyCreatedAt = LocalDateTime.parse("2023-07-18T20:41:00")

  private lazy val anyOfficeId = UUID.fromString("9ddcf215-c54d-4368-ad5b-d6c10a76800f")

  private lazy val anyDeskReservationView = DeskReservationView(
    id = anyReservationId1,
    reservedFromDate = LocalDate.parse("2024-09-24"),
    reservedToDate = LocalDate.parse("2024-09-27"),
    state = ReservationState.Confirmed,
    notes = "Test notes",
    user = UserView(
      id = UUID.fromString("a28420c3-da66-4f87-bac3-a399bafa2756"),
      firstName = "John",
      lastName = "Doe",
      email = "john.doe@example.com"
    ),
    desk = DeskView(
      id = UUID.fromString("2215a152-cc29-4086-b5a8-7aef2a96867b"),
      name = "102.1"
    )
  )
}
