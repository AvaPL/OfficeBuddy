package io.github.avapl
package domain.model.account.view

import domain.model.account.Role
import java.util.UUID

case class AccountView(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  role: Role,
  assignedOffice: Option[OfficeView],
  managedOffices: List[OfficeView]
)
