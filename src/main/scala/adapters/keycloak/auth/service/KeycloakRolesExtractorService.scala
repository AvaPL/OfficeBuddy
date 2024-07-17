package io.github.avapl
package adapters.keycloak.auth.service

import adapters.auth.service.RolesExtractorService
import adapters.keycloak.repository.account.KeycloakRole
import domain.model.account.Role
import io.circe.Json

object KeycloakRolesExtractorService extends RolesExtractorService {

  override def extract(json: Json): List[Role] =
    json.hcursor
      .downField("realm_access")
      .downField("roles")
      .as[List[String]]
      .getOrElse(Nil)
      .flatMap(KeycloakRole.withValueOpt)
      .map(_.toDomain)
}
