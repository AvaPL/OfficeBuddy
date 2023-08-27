package io.github.avapl
package config

import pureconfig.ConfigReader
import pureconfig.generic.semiauto._

case class KeycloakConfig(
  serverUrl: String,
  adminUser: String,
  adminPassword: String,
  adminClientId: String,
  masterRealmName: String,
  appRealmName: String
)

object KeycloakConfig {

  // TODO: Use derevo for config
  implicit val reader: ConfigReader[KeycloakConfig] = deriveReader
}
