package io.github.avapl
package adapters.auth.repository

import adapters.auth.model.PublicKey

trait PublicKeyRepository[F[_]] {

  def get: F[PublicKey]
}
