package io.github.avapl
package adapters.keycloak.auth.repository

import adapters.auth.model.PublicKey
import adapters.auth.repository.PublicKeyRepository
import cats.Monad
import cats.effect.Concurrent
import cats.effect.Deferred
import cats.effect.kernel.Sync
import cats.syntax.all._
import org.keycloak.admin.client.Keycloak
import org.keycloak.crypto.KeyUse
import scala.jdk.CollectionConverters._

/**
 * @param deferred
 *   Used to memoize the public key fetch result
 */
class KeycloakPublicKeyRepository[F[_]: Monad] private (
  deferred: Deferred[F, PublicKey]
)(
  fetchPublicKey: F[PublicKey]
) extends PublicKeyRepository[F] {

  override def get: F[PublicKey] =
    deferred.tryGet.flatMap {
      case Some(publicKey) => publicKey.pure
      case None            => fetchPublicKeyAndMemoize
    }

  private def fetchPublicKeyAndMemoize =
    for {
      publicKey <- fetchPublicKey
      _ <- deferred.complete(publicKey)
    } yield publicKey
}

object KeycloakPublicKeyRepository {

  def apply[F[_]: Concurrent: Sync](
    keycloak: Keycloak,
    realmName: String
  ): F[KeycloakPublicKeyRepository[F]] =
    KeycloakPublicKeyRepository[F](fetchPublicKey(keycloak, realmName))

  private[repository] def apply[F[_]: Concurrent](
    fetchPublicKey: F[PublicKey]
  ): F[KeycloakPublicKeyRepository[F]] =
    Deferred[F, PublicKey].map { deferred =>
      new KeycloakPublicKeyRepository[F](deferred)(fetchPublicKey)
    }

  private def fetchPublicKey[F[_]: Sync](keycloak: Keycloak, realmName: String) = Sync[F].delay {
    keycloak
      .realm(realmName)
      .keys()
      .getKeyMetadata
      .getKeys
      .asScala
      .find { key =>
        key.getStatus == "ACTIVE" &&
        key.getAlgorithm == "RS256" &&
        key.getUse.equals(KeyUse.SIG)
      }
      .getOrElse(throw new RuntimeException("Could not find Keycloak active RS256 signature key"))
      .getPublicKey
  }
}
