package io.github.avapl
package adapters.keycloak.repository.account

import cats.effect.IO
import cats.effect.Resource
import cats.syntax.all._
import domain.model.error.account.DuplicateAccountEmail
import io.github.avapl.adapters.keycloak.fixture.KeycloakFixture
import org.keycloak.admin.client.Keycloak
import scala.jdk.CollectionConverters._
import weaver.Expectations
import weaver.IOSuite
import weaver.TestName

object KeycloakAccountRepositorySuite extends IOSuite with KeycloakFixture {

  private def beforeTest(name: TestName)(run: KeycloakAccountRepository[IO] => IO[Expectations]): Unit =
    test(name) { keycloak =>
      lazy val keycloakAccountRepository = new KeycloakAccountRepository[IO](keycloak, realmName)
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
      | THEN the call should fail with KeycloakUserNotFound
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
    """GIVEN an existing user and new attributes
      | WHEN updateUserAttributes is called
      | THEN the user attributes are properly updated in Keycloak
      |""".stripMargin
  ) { keycloakAccountRepository =>
    val attributes = Map(
      "one" -> List("1"),
      "two" -> List("2", "22")
    )
    val user = anyUser.copy(attributes = attributes)
    val newAttributes = Map(
      "two" -> List("22", "222"),
      "three" -> List("3")
    )

    for {
      _ <- keycloakAccountRepository.createUser(user)
      _ <- keycloakAccountRepository.updateUserAttributes(user.email, newAttributes)
      readUser <- keycloakAccountRepository.findUserByEmail(user.email)
    } yield expect(readUser.attributes == newAttributes)
  }

  beforeTest(
    """WHEN updateUserAttributes is called with non-existent user email
      | THEN the call should fail with KeycloakUserNotFound
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

  private def deleteAllUsers(keycloak: Keycloak) =
    for {
      existingUserIds <- IO(safeGetAllUserIds(keycloak))
      _ <- existingUserIds.traverse(safeDeleteUser(keycloak))
    } yield ()

  private def safeGetAllUserIds(keycloak: Keycloak) =
    keycloak.realm(realmName).users().list().asScala.toList.map(_.getId)

  private def safeDeleteUser(keycloak: Keycloak)(id: String) =
    IO(keycloak.realm(realmName).users().delete(id))

  private lazy val anyUser = KeycloakUser(
    email = anyEmail,
    firstName = "Test",
    lastName = "User",
    attributes = anyAttributes
  )

  private lazy val anyEmail = "test.user@keycloak.localhost"

  private lazy val anyAttributes: Map[String, List[String]] = Map(
    "test_attribute" -> List("test", "values")
  )
}
