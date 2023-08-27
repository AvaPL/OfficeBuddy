package io.github.avapl
package adapters.http.account

import adapters.http.ApiError
import adapters.http.BaseEndpoint
import cats.ApplicativeThrow
import cats.data.NonEmptyList
import cats.syntax.all._
import domain.model.account.Role
import domain.model.error.account.AccountNotFound
import domain.model.error.account.DuplicateAccountEmail
import domain.model.error.office.OfficeNotFound
import domain.service.account.AccountService
import java.util.UUID
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint

class AccountEndpoints[F[_]: ApplicativeThrow](
  accountService: AccountService[F]
) extends BaseEndpoint {

  override protected def baseEndpointName: String = "account"

  val endpoints: List[ServerEndpoint[Any, F]] =
    createUserEndpoint ::
      readUserEndpoint ::
      assignOfficeToUserEndpoint ::
      unassignUserOfficeEndpoint ::
      createOfficeManagerEndpoint ::
      readOfficeManagerEndpoint ::
      updateOfficeManagerManagedOfficesEndpoint ::
      createSuperAdminEndpoint ::
      readSuperAdminEndpoint ::
      updateRolesEndpoint ::
      archiveAccountEndpoint ::
      Nil

  private lazy val createUserEndpoint =
    baseEndpoint.post
      .summary("Create a user")
      .in("user")
      .in(
        jsonBody[ApiCreateUserAccount]
          .example(apiCreateUserAccountExample)
      )
      .out(
        statusCode(StatusCode.Created) and jsonBody[ApiUserAccount]
          .description("User created")
          .example(apiUserAccountExample)
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
            .description("Account with the given email already exists")
        )
      )
      .serverLogic(createUser)

  private def createUser(apiCreateUser: ApiCreateUserAccount) =
    accountService
      .createUser(apiCreateUser.toDomain)
      .map(ApiUserAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case OfficeNotFound(officeId) =>
          ApiError.BadRequest(s"Office [id: $officeId] was not found").asLeft
        case DuplicateAccountEmail(email) =>
          ApiError.Conflict(s"Account with email '$email' is already defined").asLeft
      }

  private lazy val readUserEndpoint =
    baseEndpoint.get
      .summary("Find a user by ID")
      .in("user" / path[UUID]("userId"))
      .out(
        jsonBody[ApiUserAccount]
          .description("Found user")
          .example(apiUserAccountExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("User with the given ID was not found")
        )
      )
      .serverLogic(readUser)

  private def readUser(userId: UUID) =
    accountService
      .readUser(userId)
      .map(ApiUserAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case AccountNotFound(accountId) => ApiError.NotFound(s"User [id: $accountId] was not found").asLeft
      }

  private lazy val assignOfficeToUserEndpoint =
    baseEndpoint.put
      .summary("Assign office to a user")
      .in("user" / path[UUID]("userId") / "assignedOfficeId" / path[UUID]("assignedOfficeId"))
      .out(
        jsonBody[ApiUserAccount]
          .description("Updated user")
          .example(apiUserAccountExample)
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
            .description("User with the given ID was not found")
        )
      )
      .serverLogic((assignOfficeToUser _).tupled)

  private def assignOfficeToUser(userId: UUID, assignedOfficeId: UUID) =
    accountService
      .updateUserAssignedOffice(userId, Some(assignedOfficeId))
      .map(ApiUserAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case OfficeNotFound(officeId) =>
          ApiError.BadRequest(s"Office [id: $officeId] was not found").asLeft
        case AccountNotFound(accountId) =>
          ApiError.NotFound(s"User [id: $accountId] was not found").asLeft
      }

  private lazy val unassignUserOfficeEndpoint =
    baseEndpoint.delete
      .summary("Unassign user office")
      .in("user" / path[UUID]("userId") / "assignedOfficeId")
      .out(
        jsonBody[ApiUserAccount]
          .description("Updated user")
          .example(apiUserAccountExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("User with the given ID was not found")
        )
      )
      .serverLogic(unassignUserOffice)

  private def unassignUserOffice(userId: UUID) =
    accountService
      .updateUserAssignedOffice(userId, officeId = None)
      .map(ApiUserAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case AccountNotFound(accountId) =>
          ApiError.NotFound(s"User [id: $accountId] was not found").asLeft
      }

  private lazy val createOfficeManagerEndpoint =
    baseEndpoint.post
      .summary("Create an office manager")
      .in("office-manager")
      .in(
        jsonBody[ApiCreateOfficeManagerAccount]
          .example(apiCreateOfficeManagerAccountExample)
      )
      .out(
        statusCode(StatusCode.Created) and jsonBody[ApiOfficeManagerAccount]
          .description("Office manager created")
          .example(apiOfficeManagerAccountExample)
      )
      // TODO: Validate office manager managed office IDs
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.Conflict) and jsonBody[ApiError.Conflict]
            .description("Account with the given email already exists")
        )
      )
      .serverLogic(createOfficeManager)

  private def createOfficeManager(apiCreateOfficeManager: ApiCreateOfficeManagerAccount) =
    accountService
      .createOfficeManager(apiCreateOfficeManager.toDomain)
      .map(ApiOfficeManagerAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case DuplicateAccountEmail(email) =>
          ApiError.Conflict(s"Account with email '$email' is already defined").asLeft
      }

  private lazy val readOfficeManagerEndpoint =
    baseEndpoint.get
      .summary("Find an office manager by ID")
      .in("office-manager" / path[UUID]("officeManagerId"))
      .out(
        jsonBody[ApiOfficeManagerAccount]
          .description("Found office manager")
          .example(apiOfficeManagerAccountExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Office manager with the given ID was not found")
        )
      )
      .serverLogic(readOfficeManager)

  private def readOfficeManager(officeManagerId: UUID) =
    accountService
      .readOfficeManager(officeManagerId)
      .map(ApiOfficeManagerAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case AccountNotFound(accountId) => ApiError.NotFound(s"Office manager [id: $accountId] was not found").asLeft
      }

  private lazy val updateOfficeManagerManagedOfficesEndpoint =
    baseEndpoint.put
      .summary("Assign managed offices to an office manager")
      .in("office-manager" / path[UUID]("officeManagerId") / "managedOfficeIds")
      .in(
        jsonBody[List[UUID]]
          .description("Managed office IDs")
          .example(managedOfficeIdsExample)
      )
      .out(
        jsonBody[ApiOfficeManagerAccount]
          .description("Updated office manager")
          .example(apiOfficeManagerAccountExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Office manager with the given ID was not found")
        )
      )
      .serverLogic((updateOfficeManagerManagedOffices _).tupled)

  private def updateOfficeManagerManagedOffices(officeManagerId: UUID, managedOfficeIds: List[UUID]) =
    accountService
      .updateOfficeManagerManagedOffices(officeManagerId, managedOfficeIds)
      .map(ApiOfficeManagerAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case AccountNotFound(accountId) => ApiError.NotFound(s"Office manager [id: $accountId] was not found").asLeft
      }

  private lazy val createSuperAdminEndpoint =
    baseEndpoint.post
      .summary("Create a super admin")
      .in("super-admin")
      .in(
        jsonBody[ApiCreateSuperAdminAccount]
          .example(apiCreateSuperAdminAccountExample)
      )
      .out(
        statusCode(StatusCode.Created) and jsonBody[ApiSuperAdminAccount]
          .description("Super admin created")
          .example(apiSuperAdminAccountExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.Conflict) and jsonBody[ApiError.Conflict]
            .description("Account with the given email already exists")
        )
      )
      .serverLogic(createSuperAdmin)

  private def createSuperAdmin(apiCreateSuperAdmin: ApiCreateSuperAdminAccount) =
    accountService
      .createSuperAdmin(apiCreateSuperAdmin.toDomain)
      .map(ApiSuperAdminAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case DuplicateAccountEmail(email) =>
          ApiError.Conflict(s"Account with email '$email' is already defined").asLeft
      }

  private lazy val readSuperAdminEndpoint =
    baseEndpoint.get
      .summary("Find a super admin by ID")
      .in("super-admin" / path[UUID]("superAdminId"))
      .out(
        jsonBody[ApiSuperAdminAccount]
          .description("Found super admin")
          .example(apiSuperAdminAccountExample)
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Super admin with the given ID was not found")
        )
      )
      .serverLogic(readSuperAdmin)

  private def readSuperAdmin(superAdminId: UUID) =
    accountService
      .readSuperAdmin(superAdminId)
      .map(ApiSuperAdminAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case AccountNotFound(accountId) => ApiError.NotFound(s"Super admin [id: $accountId] was not found").asLeft
      }

  private lazy val updateRolesEndpoint =
    baseEndpoint.put
      .summary("Update account roles")
      .description(
        "Updates account roles. The account can be promoted or demoted depending on the roles provided."
      )
      .in(path[UUID]("accountId") / "roles")
      .in(
        jsonBody[List[ApiRole]]
          .description("Non-empty list of roles")
          .example(apiAccountRolesExample)
      )
      .out(
        statusCode(StatusCode.NoContent)
          .description("Roles updated")
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.BadRequest) and jsonBody[ApiError.BadRequest]
            .description("Roles list cannot be empty")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Account with the given ID was not found")
        )
      )
      .serverLogic((updateRoles _).tupled)

  private def updateRoles(accountId: UUID, apiRoles: List[ApiRole]): F[Either[ApiError, Unit]] =
    apiRoles.map(_.toDomain) match {
      case head :: tail => updateRolesNel(accountId, NonEmptyList.of(head, tail: _*))
      case Nil          => ApiError.BadRequest("Roles list cannot be empty").asLeft.pure[F].widen
    }

  private def updateRolesNel(accountId: UUID, roles: NonEmptyList[Role]) =
    accountService
      .updateRoles(accountId, roles)
      .as(().asRight[ApiError])
      .recover {
        case AccountNotFound(accountId) => ApiError.NotFound(s"Account [id: $accountId] was not found").asLeft
      }

  private lazy val archiveAccountEndpoint =
    baseEndpoint.delete
      .summary("Archive an account")
      .description(
        "Archives an account. The account is NOT deleted. The operation is idempotent ie. if the account doesn't exist, the operation doesn't fail."
      )
      .in(path[UUID]("accountId"))
      .out(
        statusCode(StatusCode.NoContent)
          .description("Account archived or not found")
      )
      .serverLogic(archiveAccount)

  private def archiveAccount(accountId: UUID) =
    accountService
      .archive(accountId)
      .as(().asRight[ApiError])

  private lazy val apiUserAccountExample = ApiUserAccount(
    id = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40"),
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com",
    isArchived = false,
    assignedOfficeId = Some(UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed"))
  )

  private lazy val apiCreateUserAccountExample = ApiCreateUserAccount(
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com",
    assignedOfficeId = Some(UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed"))
  )

  private lazy val apiOfficeManagerAccountExample = ApiOfficeManagerAccount(
    id = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40"),
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com",
    isArchived = false,
    managedOfficeIds = List(
      UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed"),
      UUID.fromString("305bfad9-9354-4d7f-93ef-c1bab1f8dd7b")
    )
  )

  private lazy val apiCreateOfficeManagerAccountExample = ApiCreateOfficeManagerAccount(
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com",
    managedOfficeIds = List(
      UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed"),
      UUID.fromString("305bfad9-9354-4d7f-93ef-c1bab1f8dd7b")
    )
  )

  private lazy val managedOfficeIdsExample = List(
    UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed"),
    UUID.fromString("305bfad9-9354-4d7f-93ef-c1bab1f8dd7b")
  )

  private lazy val apiSuperAdminAccountExample = ApiSuperAdminAccount(
    id = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40"),
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com",
    isArchived = false
  )

  private lazy val apiCreateSuperAdminAccountExample = ApiCreateSuperAdminAccount(
    firstName = "John",
    lastName = "Doe",
    email = "john.doe@example.com"
  )

  private lazy val apiAccountRolesExample = List(
    ApiRole.OfficeManager
  )
}
