package io.github.avapl
package adapters.keycloak.auth.service

import domain.model.account.Role.OfficeManager
import domain.model.account.Role.User
import io.circe.parser.parse
import java.util.UUID
import weaver.SimpleIOSuite

object KeycloakClaimsExtractorServiceSuite extends SimpleIOSuite {

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

    val extractedRoles = KeycloakClaimsExtractorService.extractRoles(keycloakRolesJwtPart)

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

    val extractedRoles = KeycloakClaimsExtractorService.extractRoles(jwt)

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

    val extractedRoles = KeycloakClaimsExtractorService.extractRoles(jwt)

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

    val extractedRoles = KeycloakClaimsExtractorService.extractRoles(jwt)

    expect(extractedRoles.isEmpty)
  }

  pureTest(
    """GIVEN a valid Keycloak JWT part with an account ID
      | WHEN the account ID is extracted
      | THEN it should correctly parse the account ID
      |""".stripMargin
  ) {
    val accountId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
    val jwt = parse {
      s"""{
         |  "domain_attributes": {
         |    "account_id": "$accountId"
         |  }
         |}
         |""".stripMargin
    }.toOption.get

    val extractedAccountId = KeycloakClaimsExtractorService.extractAccountId(jwt)

    expect(extractedAccountId.contains(accountId))
  }

  pureTest(
    """GIVEN a JWT without domain_attributes
      | WHEN the account ID is extracted
      | THEN it should return None
      |""".stripMargin
  ) {
    val jwt = parse {
      """{
        |  "other_attributes": {
        |    "account_id": "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        |  }
        |}
        |""".stripMargin
    }.toOption.get

    val extractedAccountId = KeycloakClaimsExtractorService.extractAccountId(jwt)

    expect(extractedAccountId.isEmpty)
  }

  pureTest(
    """GIVEN a JWT with domain_attributes but without an account ID
      | WHEN the account ID is extracted
      | THEN it should return None
      |""".stripMargin
  ) {
    val jwt = parse {
      """{
        |  "domain_attributes": {
        |  }
        |}
        |""".stripMargin
    }.toOption.get

    val extractedAccountId = KeycloakClaimsExtractorService.extractAccountId(jwt)

    expect(extractedAccountId.isEmpty)
  }

  pureTest(
    """GIVEN a JWT where account_id is not a UUID
      | WHEN the account ID is extracted
      | THEN it should return None
      |""".stripMargin
  ) {
    val jwt = parse {
      """{
        |  "domain_attributes": {
        |    "account_id": 12345
        |  }
        |}
        |""".stripMargin
    }.toOption.get

    val extractedAccountId = KeycloakClaimsExtractorService.extractAccountId(jwt)

    expect(extractedAccountId.isEmpty)
  }
}
