package io.github.avapl
package util

import cats.effect.Sync
import java.util.UUID

trait FUUID[F[_]] {

  def randomUUID(): F[UUID]
}

object FUUID {

  def apply[F[_]: FUUID]: FUUID[F] = implicitly

  implicit def forSync[F[_]: Sync]: FUUID[F] =
    () => Sync[F].delay(UUID.randomUUID())
}
