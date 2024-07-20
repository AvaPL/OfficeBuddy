package io.github.avapl
package adapters.keycloak.auth.service

import adapters.auth.service.ClaimsExtractorService
import adapters.keycloak.repository.account.KeycloakRole
import domain.model.account.Role
import io.circe.Json
import java.util.UUID

object KeycloakClaimsExtractorService extends ClaimsExtractorService {

  override def extractRoles(json: Json): List[Role] =
    json.hcursor
      .downField("realm_access")
      .downField("roles")
      .as[List[String]]
      .getOrElse(Nil)
      .flatMap(KeycloakRole.withValueOpt)
      .map(_.toDomain)

  override def extractAccountId(json: Json): Option[UUID] =
    json.hcursor
      .downField("domain_attributes")
      .get[UUID]("account_id")
      .toOption
}
