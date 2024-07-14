package io.github.avapl
package adapters.keycloak.auth.repository

import adapters.auth.model.PublicKey
import cats.effect.Deferred
import cats.effect.IO
import org.mockito.MockitoSugar
import weaver.SimpleIOSuite

object KeycloakPublicKeyRepositorySuite extends SimpleIOSuite with MockitoSugar {

  test(
    """GIVEN a function to fetch a public key
      | WHEN get is called
      | THEN the public key is returned
      |""".stripMargin
  ) {
    val publicKey = "publicKey"
    val fetchPublicKey = IO.pure(publicKey)

    for {
      keycloakPublicKeyProvider <- KeycloakPublicKeyRepository(fetchPublicKey)
      providerPublicKey <- keycloakPublicKeyProvider.get
    } yield expect(providerPublicKey == publicKey)
  }

  test(
    """GIVEN a function to fetch a public key
      | WHEN get is called twice
      | THEN the public key is fetched only once and memoized
      |""".stripMargin
  ) {
    val fetchPublicKeyFunction = mock[() => String]
    when(fetchPublicKeyFunction()) thenReturn "publicKey"
    val fetchPublicKey = IO(fetchPublicKeyFunction())

    for {
      keycloakPublicKeyProvider <- KeycloakPublicKeyRepository(fetchPublicKey)
      _ <- keycloakPublicKeyProvider.get
      _ <- keycloakPublicKeyProvider.get
    } yield {
      verify(fetchPublicKeyFunction, times(1))()
      success
    }
  }
}
