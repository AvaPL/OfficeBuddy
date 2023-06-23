package io.github.avapl
package domain.model.desk

import java.util.UUID

case class Desk(
  id: UUID,
  name: String,
  isAvailable: Boolean,
  notes: List[String],
  //
  isStanding: Boolean,
  numberOfMonitors: Int,
  hasPhone: Boolean,
  //
  officeId: UUID
)
