package io.github.avapl
package adapters.http.reservation

import adapters.auth.model.PublicKey
import adapters.http.fixture.SecuredApiEndpointFixture
import adapters.http.reservation.model.ApiCreateParkingSpotReservation
import adapters.http.reservation.model.ApiParkingSpotReservation
import adapters.http.reservation.model.ApiReservationState
import adapters.http.reservation.model.view.ApiParkingSpotReservationListView
import cats.effect.IO
import domain.model.account.Role
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
import domain.model.error.parkingspot.ParkingSpotNotFound
import domain.model.error.reservation.OverlappingReservations
import domain.model.error.reservation.ReservationNotFound
import domain.model.error.user.UserNotFound
import domain.model.reservation.ParkingSpotReservation
import domain.model.reservation.ReservationState
import domain.model.reservation.view.ParkingSpotReservationListView
import domain.model.reservation.view.ParkingSpotReservationView
import domain.model.reservation.view.ParkingSpotView
import domain.model.reservation.view.UserView
import domain.model.view.Pagination
import domain.repository.reservation.view.ParkingSpotReservationViewRepository
import domain.service.reservation.ParkingSpotReservationService
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

object ParkingSpotReservationEndpointsSuite
  extends SimpleIOSuite
  with MockitoSugar
  with ArgumentMatchersSugar
  with MockitoCats
  with SecuredApiEndpointFixture {

  test(
    """GIVEN reserve parking spot endpoint
      | WHEN a reservation is POSTed and created by a user
      | THEN 201 Created and the created reservation is returned
      |""".stripMargin
  ) {
    val reservationToCreate = anyApiCreateParkingSpotReservation
    val reservation = ParkingSpotReservation(
      id = anyReservationId1,
      userId = reservationToCreate.userId,
      createdAt = anyCreatedAt,
      reservedFromDate = reservationToCreate.reservedFrom,
      reservedToDate = reservationToCreate.reservedTo,
      state = ReservationState.Pending,
      notes = reservationToCreate.notes,
      parkingSpotId = reservationToCreate.parkingSpotId,
      plateNumber = reservationToCreate.plateNumber
    )
    val reservationService = whenF(mock[ParkingSpotReservationService[IO]].reserve(any)) thenReturn reservation

    val response = sendRequest(
      reservationService,
      role = User,
      requesterAccountId = reservationToCreate.userId
    ) {
      basicRequest
        .post(uri"http://test.com/reservation/parking")
        .body(reservationToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiParkingSpotReservation.fromDomain(reservation).asJson
    )
  }

  test(
    """GIVEN reserve parking spot endpoint
      | WHEN a reservation is POSTed and created by an office manager for another user
      | THEN 201 Created and the created reservation is returned
      |""".stripMargin
  ) {
    val reservationToCreate = anyApiCreateParkingSpotReservation
    val reservation = ParkingSpotReservation(
      id = anyReservationId1,
      userId = reservationToCreate.userId,
      createdAt = anyCreatedAt,
      reservedFromDate = reservationToCreate.reservedFrom,
      reservedToDate = reservationToCreate.reservedTo,
      state = ReservationState.Pending,
      notes = reservationToCreate.notes,
      parkingSpotId = reservationToCreate.parkingSpotId,
      plateNumber = reservationToCreate.plateNumber
    )
    val reservationService = whenF(mock[ParkingSpotReservationService[IO]].reserve(any)) thenReturn reservation

    val response = sendRequest(
      reservationService,
      role = OfficeManager,
      requesterAccountId = anyOfficeManagerId
    ) {
      basicRequest
        .post(uri"http://test.com/reservation/parking")
        .body(reservationToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiParkingSpotReservation.fromDomain(reservation).asJson
    )
  }

  test(
    """GIVEN reserve parking spot endpoint
      | WHEN there is an attempt to create a reservation for a user by another user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val requesterId = UUID.fromString("10edac56-4725-4c06-92bb-0e8fee647426")
    val reservationUserId = UUID.fromString("a8907e2a-621b-494b-ba56-bd0bdc02aa15")
    val reservationToCreate = anyApiCreateParkingSpotReservation.copy(userId = reservationUserId)
    val reservationService = mock[ParkingSpotReservationService[IO]]

    val response = sendRequest(
      reservationService,
      role = User,
      requesterAccountId = requesterId
    ) {
      basicRequest
        .post(uri"http://test.com/reservation/parking")
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
    """GIVEN reserve parking spot endpoint
      | WHEN creating the reservation fails with ParkingSpotNotFound error
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationService =
      whenF(mock[ParkingSpotReservationService[IO]].reserve(any)) thenFailWith ParkingSpotNotFound(anyParkingSpotId)

    val response = sendRequest(reservationService) {
      basicRequest
        .post(uri"http://test.com/reservation/parking")
        .body(anyApiCreateParkingSpotReservation)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN reserve parking spot endpoint
      | WHEN creating the reservation fails with UserNotFound error
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationService =
      whenF(mock[ParkingSpotReservationService[IO]].reserve(any)) thenFailWith UserNotFound(anyUserId)

    val response = sendRequest(reservationService) {
      basicRequest
        .post(uri"http://test.com/reservation/parking")
        .body(anyApiCreateParkingSpotReservation)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN reserve parking spot endpoint
      | WHEN creating the reservation fails with OverlappingReservations error
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val reservationService =
      whenF(mock[ParkingSpotReservationService[IO]].reserve(any)) thenFailWith OverlappingReservations

    val response = sendRequest(reservationService) {
      basicRequest
        .post(uri"http://test.com/reservation/parking")
        .body(anyApiCreateParkingSpotReservation)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN read parking spot reservation endpoint
      | WHEN an existing reservation is read by a user
      | THEN 200 OK and the read reservation is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId1
    val reservation = anyParkingSpotReservation.copy(id = reservationId)
    val reservationService = whenF(mock[ParkingSpotReservationService[IO]].readReservation(any)) thenReturn reservation

    val response = sendRequest(reservationService, role = User) {
      basicRequest.get(uri"http://test.com/reservation/parking/$reservationId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiParkingSpotReservation.fromDomain(reservation).asJson
    )
  }

  test(
    """GIVEN read parking spot reservation endpoint
      | WHEN the reservation is not found (ReservationNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val reservationId = anyReservationId1
    val reservationService =
      whenF(mock[ParkingSpotReservationService[IO]].readReservation(any)) thenFailWith
        ReservationNotFound(reservationId)

    val response = sendRequest(reservationService) {
      basicRequest.get(uri"http://test.com/reservation/parking/$reservationId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN view list parking spot reservation endpoint
      | WHEN the endpoint is called with filters, limit and offset
      | THEN 200 OK and the list of reservations is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ParkingSpotReservationViewRepository[IO]]
    val parkingSpotReservationListView = ParkingSpotReservationListView(
      reservations = List(
        anyParkingSpotReservationView.copy(id = anyReservationId1),
        anyParkingSpotReservationView.copy(id = anyReservationId2)
      ),
      pagination = Pagination(
        limit = 10,
        offset = 0,
        hasMoreResults = false
      )
    )
    whenF(
      reservationViewRepository.listParkingSpotReservations(any, any, any, any, any, any, any)
    ) thenReturn parkingSpotReservationListView

    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/parking/view/list"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "reservation_from" -> "2024-09-24",
            "reservation_to" -> "2024-09-27",
            "reservation_states" -> List(ApiReservationState.Pending, ApiReservationState.Confirmed)
              .map(_.entryName)
              .mkString(","),
            "user_id" -> anyUserId.toString,
            "plate_number" -> anyPlateNumber,
            "limit" -> "10",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiParkingSpotReservationListView.fromDomain(parkingSpotReservationListView).asJson
    )
  }

  test(
    """GIVEN view list parking spot reservation endpoint
      | WHEN the endpoint is called with office ID and reservation from date
      | THEN 200 OK and the list of reservations is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ParkingSpotReservationViewRepository[IO]]
    val parkingSpotReservationListView = ParkingSpotReservationListView(
      reservations = List(
        anyParkingSpotReservationView.copy(id = anyReservationId1),
        anyParkingSpotReservationView.copy(id = anyReservationId2)
      ),
      pagination = Pagination(
        limit = 10,
        offset = 0,
        hasMoreResults = false
      )
    )
    whenF(
      reservationViewRepository.listParkingSpotReservations(any, any, any, any, any, any, any)
    ) thenReturn parkingSpotReservationListView

    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/parking/view/list"
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
      bodyJson(response) == ApiParkingSpotReservationListView.fromDomain(parkingSpotReservationListView).asJson
    )
  }

  test(
    """GIVEN view list parking spot reservation endpoint
      | WHEN the endpoint is called without office ID
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ParkingSpotReservationViewRepository[IO]]
    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/parking/view/list"
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
    """GIVEN view list parking spot reservation endpoint
      | WHEN office_id is not a valid UUID
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ParkingSpotReservationViewRepository[IO]]
    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/parking/view/list"
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
    """GIVEN view list parking spot reservation endpoint
      | WHEN the endpoint is called without reservedFrom
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ParkingSpotReservationViewRepository[IO]]
    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/parking/view/list"
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
    """GIVEN view list parking spot reservation endpoint
      | WHEN reservation_from is not a valid date
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ParkingSpotReservationViewRepository[IO]]
    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/parking/view/list"
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
    """GIVEN view list parking spot reservation endpoint
      | WHEN reservation_states contains an invalid state
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ParkingSpotReservationViewRepository[IO]]
    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/parking/view/list"
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
    """GIVEN view list parking spot reservation endpoint
      | WHEN user_id is not a valid UUID
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val reservationViewRepository = mock[ParkingSpotReservationViewRepository[IO]]
    val response = sendViewRequest(reservationViewRepository) {
      basicRequest.get(
        uri"http://test.com/reservation/parking/view/list"
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
    reservationService: ParkingSpotReservationService[IO],
    role: Role = SuperAdmin,
    requesterAccountId: UUID = anySuperAdminId
  )(
    request: Request[Either[String, String], Any]
  ) = {
    val reservationViewRepository = mock[ParkingSpotReservationViewRepository[IO]]
    sendSecuredApiEndpointRequest(request, role, requesterAccountId) { claimsExtractorService =>
      new ParkingSpotReservationEndpoints[IO](
        reservationService,
        reservationViewRepository,
        publicKeyRepository,
        claimsExtractorService
      ).endpoints
    }
  }

  private def sendViewRequest(
    reservationViewRepository: ParkingSpotReservationViewRepository[IO]
  )(
    request: Request[Either[String, String], Any]
  ): IO[Response[Either[PublicKey, PublicKey]]] = {
    val reservationService = mock[ParkingSpotReservationService[IO]]
    sendSecuredApiEndpointRequest(request, role = User, accountId = anyUserId) { claimsExtractorService =>
      new ParkingSpotReservationEndpoints[IO](
        reservationService,
        reservationViewRepository,
        publicKeyRepository,
        claimsExtractorService
      ).endpoints
    }
  }

  private def bodyJson(response: Response[Either[String, String]]) =
    response.body.flatMap(parse).toOption.get

  private lazy val anyParkingSpotReservation = ParkingSpotReservation(
    id = anyReservationId1,
    userId = anyUserId,
    createdAt = anyCreatedAt,
    reservedFromDate = LocalDate.parse("2023-07-19"),
    reservedToDate = LocalDate.parse("2023-07-20"),
    state = ReservationState.Pending,
    notes = "Please remove the duck from the parking spot, why is it even there?",
    parkingSpotId = anyParkingSpotId,
    plateNumber = anyPlateNumber
  )

  private lazy val anyApiCreateParkingSpotReservation = ApiCreateParkingSpotReservation(
    userId = anyUserId,
    reservedFrom = LocalDate.parse("2023-07-19"),
    reservedTo = LocalDate.parse("2023-07-20"),
    notes = "Please remove the duck from the parking spot, why is it even there?",
    parkingSpotId = anyParkingSpotId,
    plateNumber = "DSW 58100"
  )

  private lazy val anyUserId = UUID.fromString("0f0cdeb7-c6f0-4f1e-93a5-b3fd34506dc5")
  private lazy val anyOfficeManagerId = UUID.fromString("5cf7279b-1c70-416e-84c0-cb7674cd3c05")
  private lazy val anySuperAdminId = UUID.fromString("53eea01d-4129-4098-9fe5-f07d768c937e")

  private lazy val anyCreatedAt = LocalDateTime.parse("2023-07-18T20:41:00")

  private lazy val anyParkingSpotId = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")

  private lazy val anyPlateNumber = "DSW 58100"

  private lazy val anyReservationId1 = UUID.fromString("dcd64f97-a57a-4b57-9e63-dbe56187b557")
  private lazy val anyReservationId2 = UUID.fromString("2b6cae88-393a-4222-9ddd-052aff904b94")

  private lazy val anyOfficeId = UUID.fromString("9ddcf215-c54d-4368-ad5b-d6c10a76800f")

  private lazy val anyParkingSpotReservationView = ParkingSpotReservationView(
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
    parkingSpot = ParkingSpotView(
      id = UUID.fromString("2215a152-cc29-4086-b5a8-7aef2a96867b"),
      name = "P1"
    ),
    plateNumber = "DSW 58100"
  )
}
