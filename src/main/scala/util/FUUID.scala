package io.github.avapl
package util

import cats.effect.Sync
import java.util.UUID

trait FUUID[F[_]] {

  def randomUUID(): F[UUID]
}

object FUUID {

  implicit def forSync[F[_]](implicit sync: Sync[F]): FUUID[F] =
    () => sync.delay(UUID.randomUUID())
}
