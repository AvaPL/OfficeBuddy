package io.github.avapl
package adapters.auth.service

import domain.model.account.Role
import io.circe.Json

trait RolesExtractor {

  def extract(json: Json): List[Role]
}
