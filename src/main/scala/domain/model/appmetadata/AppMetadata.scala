package io.github.avapl
package domain.model.appmetadata

import io.circe.Json

/**
 * Represents persistent data associated with application instance.
 *
 * @example
 *   Store a global variable that tells whether the demo data is already loaded via [[AppMetadata.IsDemoDataLoaded]].
 */
sealed trait AppMetadata {

  def key: String
  def valueJson: Json
}

object AppMetadata {

  case class IsDemoDataLoaded(value: Boolean) extends AppMetadata {

    override val key: String = IsDemoDataLoaded.key
    override val valueJson: Json = Json.fromBoolean(value)
  }

  object IsDemoDataLoaded {
    val key: String = "is_demo_data_loaded"
  }
}
