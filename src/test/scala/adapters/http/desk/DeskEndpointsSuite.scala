package io.github.avapl
package adapters.http.desk

import adapters.http.desk.view.ApiDeskListView
import adapters.http.fixture.SecuredApiEndpointFixture
import cats.effect.IO
import domain.model.account.Role
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
import domain.model.desk.Desk
import domain.model.desk.view.DeskListView
import domain.model.desk.view.DeskView
import domain.model.error.desk.DeskNotFound
import domain.model.error.desk.DuplicateDeskNameForOffice
import domain.model.error.office.OfficeNotFound
import domain.model.view.Pagination
import domain.repository.desk.view.DeskViewRepository
import domain.service.desk.DeskService
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

object DeskEndpointsSuite
  extends SimpleIOSuite
  with MockitoSugar
  with ArgumentMatchersSugar
  with MockitoCats
  with SecuredApiEndpointFixture {

  test(
    """GIVEN create desk endpoint
      | WHEN a desk is POSTed and created by an office manager
      | THEN 201 Created and the created desk is returned
      |""".stripMargin
  ) {
    val deskToCreate = anyApiCreateDesk
    val desk = Desk(
      id = anyDeskId1,
      name = deskToCreate.name,
      isAvailable = deskToCreate.isAvailable,
      notes = deskToCreate.notes,
      isStanding = deskToCreate.isStanding,
      monitorsCount = deskToCreate.monitorsCount,
      hasPhone = deskToCreate.hasPhone,
      officeId = deskToCreate.officeId
    )
    val deskService = whenF(mock[DeskService[IO]].createDesk(any)) thenReturn desk

    val response = sendRequest(deskService) {
      basicRequest
        .post(uri"http://test.com/desk")
        .body(deskToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiDesk.fromDomain(desk).asJson
    )
  }

  test(
    """GIVEN create desk endpoint
      | WHEN there is an attempt to create a desk by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val deskToCreate = anyApiCreateDesk
    val deskService = mock[DeskService[IO]]

    val response = sendRequest(deskService, role = User) {
      basicRequest
        .post(uri"http://test.com/desk")
        .body(deskToCreate)
    }

    for {
      response <- response
    } yield {
      verify(deskService, never).createDesk(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN create desk endpoint
      | WHEN creating the desk fails with OfficeNotFound error
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val deskService =
      whenF(mock[DeskService[IO]].createDesk(any)) thenFailWith OfficeNotFound(anyOfficeId)

    val response = sendRequest(deskService) {
      basicRequest
        .post(uri"http://test.com/desk")
        .body(anyApiCreateDesk)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN create desk endpoint
      | WHEN creating the desk fails with DuplicateDeskNameForOffice error
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val deskService =
      whenF(mock[DeskService[IO]].createDesk(any)) thenFailWith DuplicateDeskNameForOffice(anyDeskName, anyOfficeId)

    val response = sendRequest(deskService) {
      basicRequest
        .post(uri"http://test.com/desk")
        .body(anyApiCreateDesk)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN read desk endpoint
      | WHEN an existing desk is read by a user
      | THEN 200 OK and the read desk is returned
      |""".stripMargin
  ) {
    val deskId = anyDeskId1
    val desk = anyDesk.copy(id = deskId)
    val deskService = whenF(mock[DeskService[IO]].readDesk(any)) thenReturn desk

    val response = sendRequest(deskService) {
      basicRequest.get(uri"http://test.com/desk/$deskId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiDesk.fromDomain(desk).asJson
    )
  }

  test(
    """GIVEN read desk endpoint
      | WHEN the desk is not found (DeskNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val deskId = anyDeskId1
    val deskService = whenF(mock[DeskService[IO]].readDesk(any)) thenFailWith DeskNotFound(deskId)

    val response = sendRequest(deskService) {
      basicRequest.get(uri"http://test.com/desk/$deskId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN update desk endpoint
      | WHEN a desk is PATCHed and updated
      | THEN 200 OK and the updated desk is returned
      |""".stripMargin
  ) {
    val deskToUpdate = anyApiUpdateDesk
    val deskId = anyDeskId1
    val desk = Desk(
      id = anyDeskId1,
      name = deskToUpdate.name,
      isAvailable = deskToUpdate.isAvailable,
      notes = deskToUpdate.notes,
      isStanding = deskToUpdate.isStanding,
      monitorsCount = deskToUpdate.monitorsCount,
      hasPhone = deskToUpdate.hasPhone,
      officeId = deskToUpdate.officeId
    )
    val deskService = whenF(mock[DeskService[IO]].updateDesk(any, any)) thenReturn desk

    val response = sendRequest(deskService) {
      basicRequest
        .patch(uri"http://test.com/desk/$deskId")
        .body(deskToUpdate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiDesk.fromDomain(desk).asJson
    )
  }

  test(
    """GIVEN update desk endpoint
      | WHEN there is an attempt to update a desk by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val deskToUpdate = anyApiUpdateDesk
    val deskId = anyDeskId1
    val deskService = mock[DeskService[IO]]

    val response = sendRequest(deskService, role = User) {
      basicRequest
        .patch(uri"http://test.com/desk/$deskId")
        .body(deskToUpdate)
    }

    for {
      response <- response
    } yield {
      verify(deskService, never).updateDesk(any, any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN update desk endpoint
      | WHEN the related office is not found (OfficeNotFound error)
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val deskService = whenF(mock[DeskService[IO]].updateDesk(any, any)) thenFailWith OfficeNotFound(anyOfficeId)

    val response = sendRequest(deskService) {
      basicRequest
        .patch(uri"http://test.com/desk/$anyDeskId1")
        .body(anyApiUpdateDesk)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN update desk endpoint
      | WHEN the desk to update is not found (DeskNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val deskId = anyDeskId1
    val deskService = whenF(mock[DeskService[IO]].updateDesk(any, any)) thenFailWith DeskNotFound(deskId)

    val response = sendRequest(deskService) {
      basicRequest
        .patch(uri"http://test.com/desk/$deskId")
        .body(anyApiUpdateDesk)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN update desk endpoint
      | WHEN a desk with the given name already exists for the given office
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val deskService = whenF(mock[DeskService[IO]].updateDesk(any, any)) thenFailWith
      DuplicateDeskNameForOffice(anyDeskName, anyOfficeId)

    val response = sendRequest(deskService) {
      basicRequest
        .patch(uri"http://test.com/desk/$anyDeskId1")
        .body(anyApiUpdateDesk)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN archive desk endpoint
      | WHEN an existing desk is archived
      | THEN 204 NoContent is returned
      |""".stripMargin
  ) {
    val deskService = whenF(mock[DeskService[IO]].archiveDesk(any)) thenReturn ()

    val response = sendRequest(deskService) {
      basicRequest.delete(uri"http://test.com/desk/$anyDeskId1")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NoContent)
  }

  test(
    """GIVEN archive desk endpoint
      | WHEN there is an attempt to archive a desk by a user
      | THEN 403 Forbidden is returned
      |""".stripMargin
  ) {
    val deskService = mock[DeskService[IO]]

    val response = sendRequest(deskService, role = User) {
      basicRequest.delete(uri"http://test.com/desk/$anyDeskId1")
    }

    for {
      response <- response
    } yield {
      verify(deskService, never).archiveDesk(any)
      expect(response.code == StatusCode.Forbidden)
    }
  }

  test(
    """GIVEN view list desk endpoint
      | WHEN the endpoint is called with office ID
      | THEN 200 OK and the list of desks is returned
      |""".stripMargin
  ) {
    val deskViewRepository = mock[DeskViewRepository[IO]]
    val deskListView = DeskListView(
      desks = List(
        anyDeskView.copy(id = anyDeskId1, name = "desk1"),
        anyDeskView.copy(id = anyDeskId2, name = "desk2")
      ),
      pagination = Pagination(
        limit = 10,
        offset = 0,
        hasMoreResults = false
      )
    )
    whenF(deskViewRepository.listDesks(any, any, any)) thenReturn deskListView

    val response = sendViewRequest(deskViewRepository) {
      basicRequest.get(
        uri"http://test.com/desk/view/list"
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
      bodyJson(response) == ApiDeskListView.fromDomain(deskListView).asJson
    )
  }

  test(
    """GIVEN view list desk endpoint
      | WHEN office_id is not a valid UUID
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val deskViewRepository = mock[DeskViewRepository[IO]]
    val response = sendViewRequest(deskViewRepository) {
      basicRequest.get(
        uri"http://test.com/desk/view/list"
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
    """GIVEN view list desk endpoint
      | WHEN the endpoint is called with limit less than 1
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val deskViewRepository = mock[DeskViewRepository[IO]]
    val response = sendViewRequest(deskViewRepository) {
      basicRequest.get(
        uri"http://test.com/desk/view/list"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "limit" -> "0",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list desk endpoint
      | WHEN the endpoint is called with negative offset
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val deskViewRepository = mock[DeskViewRepository[IO]]
    val response = sendViewRequest(deskViewRepository) {
      basicRequest.get(
        uri"http://test.com/desk/view/list"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "limit" -> "10",
            "offset" -> "-1"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list desk endpoint
      | WHEN the endpoint is called with limit that is not a number
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val deskViewRepository = mock[DeskViewRepository[IO]]
    val response = sendViewRequest(deskViewRepository) {
      basicRequest.get(
        uri"http://test.com/desk/view/list"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "limit" -> "notANumber",
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list desk endpoint
      | WHEN the endpoint is called with offset that is not a number
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val deskViewRepository = mock[DeskViewRepository[IO]]
    val response = sendViewRequest(deskViewRepository) {
      basicRequest.get(
        uri"http://test.com/desk/view/list"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "limit" -> "10",
            "offset" -> "notANumber"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list desk endpoint
      | WHEN the endpoint is called without office_id
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val deskViewRepository = mock[DeskViewRepository[IO]]
    val response = sendViewRequest(deskViewRepository) {
      basicRequest.get(
        uri"http://test.com/desk/view/list"
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
    """GIVEN view list desk endpoint
      | WHEN the endpoint is called without limit
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val deskViewRepository = mock[DeskViewRepository[IO]]
    val response = sendViewRequest(deskViewRepository) {
      basicRequest.get(
        uri"http://test.com/desk/view/list"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "offset" -> "0"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN view list desk endpoint
      | WHEN the endpoint is called without offset
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val deskViewRepository = mock[DeskViewRepository[IO]]
    val response = sendViewRequest(deskViewRepository) {
      basicRequest.get(
        uri"http://test.com/desk/view/list"
          .withParams(
            "office_id" -> anyOfficeId.toString,
            "limit" -> "10"
          )
      )
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  private def sendRequest(deskService: DeskService[IO], role: Role = SuperAdmin)(
    request: Request[Either[String, String], Any]
  ) = {
    val deskViewRepository = mock[DeskViewRepository[IO]]
    sendSecuredApiEndpointRequest(request, role) { claimsExtractorService =>
      new DeskEndpoints[IO](deskService, deskViewRepository, publicKeyRepository, claimsExtractorService).endpoints
    }
  }

  private def sendViewRequest(
    deskViewRepository: DeskViewRepository[IO]
  )(
    request: Request[Either[String, String], Any]
  ) =
    sendSecuredApiEndpointRequest(request, role = User) { claimsExtractorService =>
      new DeskEndpoints[IO](
        mock[DeskService[IO]],
        deskViewRepository,
        publicKeyRepository,
        claimsExtractorService
      ).endpoints
    }

  private def bodyJson(response: Response[Either[String, String]]) =
    response.body.flatMap(parse).toOption.get

  private lazy val anyDesk = Desk(
    id = anyDeskId1,
    name = anyDeskName,
    isAvailable = true,
    notes = List("Rubik's Cube on the desk"),
    isStanding = true,
    monitorsCount = 2,
    hasPhone = false,
    officeId = anyOfficeId
  )

  private lazy val anyApiCreateDesk = ApiCreateDesk(
    name = anyDeskName,
    isAvailable = true,
    notes = List("Rubik's Cube on the desk"),
    isStanding = true,
    monitorsCount = 2,
    hasPhone = false,
    officeId = anyOfficeId
  )

  private lazy val anyApiUpdateDesk = ApiUpdateDesk(
    name = anyDeskName,
    isAvailable = true,
    notes = List("Rubik's Cube on the desk"),
    isStanding = true,
    monitorsCount = 2,
    hasPhone = false,
    officeId = anyOfficeId,
    isArchived = false
  )

  private lazy val anyDeskId1 = UUID.fromString("e6fd42f1-61cd-4ee7-b436-e24bc84f9d2b")
  private lazy val anyDeskId2 = UUID.fromString("22b67357-d8c8-4c11-a8e5-d64c213db3b3")

  private lazy val anyDeskName = "107.1"

  private lazy val anyOfficeId: UUID = UUID.fromString("4f840b82-63c1-4eb7-8184-d46e49227298")

  private lazy val anyDeskView = DeskView(
    id = anyDeskId1,
    name = anyDeskName,
    isAvailable = true,
    isStanding = true,
    monitorsCount = 2,
    hasPhone = true
  )
}
