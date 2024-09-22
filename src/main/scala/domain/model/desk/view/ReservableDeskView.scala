package io.github.avapl
package domain.model.desk.view

import java.util.UUID

case class ReservableDeskView(
  id: UUID,
  name: String,
  isStanding: Boolean,
  monitorsCount: Short,
  hasPhone: Boolean
)
