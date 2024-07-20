package io.github.avapl
package adapters.keycloak.repository.account

import domain.model.account.OfficeManagerAccount
import domain.model.account.SuperAdminAccount
import domain.model.account.UserAccount
import java.util.UUID
import weaver.SimpleIOSuite

object KeycloakUserSuite extends SimpleIOSuite {

  pureTest(
    """GIVEN a UserAccount
      | WHEN it is converted into a KeycloakUser
      | THEN it should have personal details, role and attributes mapped
      |""".stripMargin
  ) {
    val userAccount = UserAccount(
      id = anyAccountId,
      firstName = "Test",
      lastName = "User",
      email = "test.user@localhost",
      assignedOfficeId = Some(anyOfficeId)
    )
    val expectedKeycloakUser = KeycloakUser(
      email = userAccount.email,
      firstName = userAccount.firstName,
      lastName = userAccount.lastName,
      roles = List(KeycloakRole.User),
      attributes = List(
        KeycloakAttribute.AccountId(userAccount.id)
      ),
      isEnabled = true
    )

    val keycloakUser = KeycloakUser.fromUserAccount(userAccount)

    expect(keycloakUser == expectedKeycloakUser)
  }

  pureTest(
    """GIVEN an OfficeManagerAccount
      | WHEN it is converted into a KeycloakUser
      | THEN it should have personal details, role and attributes mapped
      |""".stripMargin
  ) {
    val officeManagerAccount = OfficeManagerAccount(
      id = anyAccountId,
      firstName = "Test",
      lastName = "User",
      email = "test.office.manager@localhost",
      managedOfficeIds = List(anyOfficeId)
    )
    val expectedKeycloakUser = KeycloakUser(
      email = officeManagerAccount.email,
      firstName = officeManagerAccount.firstName,
      lastName = officeManagerAccount.lastName,
      roles = List(KeycloakRole.OfficeManager),
      attributes = List(
        KeycloakAttribute.AccountId(officeManagerAccount.id),
        KeycloakAttribute.ManagedOfficeIds(officeManagerAccount.managedOfficeIds)
      ),
      isEnabled = true
    )

    val keycloakUser = KeycloakUser.fromOfficeManagerAccount(officeManagerAccount)

    expect(keycloakUser == expectedKeycloakUser)
  }

  pureTest(
    """GIVEN a SuperAdminAccount
      | WHEN it is converted into a KeycloakUser
      | THEN it should have personal details, role and attributes mapped
      |""".stripMargin
  ) {
    val superAdminAccount = SuperAdminAccount(
      id = anyAccountId,
      firstName = "Test",
      lastName = "User",
      email = "test.super.admin@localhost"
    )
    val expectedKeycloakUser = KeycloakUser(
      email = superAdminAccount.email,
      firstName = superAdminAccount.firstName,
      lastName = superAdminAccount.lastName,
      roles = List(KeycloakRole.SuperAdmin),
      attributes = List(
        KeycloakAttribute.AccountId(superAdminAccount.id)
      ),
      isEnabled = true
    )

    val keycloakUser = KeycloakUser.fromSuperAdminAccount(superAdminAccount)

    expect(keycloakUser == expectedKeycloakUser)
  }

  pureTest(
    """GIVEN a KeycloakUser
      | WHEN it is converted back and forth from a UserRepresentation
      | THEN it should have the same personal details, roles and attributes
      |""".stripMargin
  ) {
    val keycloakUser = KeycloakUser(
      email = "test.user@keycloak.localhost",
      firstName = "Test",
      lastName = "User",
      roles = List(KeycloakRole.OfficeManager),
      attributes = List(
        KeycloakAttribute.AccountId(anyAccountId),
        KeycloakAttribute.ManagedOfficeIds(List(anyOfficeId))
      )
    )

    val transformedKeycloakUser = KeycloakUser.fromUserRepresentation(keycloakUser.toUserRepresentation)

    expect(transformedKeycloakUser == keycloakUser)
  }

  private lazy val anyAccountId = UUID.fromString("9104d3d5-9b7b-4296-aab0-dd76c1af6a40")

  private lazy val anyOfficeId = UUID.fromString("214e1fc1-4095-479e-b71f-6888146bbeed")
}
