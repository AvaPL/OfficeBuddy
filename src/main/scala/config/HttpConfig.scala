package io.github.avapl
package config

import cats.data.NonEmptyList
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import pureconfig.ConfigReader
import pureconfig.generic.semiauto._
import pureconfig.module.cats._
import pureconfig.module.ip4s._

case class HttpConfig(
  host: Host,
  port: Port,
  swaggerUIPath: NonEmptyList[String],
  internalApiBasePath: NonEmptyList[String]
)

object HttpConfig {

  implicit val reader: ConfigReader[HttpConfig] = deriveReader
}
