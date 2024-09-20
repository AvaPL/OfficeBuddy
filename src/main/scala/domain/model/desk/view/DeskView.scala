package io.github.avapl
package domain.model.desk.view

import java.util.UUID

case class DeskView(
  id: UUID,
  name: String,
  isAvailable: Boolean,
  notes: List[String],
  isStanding: Boolean,
  monitorsCount: Short,
  hasPhone: Boolean,
)
