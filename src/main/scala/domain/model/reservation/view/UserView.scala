package io.github.avapl
package domain.model.reservation.view

import java.util.UUID

case class UserView(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String
)
