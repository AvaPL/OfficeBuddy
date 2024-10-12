package io.github.avapl
package domain.model.appmetadata

import enumeratum.values.StringEnum
import enumeratum.values.StringEnumEntry

sealed abstract class AppMetadataKey(override val value: String) extends StringEnumEntry

object AppMetadataKey extends StringEnum[AppMetadataKey] {

  case object IsDemoDataLoaded extends AppMetadataKey("is_demo_data_loaded")

  override val values: IndexedSeq[AppMetadataKey] = findValues
}
