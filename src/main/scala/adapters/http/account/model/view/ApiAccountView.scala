package io.github.avapl
package adapters.http.account.model.view

import adapters.http.account.ApiRole
import derevo.derive
import java.util.UUID
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("AccountView")
case class ApiAccountView(
  id: UUID,
  firstName: String,
  lastName: String,
  email: String,
  role: ApiRole,
  assignedOfficeId: Option[UUID],
  managedOfficeIds: List[UUID]
)
