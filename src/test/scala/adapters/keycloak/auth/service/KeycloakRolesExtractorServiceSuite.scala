package io.github.avapl
package adapters.keycloak.auth.service

import domain.model.account.Role.OfficeManager
import domain.model.account.Role.User
import io.circe.parser.parse
import weaver.SimpleIOSuite

object KeycloakRolesExtractorServiceSuite extends SimpleIOSuite {

  pureTest(
    """GIVEN a valid Keycloak JWT part with roles
      | WHEN the roles are extracted
      | THEN it should parse the domain roles and ignore others
      |""".stripMargin
  ) {
    val keycloakRolesJwtPart = parse {
      """{
        |  "realm_access": {
        |    "roles": [
        |      "default-roles-office-buddy",
        |      "offline_access",
        |      "office_buddy_user",
        |      "office_buddy_office_manager",
        |      "uma_authorization"
        |    ]
        |  }
        |}
        |""".stripMargin
    }.toOption.get

    val extractedRoles = KeycloakRolesExtractorService.extract(keycloakRolesJwtPart)

    expect(extractedRoles == List(User, OfficeManager))
  }

  pureTest(
    """GIVEN a JWT with realm_access but without roles
      | WHEN the roles are extracted
      | THEN it should return an empty list
      |""".stripMargin
  ) {
    val jwt = parse {
      """{
        |  "realm_access": {
        |    "other_collection": [
        |      "office_buddy_user"
        |    ]
        |  }
        |}
        |""".stripMargin
    }.toOption.get

    val extractedRoles = KeycloakRolesExtractorService.extract(jwt)

    expect(extractedRoles.isEmpty)
  }

  pureTest(
    """GIVEN a JWT without realm_access
      | WHEN the roles are extracted
      | THEN it should return an empty list
      |""".stripMargin
  ) {
    val jwt = parse {
      """{
        |  "roles": [
        |    "office_buddy_user"
        |  ]
        |}
        |""".stripMargin
    }.toOption.get

    val extractedRoles = KeycloakRolesExtractorService.extract(jwt)

    expect(extractedRoles.isEmpty)
  }

  pureTest(
    """GIVEN a JWT where realm_access is of the wrong type
      | WHEN the roles are extracted
      | THEN it should return an empty list
      |""".stripMargin
  ) {
    val jwt = parse {
      """{
        |  "realm_access": 123
        |}
        |""".stripMargin
    }.toOption.get

    val extractedRoles = KeycloakRolesExtractorService.extract(jwt)

    expect(extractedRoles.isEmpty)
  }
}
