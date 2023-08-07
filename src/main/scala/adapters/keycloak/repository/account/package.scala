package io.github.avapl
package adapters.keycloak.repository

import scala.jdk.CollectionConverters._
import scala.language.implicitConversions
import scala.util.control.NoStackTrace

package object account {

  case class KeycloakUserNotFound(email: String) extends NoStackTrace

  implicit def scalaMapListToJava[K, V](map: Map[K, List[V]]): java.util.Map[K, java.util.List[V]] =
    map.view.mapValues(_.asJava).toMap.asJava

  implicit def javaMapListToScala[K, V](map: java.util.Map[K, java.util.List[V]]): Map[K, List[V]] =
    map.asScala.view.mapValues(_.asScala.toList).toMap
}
