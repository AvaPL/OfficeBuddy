package io.github.avapl
package domain.model.user

import java.util.UUID

// TODO: Mock model, will change after the introduction of auth
case class User(
  id: UUID,
  name: String,
  email: String,
  //
  officeId: UUID
)
