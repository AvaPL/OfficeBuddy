package io.github.avapl
package adapters.facade.repository.account

import adapters.keycloak.fixture.KeycloakFixture
import adapters.postgres.fixture.PostgresFixture
import cats.effect.IO
import cats.effect.Resource
import cats.syntax.all._
import java.util.UUID
import org.keycloak.admin.client.Keycloak
import skunk.Session
import weaver.IOSuite

object KeycloakPostgresAccountRepositorySuite extends IOSuite {

  override type Res = (Keycloak, Resource[IO, Session[IO]])

  override def sharedResource: Resource[IO, Res] =
    KeycloakFixture.resource product PostgresFixture.resource

  test(
    """GIVEN KeycloakPostgresAccountRepository
      | WHEN Keycloak and Postgres are initialized
      | THEN super admin with all-0 UUID is created
      |""".stripMargin
  ) {
    case (keycloak, postgres) =>
      val superAdminId = UUID.fromString("00000000-0000-0000-0000-000000000000")
      val repository = KeycloakPostgresAccountRepository(keycloak, KeycloakFixture.realmName, postgres)

      for {
        _ <- repository.read(superAdminId)
      } yield success
  }
}
