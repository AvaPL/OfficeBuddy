package io.github.avapl
package adapters.keycloak.fixture

import cats.effect.IO
import cats.effect.Resource
import org.keycloak.admin.client.Keycloak
import weaver.IOSuite

trait KeycloakFixture {
  this: IOSuite =>

  override val maxParallelism: Int = 1
  override type Res = Keycloak

  val realmName = "office-buddy"

  override def sharedResource: Resource[IO, Res] =
    Resource.make(
      IO(Keycloak.getInstance("http://localhost:8888", "master", "keycloak", "keycloak", "admin-cli"))
    )(keycloak => IO(keycloak.close()))
}
