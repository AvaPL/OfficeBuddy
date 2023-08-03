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
    email = "test.user@keycloak.localhost",
    firstName = "Test",
    lastName = "User"
  )
}
