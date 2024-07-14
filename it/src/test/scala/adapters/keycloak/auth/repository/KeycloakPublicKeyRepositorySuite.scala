package io.github.avapl
package adapters.keycloak.auth.repository

import adapters.keycloak.fixture.KeycloakFixture
import weaver.IOSuite

object KeycloakPublicKeyRepositorySuite extends IOSuite with KeycloakFixture {

  test(
    """GIVEN Keycloak instance and realm name
      | WHEN get is called
      | THEN the public key is returned
      |""".stripMargin
  ) { keycloak =>
    for {
      keycloakPublicKeyProvider <- KeycloakPublicKeyRepository(keycloak, realmName)
      publicKey <- keycloakPublicKeyProvider.get
    } yield expect(publicKey.nonEmpty)
  }
}
