package io.github.avapl
package adapters.facade.repository.account

import adapters.keycloak.repository.account.KeycloakAttribute.ManagedOfficeIds
import adapters.keycloak.repository.account.KeycloakUser
import adapters.keycloak.repository.account.KeycloakUserRepository
import adapters.postgres.repository.account.PostgresAccount
import adapters.postgres.repository.account.PostgresAccountRepository
import adapters.postgres.repository.account.PostgresOfficeManagerAccount
import adapters.postgres.repository.account.PostgresSuperAdminAccount
import adapters.postgres.repository.account.PostgresUserAccount
import cats.FlatMap
import cats.syntax.all._
import domain.model.account._
import domain.repository.account.AccountRepository
import java.util.UUID

class KeycloakPostgresAccountRepository[F[_]: FlatMap](
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

  override def updateRoles(accountId: UUID, roles: List[Role]): F[Account] =
    for {
      postgresAccount <- postgresAccountRepository.updateRoles(accountId, roles)
      keycloakUser <- keycloakUserRepository.updateUserRoles(postgresAccount.email)
    } yield toAccount(postgresAccount, keycloakUser)

  private def toAccount(account: PostgresAccount, keycloakUser: KeycloakUser) =
    account match {
      case user: PostgresUserAccount                   => toUserAccount(user, keycloakUser)
      case officeManager: PostgresOfficeManagerAccount => toOfficeManagerAccount(officeManager, keycloakUser)
      case superAdmin: PostgresSuperAdminAccount       => toSuperAdminAccount(superAdmin, keycloakUser)
    }

  override def archive(accountId: UUID): F[Unit] =
    for {
      postgresAccount <- postgresAccountRepository.archive(accountId)
      _ <- keycloakUserRepository.disableUser(postgresAccount.email)
    } yield ()
}
