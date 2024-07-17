package io.github.avapl
package adapters.auth.service

import domain.model.account.Role
import io.circe.Json

trait RolesExtractorService {

  def extract(json: Json): List[Role]
}
