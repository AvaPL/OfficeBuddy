package io.github.avapl
package adapters.http.account

import cats.effect.IO
import domain.model.account.OfficeManagerAccount
import domain.model.account.SuperAdminAccount
import domain.model.account.UserAccount
import domain.model.error.account.AccountNotFound
import domain.model.error.account.DuplicateAccountEmail
import domain.model.error.office.OfficeNotFound
import domain.service.account.AccountService
import io.circe.parser._
import io.circe.syntax._
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

object AccountEndpointsSuite extends SimpleIOSuite with MockitoSugar with ArgumentMatchersSugar with MockitoCats {

  test(
    """GIVEN create user endpoint
      | WHEN a user is POSTed and created
      | THEN 201 Created and the created user is returned
      |""".stripMargin
  ) {
    val userToCreate = anyApiCreateUserAccount
    val user = UserAccount(
      id = anyAccountId,
      firstName = userToCreate.firstName,
      lastName = userToCreate.lastName,
      email = userToCreate.email,
      assignedOfficeId = userToCreate.assignedOfficeId
    )
    val accountService = whenF(mock[AccountService[IO]].createUser(any)) thenReturn user

    val response = sendRequest(accountService) {
      basicRequest
        .post(uri"http://test.com/account/user")
        .body(userToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiUserAccount.fromDomain(user).asJson
    )
  }

  test(
    """GIVEN create user endpoint
      | WHEN creating the user fail with OfficeNotFound error
      | THEN 400 BadRequest is returned
      |""".stripMargin
  ) {
    val accountService = whenF(mock[AccountService[IO]].createUser(any)) thenFailWith OfficeNotFound(anyOfficeId)

    val response = sendRequest(accountService) {
      basicRequest
        .post(uri"http://test.com/account/user")
        .body(anyApiCreateUserAccount)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.BadRequest)
  }

  test(
    """GIVEN create user endpoint
      | WHEN creating the user fail with DuplicateAccountEmail error
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val accountService = whenF(mock[AccountService[IO]].createUser(any)) thenFailWith DuplicateAccountEmail(anyEmail)

    val response = sendRequest(accountService) {
      basicRequest
        .post(uri"http://test.com/account/user")
        .body(anyApiCreateUserAccount)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN read user endpoint
      | WHEN an existing user is read
      | THEN 200 OK and the read user is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId
    val user = anyUserAccount.copy(id = userId)
    val accountService = whenF(mock[AccountService[IO]].readUser(any)) thenReturn user

    val response = sendRequest(accountService) {
      basicRequest.get(uri"http://test.com/account/user/$userId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiUserAccount.fromDomain(user).asJson
    )
  }

  test(
    """GIVEN read user endpoint
      | WHEN the user is not found (AccountNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val userId = anyAccountId
    val accountService = whenF(mock[AccountService[IO]].readUser(any)) thenFailWith AccountNotFound(userId)

    val response = sendRequest(accountService) {
      basicRequest.get(uri"http://test.com/account/user/$userId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN create office manager endpoint
      | WHEN a office manager is POSTed and created
      | THEN 201 Created and the created office manager is returned
      |""".stripMargin
  ) {
    val officeManagerToCreate = anyApiCreateOfficeManagerAccount
    val officeManager = OfficeManagerAccount(
      id = anyAccountId,
      firstName = officeManagerToCreate.firstName,
      lastName = officeManagerToCreate.lastName,
      email = officeManagerToCreate.email,
      managedOfficeIds = officeManagerToCreate.managedOfficeIds
    )
    val accountService = whenF(mock[AccountService[IO]].createOfficeManager(any)) thenReturn officeManager

    val response = sendRequest(accountService) {
      basicRequest
        .post(uri"http://test.com/account/office-manager")
        .body(officeManagerToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiOfficeManagerAccount.fromDomain(officeManager).asJson
    )
  }

  test(
    """GIVEN create office manager endpoint
      | WHEN creating the office manager fail with DuplicateAccountEmail error
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val accountService =
      whenF(mock[AccountService[IO]].createOfficeManager(any)) thenFailWith DuplicateAccountEmail(anyEmail)

    val response = sendRequest(accountService) {
      basicRequest
        .post(uri"http://test.com/account/office-manager")
        .body(anyApiCreateOfficeManagerAccount)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN read office manager endpoint
      | WHEN an existing office manager is read
      | THEN 200 OK and the read office manager is returned
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId
    val officeManager = anyOfficeManagerAccount.copy(id = officeManagerId)
    val accountService = whenF(mock[AccountService[IO]].readOfficeManager(any)) thenReturn officeManager

    val response = sendRequest(accountService) {
      basicRequest.get(uri"http://test.com/account/office-manager/$officeManagerId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiOfficeManagerAccount.fromDomain(officeManager).asJson
    )
  }

  test(
    """GIVEN read office manager endpoint
      | WHEN the office manager is not found (AccountNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId
    val accountService =
      whenF(mock[AccountService[IO]].readOfficeManager(any)) thenFailWith AccountNotFound(officeManagerId)

    val response = sendRequest(accountService) {
      basicRequest.get(uri"http://test.com/account/office-manager/$officeManagerId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  test(
    """GIVEN create super admin endpoint
      | WHEN a super admin is POSTed and created
      | THEN 201 Created and the created super admin is returned
      |""".stripMargin
  ) {
    val superAdminToCreate = anyApiCreateSuperAdminAccount
    val superAdmin = SuperAdminAccount(
      id = anyAccountId,
      firstName = superAdminToCreate.firstName,
      lastName = superAdminToCreate.lastName,
      email = superAdminToCreate.email
    )
    val accountService = whenF(mock[AccountService[IO]].createSuperAdmin(any)) thenReturn superAdmin

    val response = sendRequest(accountService) {
      basicRequest
        .post(uri"http://test.com/account/super-admin")
        .body(superAdminToCreate)
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Created,
      bodyJson(response) == ApiSuperAdminAccount.fromDomain(superAdmin).asJson
    )
  }

  test(
    """GIVEN create super admin endpoint
      | WHEN creating the super admin fail with DuplicateAccountEmail error
      | THEN 409 Conflict is returned
      |""".stripMargin
  ) {
    val accountService =
      whenF(mock[AccountService[IO]].createSuperAdmin(any)) thenFailWith DuplicateAccountEmail(anyEmail)

    val response = sendRequest(accountService) {
      basicRequest
        .post(uri"http://test.com/account/super-admin")
        .body(anyApiCreateSuperAdminAccount)
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.Conflict)
  }

  test(
    """GIVEN read super admin endpoint
      | WHEN an existing super admin is read
      | THEN 200 OK and the read super admin is returned
      |""".stripMargin
  ) {
    val superAdminId = anyAccountId
    val superAdmin = anySuperAdminAccount.copy(id = superAdminId)
    val accountService = whenF(mock[AccountService[IO]].readSuperAdmin(any)) thenReturn superAdmin

    val response = sendRequest(accountService) {
      basicRequest.get(uri"http://test.com/account/super-admin/$superAdminId")
    }

    for {
      response <- response
    } yield expect.all(
      response.code == StatusCode.Ok,
      bodyJson(response) == ApiSuperAdminAccount.fromDomain(superAdmin).asJson
    )
  }

  test(
    """GIVEN read super admin endpoint
      | WHEN the super admin is not found (AccountNotFound error)
      | THEN 404 NotFound is returned
      |""".stripMargin
  ) {
    val superAdminId = anyAccountId
    val accountService =
      whenF(mock[AccountService[IO]].readSuperAdmin(any)) thenFailWith AccountNotFound(superAdminId)

    val response = sendRequest(accountService) {
      basicRequest.get(uri"http://test.com/account/super-admin/$superAdminId")
    }

    for {
      response <- response
    } yield expect(response.code == StatusCode.NotFound)
  }

  private def sendRequest(accountService: AccountService[IO])(request: Request[Either[String, String], Any]) = {
    val accountEndpoints = new AccountEndpoints[IO](accountService)
    val backendStub = TapirStubInterpreter(SttpBackendStub(new CatsMonadError[IO]))
      .whenServerEndpointsRunLogic(accountEndpoints.endpoints)
      .backend()
    request.send(backendStub)
  }

  private def bodyJson(response: Response[Either[String, String]]) =
    response.body.flatMap(parse).toOption.get

  private lazy val anyUserAccount = UserAccount(
    id = anyAccountId,
    firstName = "John",
    lastName = "Doe",
    email = anyEmail,
    assignedOfficeId = Some(anyOfficeId)
  )

  private lazy val anyApiCreateUserAccount = ApiCreateUserAccount(
    firstName = "John",
    lastName = "Doe",
    email = anyEmail,
    assignedOfficeId = Some(anyOfficeId)
  )

  private lazy val anyOfficeManagerAccount = OfficeManagerAccount(
    id = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40"),
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com",
    managedOfficeIds = List(
      UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed"),
      UUID.fromString("305bfad9-9354-4d7f-93ef-c1bab1f8dd7b")
    )
  )

  private lazy val anyApiCreateOfficeManagerAccount = ApiCreateOfficeManagerAccount(
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com",
    managedOfficeIds = List(
      UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed"),
      UUID.fromString("305bfad9-9354-4d7f-93ef-c1bab1f8dd7b")
    )
  )

  private lazy val anySuperAdminAccount = SuperAdminAccount(
    id = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40"),
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com"
  )

  private lazy val anyApiCreateSuperAdminAccount = ApiCreateSuperAdminAccount(
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com"
  )

  private lazy val anyAccountId: UUID = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40")

  private lazy val anyEmail = "john.doe@example.com"

  private lazy val anyOfficeId: UUID = UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed")
}
