package io.github.avapl
package domain.model.account

import domain.model.account.Role.OfficeManager
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
import weaver.SimpleIOSuite

object RoleSuite extends SimpleIOSuite {

  pureTest(
    """GIVEN User role and required roles
      | WHEN required role is User
      | THEN User should have access
      |""".stripMargin
  ) {
    expect.all(
      User.hasAccess(requiredRole = User),
      !User.hasAccess(requiredRole = OfficeManager),
      !User.hasAccess(requiredRole = SuperAdmin)
    )
  }

  pureTest(
    """GIVEN OfficeManager role and required roles
      | WHEN required role is User or OfficeManager
      | THEN OfficeManager should have access
      |""".stripMargin
  ) {
    expect.all(
      OfficeManager.hasAccess(requiredRole = User),
      OfficeManager.hasAccess(requiredRole = OfficeManager),
      !OfficeManager.hasAccess(requiredRole = SuperAdmin)
    )
  }

  pureTest(
    """GIVEN SuperAdmin role and required roles
      | WHEN required role is User, OfficeManager or SuperAdmin
      | THEN SuperAdmin should have access
      |""".stripMargin
  ) {
    expect.all(
      SuperAdmin.hasAccess(requiredRole = User),
      SuperAdmin.hasAccess(requiredRole = OfficeManager),
      SuperAdmin.hasAccess(requiredRole = SuperAdmin)
    )
  }
}
