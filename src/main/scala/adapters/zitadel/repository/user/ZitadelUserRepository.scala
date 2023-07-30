package io.github.avapl
package adapters.zitadel.repository.user

import com.zitadel.management.v1.management.AddHumanUserRequest
import domain.repository.user.UserRepository
import io.github.avapl.domain.model.user.UserAccount

class ZitadelUserRepository[F[_]] extends UserRepository[F] {

  override def createUser(user: UserAccount): F[UserAccount] = {
    val addHumanUserRequest = AddHumanUserRequest(
      userName = user.email,
      profile = Some(
        AddHumanUserRequest.Profile(
          firstName = user.firstName,
          lastName = user.lastName,
          displayName = s"${user.firstName} ${user.lastName}"
        )
      ),
      email = Some(AddHumanUserRequest.Email(user.email))
    )
  }
}
