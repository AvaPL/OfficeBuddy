package io.github.avapl
package adapters.keycloak.repository.account

import cats.effect.IO
import cats.effect.Resource
import domain.model.error.account.DuplicateAccountEmail
import org.keycloak.admin.client.Keycloak
import weaver.IOSuite

object KeycloakAccountRepositorySuite extends IOSuite {

  override type Res = Keycloak

  override def sharedResource: Resource[IO, Res] =
    Resource.make(
      IO(Keycloak.getInstance("http://localhost:8888", "master", "keycloak", "keycloak", "admin-cli"))
    )(keycloak => IO(keycloak.close()))

  // TODO: Add beforeEach cleanup

  test(
    """GIVEN a user to create
      | WHEN createUser is called
      | THEN the user is properly created in Keycloak
      |""".stripMargin
  ) { keycloak =>
    val keycloakAccountRepository = new KeycloakAccountRepository[IO](keycloak, "office-buddy")
    val user = anyUser

    for {
      _ <- keycloakAccountRepository.createUser(user)
      readUser <- keycloakAccountRepository.findUserByEmail(user.email)
    } yield expect(readUser == user)
  }

  test(
    """GIVEN a user that already exists in Keycloak
      | WHEN createUser is called
      | THEN the call should fail with DuplicateAccountEmail
      |""".stripMargin
  ) { keycloak =>
    val keycloakAccountRepository = new KeycloakAccountRepository[IO](keycloak, "office-buddy")
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

  private lazy val anyUser = KeycloakUser(
    email = "test.user@keycloak.localhost",
    firstName = "Test",
    lastName = "User"
  )
}
