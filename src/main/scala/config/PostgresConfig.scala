package io.github.avapl
package config

import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import pureconfig.ConfigReader
import pureconfig.generic.semiauto._
import pureconfig.module.ip4s._

case class PostgresConfig(
  host: Host,
  port: Port,
  user: String,
  password: String,
  database: String,
  maxConcurrentSessions: Int
)

object PostgresConfig {

  implicit val reader: ConfigReader[PostgresConfig] = deriveReader
}
