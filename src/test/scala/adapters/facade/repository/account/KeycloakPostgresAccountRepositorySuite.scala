package io.github.avapl
package adapters.facade.repository.account

import adapters.keycloak.repository.account.KeycloakAttribute
import adapters.keycloak.repository.account.KeycloakAttribute.ManagedOfficeIds
import adapters.keycloak.repository.account.KeycloakRole
import adapters.keycloak.repository.account.KeycloakUser
import adapters.keycloak.repository.account.KeycloakUserRepository
import adapters.postgres.repository.account.PostgresAccountRepository
import adapters.postgres.repository.account.PostgresOfficeManagerAccount
import adapters.postgres.repository.account.PostgresSuperAdminAccount
import adapters.postgres.repository.account.PostgresUserAccount
import cats.data.NonEmptyList
import cats.effect.IO
import com.softwaremill.quicklens._
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
      | WHEN createUser is called
      | THEN the user should be created in Postgres and Keycloak
      |""".stripMargin
  ) {
    val user = anyUserAccount

    val keycloakUser = KeycloakUser.fromUserAccount(user)
    val postgresUserAccount = PostgresUserAccount.fromUserAccount(user)

    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.createUser(any)) thenReturn keycloakUser
    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.create(any)) thenReturn postgresUserAccount
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      createdUser <- keycloakPostgresAccountRepository.createUser(user)
    } yield {
      verify(postgresAccountRepository, only).create(eqTo(postgresUserAccount))
      verify(keycloakUserRepository, only).createUser(eqTo(keycloakUser))
      expect(createdUser == user)
    }
  }

  test(
    """GIVEN a user ID
      | WHEN readUser is called
      | THEN the user should be read from Postgres and Keycloak
      |""".stripMargin
  ) {
    val userId = anyAccountId

    val expectedUser = anyUserAccount.copy(id = userId)
    val postgresUserAccount = PostgresUserAccount.fromUserAccount(expectedUser)
    val keycloakUser = KeycloakUser.fromUserAccount(expectedUser)

    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.read(any)) thenReturn postgresUserAccount
    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.findUserByEmail(any)) thenReturn keycloakUser
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      readUser <- keycloakPostgresAccountRepository.readUser(userId)
    } yield {
      verify(postgresAccountRepository, only).read(eqTo(userId))
      verify(keycloakUserRepository, only).findUserByEmail(eqTo(postgresUserAccount.email))
      expect(readUser == expectedUser)
    }
  }

  test(
    """GIVEN a user ID and an office ID
      | WHEN updateUserAssignedOffice is called
      | THEN the assigned office should be updated in Postgres and the missing fields should be read from Keycloak
      |""".stripMargin
  ) {
    val userId = anyAccountId
    val assignedOfficeId = Some(anyOfficeId)

    val expectedUpdatedUser = anyUserAccount.copy(id = userId, assignedOfficeId = assignedOfficeId)
    val postgresUserAccount = PostgresUserAccount.fromUserAccount(expectedUpdatedUser)
    val keycloakUser = KeycloakUser.fromUserAccount(expectedUpdatedUser)

    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.updateAssignedOffice(any, any)) thenReturn postgresUserAccount
    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.findUserByEmail(any)) thenReturn keycloakUser
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      updatedUser <- keycloakPostgresAccountRepository.updateUserAssignedOffice(userId, assignedOfficeId)
    } yield {
      verify(postgresAccountRepository, only).updateAssignedOffice(eqTo(userId), eqTo(assignedOfficeId))
      verify(keycloakUserRepository, only).findUserByEmail(eqTo(postgresUserAccount.email))
      expect(updatedUser == expectedUpdatedUser)
    }
  }

  test(
    """GIVEN an office manager to create
      | WHEN createOfficeManager is called
      | THEN the office manager should be created in Postgres and Keycloak
      |""".stripMargin
  ) {
    val officeManager = anyOfficeManagerAccount

    val keycloakUser = KeycloakUser.fromOfficeManagerAccount(officeManager)
    val postgresOfficeManagerAccount = PostgresOfficeManagerAccount.fromOfficeManagerAccount(officeManager)

    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.createUser(any)) thenReturn keycloakUser
    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.createOfficeManager(any)) thenReturn postgresOfficeManagerAccount
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      createdOfficeManager <- keycloakPostgresAccountRepository.createOfficeManager(officeManager)
    } yield {
      verify(postgresAccountRepository, only).createOfficeManager(eqTo(postgresOfficeManagerAccount))
      verify(keycloakUserRepository, only).createUser(eqTo(keycloakUser))
      expect(createdOfficeManager == officeManager)
    }
  }

  test(
    """GIVEN an office manager ID
      | WHEN readOfficeManager is called
      | THEN the user should be read from Postgres and Keycloak
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId

    val expectedOfficeManager = anyOfficeManagerAccount.copy(id = officeManagerId)
    val postgresOfficeManagerAccount = PostgresOfficeManagerAccount.fromOfficeManagerAccount(expectedOfficeManager)
    val keycloakUser = KeycloakUser.fromOfficeManagerAccount(expectedOfficeManager)

    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.readOfficeManager(any)) thenReturn postgresOfficeManagerAccount
    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.findUserByEmail(any)) thenReturn keycloakUser
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      readOfficeManager <- keycloakPostgresAccountRepository.readOfficeManager(officeManagerId)
    } yield {
      verify(postgresAccountRepository, only).readOfficeManager(eqTo(officeManagerId))
      verify(keycloakUserRepository, only).findUserByEmail(eqTo(postgresOfficeManagerAccount.email))
      expect(readOfficeManager == expectedOfficeManager)
    }
  }

  test(
    """GIVEN an office manager ID and a list of managed office IDs
      | WHEN updateOfficeManagerManagedOffices is called
      | THEN the managed offices should be updated in Keycloak and the missing fields should be read from Postgres
      |""".stripMargin
  ) {
    val officeManagerId = anyAccountId
    val managedOfficeIds = List(
      UUID.fromString("5de012f5-f997-4735-983a-280c0222d5d0"),
      UUID.fromString("92e171d2-6568-482f-b0ac-ca200b12e54a")
    )

    val expectedOfficeManager = anyOfficeManagerAccount.copy(id = officeManagerId, managedOfficeIds = managedOfficeIds)
    val postgresOfficeManagerAccount = PostgresOfficeManagerAccount.fromOfficeManagerAccount(expectedOfficeManager)
    val keycloakUser = KeycloakUser.fromOfficeManagerAccount(expectedOfficeManager)

    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.readOfficeManager(any)) thenReturn postgresOfficeManagerAccount
    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.getUserAttributes(any)) thenReturn keycloakUser.attributes
    val updatedKeycloakUser =
      keycloakUser.modify(_.attributes.each.when[ManagedOfficeIds]).setTo(ManagedOfficeIds(managedOfficeIds))
    whenF(keycloakUserRepository.updateUserAttributes(any, any)) thenReturn updatedKeycloakUser
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      updatedOfficeManager <- keycloakPostgresAccountRepository.updateOfficeManagerManagedOffices(
        officeManagerId,
        managedOfficeIds
      )
    } yield {
      verify(postgresAccountRepository, only).readOfficeManager(eqTo(officeManagerId))
      verify(keycloakUserRepository, times(1)).updateUserAttributes(
        eqTo(postgresOfficeManagerAccount.email),
        argThat[List[KeycloakAttribute]](_.toSet == updatedKeycloakUser.attributes.toSet, "same elements")
      )
      expect(updatedOfficeManager == expectedOfficeManager)
    }
  }

  test(
    """GIVEN a super admin to create
      | WHEN createSuperAdmin is called
      | THEN the super admin should be created in Postgres and Keycloak
      |""".stripMargin
  ) {
    val superAdmin = anySuperAdminAccount

    val keycloakUser = KeycloakUser.fromSuperAdminAccount(superAdmin)
    val postgresSuperAdminAccount = PostgresSuperAdminAccount.fromSuperAdminAccount(superAdmin)

    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.createUser(any)) thenReturn keycloakUser
    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.createSuperAdmin(any)) thenReturn postgresSuperAdminAccount
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      createdSuperAdmin <- keycloakPostgresAccountRepository.createSuperAdmin(superAdmin)
    } yield {
      verify(postgresAccountRepository, only).createSuperAdmin(eqTo(postgresSuperAdminAccount))
      verify(keycloakUserRepository, only).createUser(eqTo(keycloakUser))
      expect(createdSuperAdmin == superAdmin)
    }
  }

  test(
    """GIVEN a super admin ID
      | WHEN readSuperAdmin is called
      | THEN the user should be read from Postgres and Keycloak
      |""".stripMargin
  ) {
    val superAdminId = anyAccountId

    val expectedSuperAdmin = anySuperAdminAccount.copy(id = superAdminId)
    val postgresSuperAdminAccount = PostgresSuperAdminAccount.fromSuperAdminAccount(expectedSuperAdmin)
    val keycloakUser = KeycloakUser.fromSuperAdminAccount(expectedSuperAdmin)

    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.readSuperAdmin(any)) thenReturn postgresSuperAdminAccount
    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.findUserByEmail(any)) thenReturn keycloakUser
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      readSuperAdmin <- keycloakPostgresAccountRepository.readSuperAdmin(superAdminId)
    } yield {
      verify(postgresAccountRepository, only).readSuperAdmin(eqTo(superAdminId))
      verify(keycloakUserRepository, only).findUserByEmail(eqTo(postgresSuperAdminAccount.email))
      expect(readSuperAdmin == expectedSuperAdmin)
    }
  }

  test(
    """GIVEN a user ID
      | WHEN updateRoles is called with the office manager role
      | THEN the role should be updated in Postgres and Keycloak
      |""".stripMargin
  ) {
    val userId = anyAccountId
    val roles = NonEmptyList.one(Role.OfficeManager)

    val expectedOfficeManager = anyOfficeManagerAccount.copy(id = userId)
    val postgresOfficeManagerAccount = PostgresOfficeManagerAccount.fromOfficeManagerAccount(expectedOfficeManager)
    val keycloakUser = KeycloakUser.fromOfficeManagerAccount(expectedOfficeManager)

    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.readAccountEmail(any)) thenReturn postgresOfficeManagerAccount.email
    whenF(postgresAccountRepository.updateRoles(any, any)) thenReturn postgresOfficeManagerAccount
    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.updateUserRoles(any, any)) thenReturn keycloakUser
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      officeManager <- keycloakPostgresAccountRepository.updateRoles(userId, roles)
    } yield {
      verify(postgresAccountRepository, times(1)).updateRoles(eqTo(userId), eqTo(roles))
      val keycloakRoles = roles.map(KeycloakRole.fromDomain).toList
      verify(keycloakUserRepository, only).updateUserRoles(
        eqTo(postgresOfficeManagerAccount.email),
        eqTo(keycloakRoles)
      )
      expect(officeManager == expectedOfficeManager)
    }
  }

  test(
    """GIVEN a user ID
      | WHEN archive is called
      | THEN the user should be archived in Postgres and Keycloak
      |""".stripMargin
  ) {
    val userId = anyAccountId

    val email = anyEmail

    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.archive(any)) thenReturn ()
    whenF(postgresAccountRepository.readAccountEmail(any)) thenReturn email
    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    whenF(keycloakUserRepository.disableUser(any)) thenReturn ()
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      _ <- keycloakPostgresAccountRepository.archive(userId)
    } yield {
      verify(postgresAccountRepository, times(1)).archive(userId)
      verify(keycloakUserRepository, only).disableUser(email)
      success
    }
  }

  test(
    """GIVEN a user ID
      | WHEN archive is called and Postgres fails with AccountNotFound error
      | THEN the call should recover and be successful
      |""".stripMargin
  ) {
    val userId = anyAccountId

    val postgresAccountRepository = mock[PostgresAccountRepository[IO]]
    whenF(postgresAccountRepository.archive(any)) thenReturn ()
    whenF(postgresAccountRepository.readAccountEmail(any)) thenFailWith AccountNotFound(userId)
    val keycloakUserRepository = mock[KeycloakUserRepository[IO]]
    val keycloakPostgresAccountRepository =
      new KeycloakPostgresAccountRepository(keycloakUserRepository, postgresAccountRepository)

    for {
      _ <- keycloakPostgresAccountRepository.archive(userId)
    } yield success
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
