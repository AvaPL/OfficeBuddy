package io.github.avapl
package adapters.auth.service

import domain.model.account.Role
import io.circe.Json
import java.util.UUID

trait ClaimsExtractorService {

  def extractRoles(json: Json): List[Role]

  def extractAccountId(json: Json): Option[UUID]
}
