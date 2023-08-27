package io.github.avapl
package config

import pureconfig.ConfigReader
import pureconfig.generic.semiauto._

case class AppConfig(
  postgres: PostgresConfig,
  keycloak: KeycloakConfig,
  http: HttpConfig
)

object AppConfig {

  implicit val reader: ConfigReader[AppConfig] = deriveReader
}
