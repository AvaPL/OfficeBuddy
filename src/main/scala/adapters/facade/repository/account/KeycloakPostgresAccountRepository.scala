package io.github.avapl
package adapters.facade.repository.account

import adapters.keycloak.repository.account.KeycloakRole
import adapters.keycloak.repository.account.KeycloakUser
import adapters.keycloak.repository.account.KeycloakUserRepository
import adapters.postgres.repository.account._
import cats.MonadThrow
import cats.effect.Resource
import cats.effect.Sync
import cats.syntax.all._
import domain.model.account._
import domain.model.error.account.AccountNotFound
import domain.repository.account.AccountRepository
import domain.repository.account.TemporaryPasswordRepository
import java.util.UUID
import org.keycloak.admin.client.Keycloak
import skunk.Session

class KeycloakPostgresAccountRepository[F[_]: MonadThrow](
  keycloakUserRepository: KeycloakUserRepository[F],
  postgresAccountRepository: PostgresAccountRepository[F]
) extends AccountRepository[F]
  with TemporaryPasswordRepository[F] {

  // TODO: Send an email to the user with a link to set a password
  override def create(account: Account): F[Account] =
    for {
      _ <- postgresAccountRepository.create(account)
      _ <- keycloakUserRepository.createUser(KeycloakUser.fromDomainAccount(account))
    } yield account

  override def read(accountId: UUID): F[Account] =
    for {
      account <- postgresAccountRepository.read(accountId)
      _ <- keycloakUserRepository.findUserByEmail(account.email)
    } yield account

  override def updateAssignedOffice(accountId: UUID, officeId: Option[UUID]): F[Account] =
    postgresAccountRepository.updateAssignedOffice(accountId, officeId)

  // TODO: Allow updating managed offices only for office managers and super admins (maybe via a DB constraint?)
  override def updateManagedOffices(
    accountId: UUID,
    managedOfficeIds: List[UUID]
  ): F[Account] =
    postgresAccountRepository.updateManagedOffices(accountId, managedOfficeIds)

  // TODO: Logout the user after the roles are updated
  override def updateRole(accountId: UUID, role: Role): F[Account] =
    for {
      account <- postgresAccountRepository.updateRole(accountId, role)
      keycloakRole = KeycloakRole.fromDomain(role)
      _ <- keycloakUserRepository.updateUserRoles(account.email, List(keycloakRole))
    } yield account

  override def archive(accountId: UUID): F[Unit] = {
    for {
      _ <- postgresAccountRepository.archive(accountId)
      email <- postgresAccountRepository.readAccountEmail(accountId)
      _ <- keycloakUserRepository.disableUser(email)
    } yield ()
  }.recoverWith {
    case AccountNotFound(_) => ().pure
  }

  override def setTemporaryPassword(accountId: UUID, temporaryPassword: String): F[Unit] =
    for {
      email <- postgresAccountRepository.readAccountEmail(accountId)
      _ <- keycloakUserRepository.setTemporaryPassword(email, temporaryPassword)
    } yield ()
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
