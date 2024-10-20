package io.github.avapl
package adapters.http.parkingspot

import adapters.http.fixture.SecuredApiEndpointFixture
import adapters.http.parkingspot.model.ApiCreateParkingSpot
import adapters.http.parkingspot.model.ApiParkingSpot
import adapters.http.parkingspot.model.ApiUpdateParkingSpot
import adapters.http.parkingspot.model.view.ApiParkingSpotListView
import adapters.http.parkingspot.model.view.ApiReservableParkingSpotView
import cats.effect.IO
import cats.syntax.all._
import domain.model.account.Role
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
import domain.model.error.office.OfficeNotFound
import domain.model.error.parkingspot.DuplicateParkingSpotNameForOffice
import domain.model.error.parkingspot.ParkingSpotNotFound
import domain.model.parkingspot.ParkingSpot
import domain.model.parkingspot.view.ParkingSpotListView
import domain.model.parkingspot.view.ParkingSpotView
import domain.model.parkingspot.view.ReservableParkingSpotView
import domain.model.view.Pagination
import domain.repository.parkingspot.view.ParkingSpotViewRepository
import domain.service.parkingspot.ParkingSpotService
import io.circe.parser._
import io.circe.syntax._
import java.util.UUID
import org.mockito.ArgumentMatchersSugar
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import sttp.client3._
import sttp.client3.circe._
import sttp.model.StatusCode
import weaver.SimpleIOSuite

