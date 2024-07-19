package io.github.avapl
package adapters.http.account

import adapters.auth.repository.PublicKeyRepository
import adapters.auth.service.RolesExtractorService
import adapters.http.ApiError
import adapters.http.SecuredApiEndpoint
import cats.MonadThrow
import cats.data.NonEmptyList
import cats.effect.Clock
import cats.syntax.all._
import domain.model.account.Account
import domain.model.account.Role
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
import domain.model.error.account.AccountNotFound
import domain.model.error.account.DuplicateAccountEmail
import domain.model.error.office.OfficeNotFound
import domain.service.account.AccountService
import java.util.UUID
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint

class AccountEndpoints[F[_]: Clock: MonadThrow](
  accountService: AccountService[F],
  override val publicKeyRepository: PublicKeyRepository[F],
  override val rolesExtractor: RolesExtractorService
) extends SecuredApiEndpoint[F] {

  override protected def apiEndpointName: String = "account"

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
    securedEndpoint(requiredRole = OfficeManager).post
      .summary("Create a user")
      .description("Required role: office manager")
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
      .serverLogic(_ => createUser)

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
    securedEndpoint(requiredRole = User).get
      .summary("Find a user by ID")
      .description("Required role: office manager")
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
      .serverLogic(_ => readUser)

  private def readUser(userId: UUID) =
    accountService
      .readUser(userId)
      .map(ApiUserAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case AccountNotFound(accountId) => ApiError.NotFound(s"User [id: $accountId] was not found").asLeft
      }

  private lazy val assignOfficeToUserEndpoint =
    securedEndpoint(requiredRole = OfficeManager).put
      .summary("Assign office to a user")
      .description("Required role: office manager")
      .in("user" / path[UUID]("userId") / "assigned-office-id" / path[UUID]("assignedOfficeId"))
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
      .serverLogic(_ => (assignOfficeToUser _).tupled)

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
    securedEndpoint(requiredRole = OfficeManager).delete
      .summary("Unassign user office")
      .description("Required role: office manager")
      .in("user" / path[UUID]("userId") / "assigned-office-id")
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
      .serverLogic(_ => unassignUserOffice)

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
    securedEndpoint(requiredRole = SuperAdmin).post
      .summary("Create an office manager")
      .description("Required role: super admin")
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
      .serverLogic(_ => createOfficeManager)

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
    securedEndpoint(requiredRole = OfficeManager).get
      .summary("Find an office manager by ID")
      .description("Required role: office manager")
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
      .serverLogic(_ => readOfficeManager)

  private def readOfficeManager(officeManagerId: UUID) =
    accountService
      .readOfficeManager(officeManagerId)
      .map(ApiOfficeManagerAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case AccountNotFound(accountId) => ApiError.NotFound(s"Office manager [id: $accountId] was not found").asLeft
      }

  private lazy val updateOfficeManagerManagedOfficesEndpoint =
    securedEndpoint(requiredRole = SuperAdmin).put
      .summary("Assign managed offices to an office manager")
      .description("Required role: super admin")
      .in("office-manager" / path[UUID]("officeManagerId") / "managed-office-ids")
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
      // TODO: Validate office manager managed office IDs
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.NotFound) and jsonBody[ApiError.NotFound]
            .description("Office manager with the given ID was not found")
        )
      )
      .serverLogic(_ => (updateOfficeManagerManagedOffices _).tupled)

  private def updateOfficeManagerManagedOffices(officeManagerId: UUID, managedOfficeIds: List[UUID]) =
    accountService
      .updateOfficeManagerManagedOffices(officeManagerId, managedOfficeIds)
      .map(ApiOfficeManagerAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case AccountNotFound(accountId) => ApiError.NotFound(s"Office manager [id: $accountId] was not found").asLeft
      }

  private lazy val createSuperAdminEndpoint =
    securedEndpoint(requiredRole = SuperAdmin).post
      .summary("Create a super admin")
      .description("Required role: super admin")
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
      .serverLogic(_ => createSuperAdmin)

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
    securedEndpoint(requiredRole = SuperAdmin).get
      .summary("Find a super admin by ID")
      .description("Required role: super admin")
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
      .serverLogic(_ => readSuperAdmin)

  private def readSuperAdmin(superAdminId: UUID) =
    accountService
      .readSuperAdmin(superAdminId)
      .map(ApiSuperAdminAccount.fromDomain)
      .map(_.asRight[ApiError])
      .recover {
        case AccountNotFound(accountId) => ApiError.NotFound(s"Super admin [id: $accountId] was not found").asLeft
      }

  private lazy val updateRolesEndpoint =
    securedEndpoint(requiredRole = SuperAdmin).put
      .summary("Update account roles")
      .description(
        """Updates account roles. The account can be promoted or demoted depending on the roles provided.
          |
          |Required role: super admin
          |""".stripMargin
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
      .serverLogic(_ => (updateRoles _).tupled)

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
    securedEndpoint(requiredRole = OfficeManager).delete
      .summary("Archive an account")
      .description(
        """Archives an account. The account is NOT deleted. The operation is idempotent ie. if the account doesn't exist, the operation doesn't fail.
          |
          |Required role: office manager
          |Access note: The requester can only modify accounts with lower privileges.
          |""".stripMargin
      )
      .in(path[UUID]("accountId"))
      .out(
        statusCode(StatusCode.NoContent)
          .description("Account archived or not found")
      )
      .serverLogic(accessToken => archiveAccount(accessToken.roles))

  private def archiveAccount(requesterRoles: List[Role])(accountId: UUID): F[Either[ApiError, Unit]] =
    for {
      isAccountToArchiveSuperAdmin <- exists(_.readSuperAdmin(accountId))
      isAccountToArchiveOfficeManager <- exists(_.readOfficeManager(accountId))
      isAuthorized =
        if (isAccountToArchiveSuperAdmin || isAccountToArchiveOfficeManager) requesterRoles.contains(SuperAdmin)
        else requesterRoles.contains(OfficeManager)
      result <-
        if (isAuthorized) accountService.archive(accountId).as(().asRight[ApiError])
        else ApiError.Unauthorized.asLeft.pure[F]
    } yield result

  private def exists(readAccount: AccountService[F] => F[_ <: Account]) =
    readAccount(accountService).as(true).recover {
      case AccountNotFound(_) => false
    }

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
