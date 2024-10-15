package io.github.avapl
package adapters.facade.repository.account

import adapters.keycloak.repository.account.KeycloakRole
import adapters.keycloak.repository.account.KeycloakUser
import adapters.keycloak.repository.account.KeycloakUserRepository
import adapters.postgres.repository.account.PostgresAccountRepository
import cats.effect.IO
import domain.model.account.OfficeManagerAccount
import domain.model.account.Role
import domain.model.account.SuperAdminAccount
import domain.model.account.UserAccount
import domain.model.error.account.AccountNotFound
import java.util.UUID
import org.mockito.ArgumentMatchersSugar
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import weaver.SimpleIOSuite

object KeycloakPostgresAccountRepositorySuite
  extends SimpleIOSuite
  with MockitoSugar
  with ArgumentMatchersSugar
  with MockitoCats {

  test(
    """GIVEN a user to create
      | WHEN create is called
      | THEN the account should be created in Postgres and Keycloak
      |""".stripMargin
  ) {
    val user = anyUserAccount

    val keycloakUser = KeycloakUser.fromDomainAccount(user)

    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.createUser(any)) thenReturn keycloakUser
    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.create(any)) thenReturn user
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      createdUser <- keycloakPostgresAccountRepository.create(user)
    } yield {
      verify(postgresAccountRepository, only).create(eqTo(user))
      verify(keycloakUserRepository, only).createUser(eqTo(keycloakUser))
      expect(createdUser == user)
    }
  }

  test(
    """GIVEN an office manager to create
      | WHEN create is called
      | THEN the office manager should be created in Postgres and Keycloak
      |""".stripMargin
  ) {
    val officeManager = anyOfficeManagerAccount

    val keycloakUser = KeycloakUser.fromDomainAccount(officeManager)

    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.createUser(any)) thenReturn keycloakUser
    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.create(any)) thenReturn officeManager
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      createdOfficeManager <- keycloakPostgresAccountRepository.create(officeManager)
    } yield {
      verify(postgresAccountRepository, only).create(eqTo(officeManager))
      verify(keycloakUserRepository, only).createUser(eqTo(keycloakUser))
      expect(createdOfficeManager == officeManager)
    }
  }

  test(
    """GIVEN a super admin to create
      | WHEN create is called
      | THEN the super admin should be created in Postgres and Keycloak
      |""".stripMargin
  ) {
    val superAdmin = anySuperAdminAccount

    val keycloakUser = KeycloakUser.fromDomainAccount(superAdmin)

    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.createUser(any)) thenReturn keycloakUser
    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.create(any)) thenReturn superAdmin
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      createdSuperAdmin <- keycloakPostgresAccountRepository.create(superAdmin)
    } yield {
      verify(postgresAccountRepository, only).create(eqTo(superAdmin))
      verify(keycloakUserRepository, only).createUser(eqTo(keycloakUser))
      expect(createdSuperAdmin == superAdmin)
    }
  }

  test(
    """GIVEN an account ID
      | WHEN read is called
      | THEN the user should be read from Postgres and Keycloak
      |""".stripMargin
  ) {
    val accountId = anyAccountId

    val expectedAccount = anyUserAccount.copy(id = accountId)
    val keycloakUser = KeycloakUser.fromDomainAccount(expectedAccount)

    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.read(any)) thenReturn expectedAccount
    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.findUserByEmail(any)) thenReturn keycloakUser
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      readUser <- keycloakPostgresAccountRepository.read(accountId)
    } yield {
      verify(postgresAccountRepository, only).read(eqTo(accountId))
      verify(keycloakUserRepository, only).findUserByEmail(eqTo(expectedAccount.email))
      expect(readUser == expectedAccount)
    }
  }

  test(
    """GIVEN an account ID and an office ID
      | WHEN updateAssignedOffice is called
      | THEN the assigned office should be updated in Postgres
      |""".stripMargin
  ) {
    val accountId = anyAccountId
    val assignedOfficeId = Some(anyOfficeId)

    val expectedUpdatedAccount = anyUserAccount.copy(id = accountId, assignedOfficeId = assignedOfficeId)

    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.updateAssignedOffice(any, any)) thenReturn expectedUpdatedAccount
    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      updatedUser <- keycloakPostgresAccountRepository.updateAssignedOffice(accountId, assignedOfficeId)
    } yield {
      verify(postgresAccountRepository, only).updateAssignedOffice(eqTo(accountId), eqTo(assignedOfficeId))
      expect(updatedUser == expectedUpdatedAccount)
    }
  }

  test(
    """GIVEN an office manager ID and a list of managed office IDs
      | WHEN updateManagedOffices is called
      | THEN the managed offices should be updated in Postgres
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId
    val managedOfficeIds = List(
      UUID.fromString("5de012f5-f997-4735-983a-280c0222d5d0"),
      UUID.fromString("92e171d2-6568-482f-b0ac-ca200b12e54a")
    )

    val expectedOfficeManager = anyOfficeManagerAccount.copy(id = officeManagerId, managedOfficeIds = managedOfficeIds)

    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.updateManagedOffices(any, any)) thenReturn expectedOfficeManager
    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      updatedOfficeManager <- keycloakPostgresAccountRepository.updateManagedOffices(officeManagerId, managedOfficeIds)
    } yield {
      verify(postgresAccountRepository).updateManagedOffices(eqTo(officeManagerId), eqTo(managedOfficeIds))
      expect(updatedOfficeManager == expectedOfficeManager)
    }
  }

  test(
    """GIVEN a user ID
      | WHEN updateRole is called with the OfficeManager role
      | THEN the role should be updated in Postgres and Keycloak
      |""".stripMargin
  ) {
    val userId = anyAccountId
    val role = Role.OfficeManager

    val expectedOfficeManager = anyOfficeManagerAccount.copy(id = userId)
    val keycloakUser = KeycloakUser.fromDomainAccount(expectedOfficeManager)

    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.readAccountEmail(any)) thenReturn expectedOfficeManager.email
    whenF(postgresAccountRepository.updateRole(any, any)) thenReturn expectedOfficeManager
    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.updateUserRoles(any, any)) thenReturn keycloakUser
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      officeManager <- keycloakPostgresAccountRepository.updateRole(userId, role)
    } yield {
      verify(postgresAccountRepository, times(1)).updateRole(eqTo(userId), eqTo(role))
      val keycloakRole = KeycloakRole.fromDomain(role)
      verify(keycloakUserRepository, only).updateUserRoles(eqTo(officeManager.email), eqTo(List(keycloakRole)))
      expect(officeManager == expectedOfficeManager)
    }
  }

  test(
    """GIVEN an account ID
      | WHEN archive is called
      | THEN the user should be archived in Postgres and Keycloak
      |""".stripMargin
  ) {
    val accountId = anyAccountId

    val email = anyEmail

    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.archive(any)) thenReturn ()
    whenF(postgresAccountRepository.readAccountEmail(any)) thenReturn email
    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.disableUser(any)) thenReturn ()
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      _ <- keycloakPostgresAccountRepository.archive(accountId)
    } yield {
      verify(postgresAccountRepository, times(1)).archive(accountId)
      verify(keycloakUserRepository, only).disableUser(email)
      success
    }
  }

  test(
    """GIVEN an account ID
      | WHEN archive is called and Postgres fails with AccountNotFound error
      | THEN the call should recover and be successful
      |""".stripMargin
  ) {
    val accountId = anyAccountId

    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.archive(any)) thenReturn ()
    whenF(postgresAccountRepository.readAccountEmail(any)) thenFailWith AccountNotFound(accountId)
    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      _ <- keycloakPostgresAccountRepository.archive(accountId)
    } yield success
  }

  test(
    """GIVEN an account ID and a temporary password
      | WHEN setTemporaryPassword is called
      | THEN the password should be set in Keycloak
      |""".stripMargin
  ) {
    val user = anyUserAccount
    val temporaryPassword = "temporaryPassword"

    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.readAccountEmail(any)) thenReturn user.email
    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.setTemporaryPassword(any, any)) thenReturn KeycloakUser.fromDomainAccount(user)
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      _ <- keycloakPostgresAccountRepository.setTemporaryPassword(user.id, temporaryPassword)
    } yield {
      verify(postgresAccountRepository, only).readAccountEmail(user.id)
      verify(keycloakUserRepository, only).setTemporaryPassword(eqTo(user.email), eqTo(temporaryPassword))
      success
    }
  }

  private lazy val anyUserAccount = UserAccount(
    id = anyAccountId,
    firstName = "Test",
    lastName = "User",
    email = "test.user@localhost",
    assignedOfficeId = Some(anyOfficeId)
  )

  private lazy val anyOfficeManagerAccount = OfficeManagerAccount(
    id = anyAccountId,
    firstName = "Test",
    lastName = "User",
    email = "test.office.manager@localhost",
    managedOfficeIds = List(anyOfficeId)
  )

  private lazy val anySuperAdminAccount = SuperAdminAccount(
    id = anyAccountId,
    firstName = "Test",
    lastName = "User",
    email = "test.super.admin@localhost"
  )

  private lazy val anyAccountId = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40")

  private lazy val anyEmail: String = "test.account@localhost"

  private lazy val anyOfficeId = UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed")
}
