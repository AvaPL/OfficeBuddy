package io.github.avapl
package adapters.keycloak.repository

import scala.util.control.NoStackTrace

package object account {

  case class KeycloakUserNotFound(email: String) extends NoStackTrace
}
