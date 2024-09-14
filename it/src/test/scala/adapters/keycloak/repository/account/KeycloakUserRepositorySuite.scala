package io.github.avapl
package adapters.keycloak.repository.account

import adapters.keycloak.fixture.KeycloakFixture
import adapters.keycloak.repository.account.KeycloakAttribute.AccountId
import adapters.keycloak.repository.account.KeycloakRole.OfficeManager
import adapters.keycloak.repository.account.KeycloakRole.SuperAdmin
import adapters.keycloak.repository.account.KeycloakRole.User
import cats.effect.IO
import cats.syntax.all._
import domain.model.error.account.DuplicateAccountEmail
import java.util.UUID
import org.keycloak.admin.client.Keycloak
import scala.jdk.CollectionConverters._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object KeycloakUserRepositorySuite extends IOSuite with KeycloakFixture {

  private def beforeTest(name: TestName)(run: KeycloakUserRepository[IO] => IO[Expectations]): Unit =
    test(name) { keycloak =>
      lazy val keycloakAccountRepository = new KeycloakUserRepository[IO](keycloak, realmName)
      deleteAllUsers(keycloak) >>
        run(keycloakAccountRepository)
    }

  beforeTest(
    """GIVEN a user to create
      | WHEN createUser is called
      | THEN the user is properly created in Keycloak
      |""".stripMargin
  ) { keycloakAccountRepository =>
    val user = anyUser

    for {
      _ <- keycloakAccountRepository.createUser(user)
      readUser <- keycloakAccountRepository.findUserByEmail(user.email)
    } yield expect(readUser == user)
  }

  beforeTest(
    """GIVEN a user that already exists in Keycloak
      | WHEN createUser is called
      | THEN the call should fail with DuplicateAccountEmail
      |""".stripMargin
  ) { keycloakAccountRepository =>
    val user = anyUser
    val userWithTheSameEmail = user.copy(firstName = "Other")

    for {
      _ <- keycloakAccountRepository.createUser(user)
      result <- keycloakAccountRepository.createUser(userWithTheSameEmail).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val duplicateAccountEmail = DuplicateAccountEmail(user.email)
        expect(throwable == duplicateAccountEmail)
    }
  }

  beforeTest(
    """WHEN findUserByEmail is called with non-existent user email
      |THEN the call should fail with KeycloakUserNotFound
      |""".stripMargin
  ) { keycloakAccountRepository =>
    val email = anyEmail

    for {
      result <- keycloakAccountRepository.findUserByEmail(email).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val keycloakUserNotFound = KeycloakUserNotFound(email)
        expect(throwable == keycloakUserNotFound)
    }
  }

  beforeTest(
    """GIVEN a user with attributes
      | WHEN getUserAttributes is called
      | THEN the user attributes are properly read from Keycloak
      |""".stripMargin
  ) { keycloakAccountRepository =>
    val user = anyUser.copy(attributes = List(AccountId(UUID.fromString("1518acae-9048-4de1-b9bd-8ca28f848069"))))

    for {
      _ <- keycloakAccountRepository.createUser(user)
      readAttributes <- keycloakAccountRepository.getUserAttributes(user.email)
    } yield expect(readAttributes == user.attributes)
  }

  beforeTest(
    """GIVEN an existing user and new attributes
      | WHEN updateUserAttributes is called
      | THEN the user attributes are properly updated in Keycloak
      |""".stripMargin
  ) { keycloakAccountRepository =>
    val user = anyUser.copy(attributes = List(AccountId(UUID.fromString("b070123b-a063-44f4-95c5-a9947772c980"))))
    val newAttributes = List(AccountId(UUID.fromString("1518acae-9048-4de1-b9bd-8ca28f848069")))

    for {
      _ <- keycloakAccountRepository.createUser(user)
      _ <- keycloakAccountRepository.updateUserAttributes(user.email, newAttributes)
      readUser <- keycloakAccountRepository.findUserByEmail(user.email)
    } yield expect(readUser.attributes == newAttributes)
  }

  beforeTest(
    """WHEN updateUserAttributes is called with non-existent user email
      |THEN the call should fail with KeycloakUserNotFound
      |""".stripMargin
  ) { keycloakAccountRepository =>
    val email = anyEmail

    for {
      result <- keycloakAccountRepository.updateUserAttributes(email, anyAttributes).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val keycloakUserNotFound = KeycloakUserNotFound(email)
        expect(throwable == keycloakUserNotFound)
    }
  }

  beforeTest(
    """GIVEN an existing user and new roles
      | WHEN updateUserRoles is called
      | THEN the user roles are properly updated in Keycloak
      |""".stripMargin
  ) { keycloakAccountRepository =>
    val roles = List(User)
    val user = anyUser.copy(roles = roles)
    val newRoles = SuperAdmin :: OfficeManager :: roles

    for {
      _ <- keycloakAccountRepository.createUser(user)
      _ <- keycloakAccountRepository.updateUserRoles(user.email, newRoles)
      readUser <- keycloakAccountRepository.findUserByEmail(user.email)
    } yield forEach(newRoles) { newRole =>
      expect(readUser.roles.contains(newRole))
    }
  }

  beforeTest(
    """WHEN updateUserRoles is called with non-existent user email
      |THEN the call should fail with KeycloakUserNotFound
      |""".stripMargin
  ) { keycloakAccountRepository =>
    val email = anyEmail

    for {
      result <- keycloakAccountRepository.updateUserRoles(email, anyRoles).attempt
    } yield matches(result) {
      case Left(throwable) =>
        val keycloakUserNotFound = KeycloakUserNotFound(email)
        expect(throwable == keycloakUserNotFound)
    }
  }

  beforeTest(
    """GIVEN an existing user
      | WHEN disableUser is called
      | THEN the user is disabled in Keycloak
      |""".stripMargin
  ) { keycloakAccountRepository =>
    val user = anyUser

    for {
      _ <- keycloakAccountRepository.createUser(user)
      _ <- keycloakAccountRepository.disableUser(user.email)
      readUser <- keycloakAccountRepository.findUserByEmail(user.email)
    } yield expect(!readUser.isEnabled)
  }

  beforeTest(
    """WHEN disableUser is called with non-existent user email
      |THEN the call should not fail (no-op)
      |""".stripMargin
  ) { keycloakAccountRepository =>
    val email = anyEmail

    for {
      _ <- keycloakAccountRepository.disableUser(email)
    } yield success
  }

  private def deleteAllUsers(keycloak: Keycloak) =
    for {
      existingUserIds <- getAllUserIds(keycloak)
      _ <- existingUserIds.traverse(deleteUser(keycloak))
    } yield ()

  private def getAllUserIds(keycloak: Keycloak) =
    IO(keycloak.realm(realmName).users().list().asScala.toList.map(_.getId))

  private def deleteUser(keycloak: Keycloak)(id: String) =
    IO(keycloak.realm(realmName).users().delete(id))

  private lazy val anyUser = KeycloakUser(
    email = anyEmail,
    firstName = "Test",
    lastName = "User",
    roles = anyRoles,
    attributes = anyAttributes
  )

  private lazy val anyEmail = "test.user@keycloak.localhost"

  private lazy val anyRoles = List(User)

  private lazy val anyAttributes = List(AccountId(UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40")))
}
