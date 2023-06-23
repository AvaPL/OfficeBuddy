package io.github.avapl
package domain.model.office

import java.util.UUID

case class Office(
  id: UUID,
  name: String,
  notes: List[String],
  //
  address: Address,
)
