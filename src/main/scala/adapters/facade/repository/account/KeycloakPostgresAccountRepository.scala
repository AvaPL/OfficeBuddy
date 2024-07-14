package io.github.avapl
package adapters.facade.repository.account

import adapters.keycloak.repository.account.KeycloakAttribute.ManagedOfficeIds
import adapters.keycloak.repository.account.KeycloakRole
import adapters.keycloak.repository.account.KeycloakUser
import adapters.keycloak.repository.account.KeycloakUserRepository
import adapters.postgres.repository.account._
import cats.MonadThrow
import cats.data.NonEmptyList
import cats.effect.Resource
import cats.effect.Sync
import cats.syntax.all._
import domain.model.account._
import domain.model.error.account.AccountNotFound
import domain.repository.account.AccountRepository
import java.util.UUID
import org.keycloak.admin.client.Keycloak
import skunk.Session

class KeycloakPostgresAccountRepository[F[_]: MonadThrow](
  keycloakUserRepository: KeycloakUserRepository[F],
  postgresAccountRepository: PostgresAccountRepository[F]
) extends AccountRepository[F] {

  override def createUser(user: UserAccount): F[UserAccount] =
    for {
      _ <- postgresAccountRepository.createUser(PostgresUserAccount.fromUserAccount(user))
      _ <- keycloakUserRepository.createUser(KeycloakUser.fromUserAccount(user))
    } yield user

  override def readUser(userId: UUID): F[UserAccount] =
    for {
      postgresUser <- postgresAccountRepository.readUser(userId)
      keycloakUser <- keycloakUserRepository.findUserByEmail(postgresUser.email)
    } yield toUserAccount(postgresUser, keycloakUser)

  private def toUserAccount(postgresUser: PostgresUserAccount, keycloakUser: KeycloakUser) =
    UserAccount(
      id = postgresUser.id,
      firstName = keycloakUser.firstName,
      lastName = keycloakUser.lastName,
      email = postgresUser.email,
      isArchived = postgresUser.isArchived,
      assignedOfficeId = postgresUser.assignedOfficeId
    )

  override def updateUserAssignedOffice(userId: UUID, officeId: Option[UUID]): F[UserAccount] =
    for {
      postgresUser <- postgresAccountRepository.updateUserAssignedOffice(userId, officeId)
      keycloakUser <- keycloakUserRepository.findUserByEmail(postgresUser.email)
    } yield toUserAccount(postgresUser, keycloakUser)

  override def createOfficeManager(officeManager: OfficeManagerAccount): F[OfficeManagerAccount] =
    for {
      _ <- postgresAccountRepository.createOfficeManager(
        PostgresOfficeManagerAccount.fromOfficeManagerAccount(officeManager)
      )
      _ <- keycloakUserRepository.createUser(KeycloakUser.fromOfficeManagerAccount(officeManager))
    } yield officeManager

  override def readOfficeManager(officeManagerId: UUID): F[OfficeManagerAccount] =
    for {
      postgresOfficeManager <- postgresAccountRepository.readOfficeManager(officeManagerId)
      keycloakUser <- keycloakUserRepository.findUserByEmail(postgresOfficeManager.email)
    } yield toOfficeManagerAccount(postgresOfficeManager, keycloakUser)

  private def toOfficeManagerAccount(postgresOfficeManager: PostgresOfficeManagerAccount, keycloakUser: KeycloakUser) =
    OfficeManagerAccount(
      id = postgresOfficeManager.id,
      firstName = keycloakUser.firstName,
      lastName = keycloakUser.lastName,
      email = postgresOfficeManager.email,
      isArchived = postgresOfficeManager.isArchived,
      managedOfficeIds = keycloakUser.attributes.collect {
        case ManagedOfficeIds(ids) => ids
      }.flatten
    )

  override def updateOfficeManagerManagedOffices(
    officeManagerId: UUID,
    officeIds: List[UUID]
  ): F[OfficeManagerAccount] =
    for {
      postgresOfficeManager <- postgresAccountRepository.readOfficeManager(officeManagerId)
      // TODO: If more attributes are introduced, this logic has to be adjusted
      keycloakUser <- keycloakUserRepository.updateUserAttributes(
        postgresOfficeManager.email,
        List(ManagedOfficeIds(officeIds))
      )
    } yield toOfficeManagerAccount(postgresOfficeManager, keycloakUser)

  override def createSuperAdmin(superAdmin: SuperAdminAccount): F[SuperAdminAccount] =
    for {
      _ <- postgresAccountRepository.createSuperAdmin(PostgresSuperAdminAccount.fromSuperAdminAccount(superAdmin))
      _ <- keycloakUserRepository.createUser(KeycloakUser.fromSuperAdminAccount(superAdmin))
    } yield superAdmin

  override def readSuperAdmin(superAdminId: UUID): F[SuperAdminAccount] =
    for {
      postgresSuperAdmin <- postgresAccountRepository.readSuperAdmin(superAdminId)
      keycloakUser <- keycloakUserRepository.findUserByEmail(postgresSuperAdmin.email)
    } yield toSuperAdminAccount(postgresSuperAdmin, keycloakUser)

  private def toSuperAdminAccount(postgresSuperAdmin: PostgresSuperAdminAccount, keycloakUser: KeycloakUser) =
    SuperAdminAccount(
      id = postgresSuperAdmin.id,
      firstName = keycloakUser.firstName,
      lastName = keycloakUser.lastName,
      email = postgresSuperAdmin.email,
      isArchived = postgresSuperAdmin.isArchived
    )

  override def updateRoles(accountId: UUID, roles: NonEmptyList[Role]): F[Account] =
    for {
      postgresAccount <- postgresAccountRepository.updateRoles(accountId, roles)
      keycloakRoles = roles.map(KeycloakRole.fromDomain).toList
      keycloakUser <- keycloakUserRepository.updateUserRoles(postgresAccount.email, keycloakRoles)
    } yield toAccount(postgresAccount, keycloakUser)

  private def toAccount(account: PostgresAccount, keycloakUser: KeycloakUser) =
    account match {
      case user: PostgresUserAccount                   => toUserAccount(user, keycloakUser)
      case officeManager: PostgresOfficeManagerAccount => toOfficeManagerAccount(officeManager, keycloakUser)
      case superAdmin: PostgresSuperAdminAccount       => toSuperAdminAccount(superAdmin, keycloakUser)
    }

  override def archive(accountId: UUID): F[Unit] = {
    for {
      _ <- postgresAccountRepository.archive(accountId)
      email <- postgresAccountRepository.readAccountEmail(accountId)
      _ <- keycloakUserRepository.disableUser(email)
    } yield ()
  }.recoverWith {
    case AccountNotFound(_) => ().pure
  }
}

object KeycloakPostgresAccountRepository {

  def apply[F[_]: Sync](
    keycloak: Keycloak,
    realmName: String,
    session: Resource[F, Session[F]]
  ): KeycloakPostgresAccountRepository[F] = {
    val keycloakUserRepository = new KeycloakUserRepository[F](keycloak, realmName)
    val postgresAccountRepository = new PostgresAccountRepository[F](session)
    new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)
  }
}
