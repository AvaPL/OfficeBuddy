package io.github.avapl
package domain.repository.user

import domain.model.user.UserAccount

trait UserRepository[F[_]] {

  def createUser(user: UserAccount): F[UserAccount]
}