object ParkingSpotEndpointsSuite
  extends SimpleIOSuite
  with MockitoSugar
  with ArgumentMatchersSugar
  with MockitoCats
  with SecuredApiEndpointFixture {

  test(
    """GIVEN create parking spot endpoint
      | WHEN a parking spot is POSTed and created by an office manager
      | THEN 201 Created and the created parking spot is returned
      |""".stripMargin
  ) {
    val parkingSpotToCreate = anyApiCreateParkingSpot
    val parkingSpot = ParkingSpot(
      id = anyParkingSpotId1,
      name = parkingSpotToCreate.name,
      isAvailable = parkingSpotToCreate.isAvailable,
      notes = parkingSpotToCreate.notes,
      isHandicapped = parkingSpotToCreate.isHandicapped,
      isUnderground = parkingSpotToCreate.isUnderground,
      officeId = parkingSpotToCreate.officeId
    )
    val parkingSpotService = whenF(mock[ParkingSpotService[IO]].createParkingSpot(any)) thenReturn parkingSpot

    val response = sendRequest(parkingSpotService) {
      basicRequest
        .post(uri"http://test.com/parking")
        .body(parkingSpotToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiParkingSpot.fromDomain(parkingSpot).asJson
    )
  }

  test(
    """GIVEN create parking spot endpoint
      | WHEN there is an attempt to create a parking spot by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val parkingSpotToCreate = anyApiCreateParkingSpot
    val parkingSpotService = mock[ParkingSpotService[IO]]

    val response = sendRequest(parkingSpotService, role = User) {
      basicRequest
        .post(uri"http://test.com/parking")
        .body(parkingSpotToCreate)
    }

    for {
      response <- response
    } yield {
      verify(parkingSpotService, never).createParkingSpot(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN create parking spot endpoint
      | WHEN creating the parking spot fails with OfficeNotFound error
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val parkingSpotService =
      whenF(mock[ParkingSpotService[IO]].createParkingSpot(any)) thenFailWith OfficeNotFound(anyOfficeId)

    val response = sendRequest(parkingSpotService) {
      basicRequest
        .post(uri"http://test.com/parking")
        .body(anyApiCreateParkingSpot)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN create parking spot endpoint
      | WHEN creating the parking spot fails with DuplicateParkingSpotNameForOffice error
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val parkingSpotService =
      whenF(mock[ParkingSpotService[IO]].createParkingSpot(any)) thenFailWith
        DuplicateParkingSpotNameForOffice(anyParkingSpotName.some, anyOfficeId.some)

    val response = sendRequest(parkingSpotService) {
      basicRequest
        .post(uri"http://test.com/parking")
        .body(anyApiCreateParkingSpot)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN read parking spot endpoint
      | WHEN an existing parking spot is read by a user
      | THEN 200 OK and the read parking spot is returned
      |""".stripMargin
  ) {
    val parkingSpotId = anyParkingSpotId1
    val parkingSpot = anyParkingSpot.copy(id = parkingSpotId)
    val parkingSpotService = whenF(mock[ParkingSpotService[IO]].readParkingSpot(any)) thenReturn parkingSpot

    val response = sendRequest(parkingSpotService) {
      basicRequest.get(uri"http://test.com/parking/$parkingSpotId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiParkingSpot.fromDomain(parkingSpot).asJson
    )
  }

  test(
    """GIVEN read parking spot endpoint
      | WHEN the parking spot is not found (ParkingSpotNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val parkingSpotId = anyParkingSpotId1
    val parkingSpotService =
      whenF(mock[ParkingSpotService[IO]].readParkingSpot(any)) thenFailWith ParkingSpotNotFound(parkingSpotId)

    val response = sendRequest(parkingSpotService) {
      basicRequest.get(uri"http://test.com/parking/$parkingSpotId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN update parking spot endpoint
      | WHEN a parking spot is PATCHed and updated
      | THEN 200 OK and the updated parking spot is returned
      |""".stripMargin
  ) {
    val parkingSpotToUpdate = anyApiUpdateParkingSpot
    val parkingSpotId = anyParkingSpotId1
    val parkingSpot = ParkingSpot(
      id = anyParkingSpotId1,
      name = parkingSpotToUpdate.name.get,
      isAvailable = parkingSpotToUpdate.isAvailable.get,
      notes = parkingSpotToUpdate.notes.get,
      isHandicapped = parkingSpotToUpdate.isHandicapped.get,
      isUnderground = parkingSpotToUpdate.isUnderground.get,
      officeId = parkingSpotToUpdate.officeId.get
    )
    val parkingSpotService = whenF(mock[ParkingSpotService[IO]].updateParkingSpot(any, any)) thenReturn parkingSpot

    val response = sendRequest(parkingSpotService) {
      basicRequest
        .patch(uri"http://test.com/parking/$parkingSpotId")
        .body(parkingSpotToUpdate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiParkingSpot.fromDomain(parkingSpot).asJson
    )
  }

  test(
    """GIVEN update parking spot endpoint
      | WHEN there is an attempt to update a parking spot by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val parkingSpotToUpdate = anyApiUpdateParkingSpot
    val parkingSpotId = anyParkingSpotId1
    val parkingSpotService = mock[ParkingSpotService[IO]]

    val response = sendRequest(parkingSpotService, role = User) {
      basicRequest
        .patch(uri"http://test.com/parking/$parkingSpotId")
        .body(parkingSpotToUpdate)
    }

    for {
      response <- response
    } yield {
      verify(parkingSpotService, never).updateParkingSpot(any, any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN update parking spot endpoint
      | WHEN the related office is not found (OfficeNotFound error)
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val parkingSpotService =
      whenF(mock[ParkingSpotService[IO]].updateParkingSpot(any, any)) thenFailWith OfficeNotFound(anyOfficeId)

    val response = sendRequest(parkingSpotService) {
      basicRequest
        .patch(uri"http://test.com/parking/$anyParkingSpotId1")
        .body(anyApiUpdateParkingSpot)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN update parking spot endpoint
      | WHEN the parking spot to update is not found (ParkingSpotNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val parkingSpotId = anyParkingSpotId1
    val parkingSpotService =
      whenF(mock[ParkingSpotService[IO]].updateParkingSpot(any, any)) thenFailWith ParkingSpotNotFound(parkingSpotId)

    val response = sendRequest(parkingSpotService) {
      basicRequest
        .patch(uri"http://test.com/parking/$parkingSpotId")
        .body(anyApiUpdateParkingSpot)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN update parking spot endpoint
      | WHEN a parking spot with the given name already exists for the given office
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val parkingSpotService = whenF(mock[ParkingSpotService[IO]].updateParkingSpot(any, any)) thenFailWith
      DuplicateParkingSpotNameForOffice(anyParkingSpotName.some, anyOfficeId.some)

    val response = sendRequest(parkingSpotService) {
      basicRequest
        .patch(uri"http://test.com/parking/$anyParkingSpotId1")
        .body(anyApiUpdateParkingSpot)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN archive parking spot endpoint
      | WHEN an existing parking spot is archived
      | THEN 204 NoContent is returned
      |""".stripMargin
  ) {
    val parkingSpotService = whenF(mock[ParkingSpotService[IO]].archiveParkingSpot(any)) thenReturn ()

    val response = sendRequest(parkingSpotService) {
      basicRequest.delete(uri"http://test.com/parking/$anyParkingSpotId1")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NoContent)
  }

  test(
    """GIVEN archive parking spot endpoint
      | WHEN there is an attempt to archive a parking spot by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val parkingSpotService = mock[ParkingSpotService[IO]]

    val response = sendRequest(parkingSpotService, role = User) {
      basicRequest.delete(uri"http://test.com/parking/$anyParkingSpotId1")
    }

    for {
      response <- response
    } yield {
      verify(parkingSpotService, never).archiveParkingSpot(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN view list parking spot endpoint
      | WHEN the endpoint is called with office ID
      | THEN 200 OK and the list of parking spots is returned
      |""".stripMargin
  ) {
    val parkingSpotViewRepository = mock[ParkingSpotViewRepository[IO]]
    val parkingSpotListView = ParkingSpotListView(
      parkingSpots = List(
        anyParkingSpotView.copy(id = anyParkingSpotId1, name = "spot1"),
        anyParkingSpotView.copy(id = anyParkingSpotId2, name = "spot2")
      ),
      pagination = Pagination(
        limit = 10,
        offset = 0,
        hasMoreResults = false
      )
    )
    whenF(parkingSpotViewRepository.listParkingSpots(any, any, any)) thenReturn parkingSpotListView

    val response = sendViewRequest(parkingSpotViewRepository) {
      basicRequest.get(
        uri"http://test.com/parking/view/list"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "limit" -> "10",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiParkingSpotListView.fromDomain(parkingSpotListView).asJson
    )
  }

  test(
    """GIVEN view list parking spot endpoint
      | WHEN the endpoint is called without office_id
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val parkingSpotViewRepository = mock[ParkingSpotViewRepository[IO]]
    val response = sendViewRequest(parkingSpotViewRepository) {
      basicRequest.get(
        uri"http://test.com/parking/view/list"
          .withParams(
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
    """GIVEN view list parking spot endpoint
      | WHEN office_id is not a valid UUID
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val parkingSpotViewRepository = mock[ParkingSpotViewRepository[IO]]
    val response = sendViewRequest(parkingSpotViewRepository) {
      basicRequest.get(
        uri"http://test.com/parking/view/list"
          .withParams(
            "office_id" -> "not a UUID",
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
    """GIVEN reservable parking spots view list endpoint
      | WHEN the endpoint is called with office ID and reservation range
      | THEN 200 OK and the list of reservable parking spots is returned
      |""".stripMargin
  ) {
    val parkingSpotViewRepository = mock[ParkingSpotViewRepository[IO]]
    val reservableParkingSpotViews = List(
      ReservableParkingSpotView(
        id = anyParkingSpotId1,
        name = "P1",
        isHandicapped = true,
        isUnderground = false
      ),
      ReservableParkingSpotView(
        id = anyParkingSpotId2,
        name = "P2",
        isHandicapped = false,
        isUnderground = true
      )
    )
    whenF(
      parkingSpotViewRepository.listParkingSpotsAvailableForReservation(any, any, any)
    ) thenReturn reservableParkingSpotViews

    val response = sendViewRequest(parkingSpotViewRepository) {
      basicRequest.get(
        uri"http://test.com/parking/view/reservable"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "reservation_from" -> "2024-09-24",
            "reservation_to" -> "2024-09-27"
          )
      )
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == reservableParkingSpotViews.map(ApiReservableParkingSpotView.fromDomain).asJson
    )
  }

  test(
    """GIVEN reservable parking spots view list endpoint
      | WHEN the endpoint is called without office_id
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val parkingSpotViewRepository = mock[ParkingSpotViewRepository[IO]]
    val response = sendViewRequest(parkingSpotViewRepository) {
      basicRequest.get(
        uri"http://test.com/parking/view/reservable"
          .withParams(
            "reservation_from" -> "2024-09-24",
            "reservation_to" -> "2024-09-27"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN reservable parking spots view list endpoint
      | WHEN office_id is not a valid UUID
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val parkingSpotViewRepository = mock[ParkingSpotViewRepository[IO]]
    val response = sendViewRequest(parkingSpotViewRepository) {
      basicRequest.get(
        uri"http://test.com/parking/view/reservable"
          .withParams(
            "office_id" -> "not a UUID",
            "reservation_from" -> "2024-09-24",
            "reservation_to" -> "2024-09-27"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN reservable parking spots view list endpoint
      | WHEN the endpoint is called without reservation_from
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val parkingSpotViewRepository = mock[ParkingSpotViewRepository[IO]]
    val response = sendViewRequest(parkingSpotViewRepository) {
      basicRequest.get(
        uri"http://test.com/parking/view/reservable"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "reservation_to" -> "2024-09-27"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN reservable parking spots view list endpoint
      | WHEN reservation_from is not a valid date
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val parkingSpotViewRepository = mock[ParkingSpotViewRepository[IO]]
    val response = sendViewRequest(parkingSpotViewRepository) {
      basicRequest.get(
        uri"http://test.com/parking/view/reservable"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "reservation_from" -> "not a date",
            "reservation_to" -> "2024-09-27"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN reservable parking spots view list endpoint
      | WHEN the endpoint is called without reservation_to
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val parkingSpotViewRepository = mock[ParkingSpotViewRepository[IO]]
    val response = sendViewRequest(parkingSpotViewRepository) {
      basicRequest.get(
        uri"http://test.com/parking/view/reservable"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "reservation_from" -> "2024-09-24"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN reservable parking spots view list endpoint
      | WHEN reservation_to is not a valid date
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val parkingSpotViewRepository = mock[ParkingSpotViewRepository[IO]]
    val response = sendViewRequest(parkingSpotViewRepository) {
      basicRequest.get(
        uri"http://test.com/parking/view/reservable"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "reservation_from" -> "2024-09-24",
            "reservation_to" -> "not a date"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  private def sendRequest(parkingSpotService: ParkingSpotService[IO], role: Role = SuperAdmin)(
    request: Request[Either[String, String], Any]
  ) = {
    val parkingSpotViewRepository = mock[ParkingSpotViewRepository[IO]]
    sendSecuredApiEndpointRequest(request, role) { claimsExtractorService =>
      new ParkingSpotEndpoints[IO](
        parkingSpotService,
        parkingSpotViewRepository,
        publicKeyRepository,
        claimsExtractorService
      ).endpoints
    }
  }

  private def sendViewRequest(
    parkingSpotViewRepository: ParkingSpotViewRepository[IO]
  )(
    request: Request[Either[String, String], Any]
  ) =
    sendSecuredApiEndpointRequest(request, role = User) { claimsExtractorService =>
      new ParkingSpotEndpoints[IO](
        mock[ParkingSpotService[IO]],
        parkingSpotViewRepository,
        publicKeyRepository,
        claimsExtractorService
      ).endpoints
    }

  private def bodyJson(response: Response[Either[String, String]]) =
    response.body.flatMap(parse).toOption.get

  private lazy val anyParkingSpot = ParkingSpot(
    id = anyParkingSpotId1,
    name = anyParkingSpotName,
    isAvailable = true,
    notes = List("Near the entrance"),
    isHandicapped = true,
    isUnderground = false,
    officeId = anyOfficeId
  )

  private lazy val anyApiCreateParkingSpot = ApiCreateParkingSpot(
    name = anyParkingSpotName,
    isAvailable = true,
    notes = List("Near the entrance"),
    isHandicapped = true,
    isUnderground = false,
    officeId = anyOfficeId
  )

  private lazy val anyApiUpdateParkingSpot = ApiUpdateParkingSpot(
    name = Some(anyParkingSpotName),
    isAvailable = Some(true),
    notes = Some(List("Near the entrance")),
    isHandicapped = Some(true),
    isUnderground = Some(false),
    officeId = Some(anyOfficeId),
    isArchived = Some(false)
  )

  private lazy val anyParkingSpotId1 = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")
  private lazy val anyParkingSpotId2 = UUID.fromString("22b67357-d8c8-4c11-a8e5-d64c213db3b3")

  private lazy val anyParkingSpotName = "P1"

  private lazy val anyOfficeId: UUID = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298")

  private lazy val anyParkingSpotView = ParkingSpotView(
    id = anyParkingSpotId1,
    name = anyParkingSpotName,
    isAvailable = true,
    isHandicapped = true,
    isUnderground = false
  )
}
