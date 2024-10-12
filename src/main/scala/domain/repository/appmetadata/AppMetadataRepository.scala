package io.github.avapl
package domain.repository.appmetadata

import domain.model.appmetadata.AppMetadata
import domain.model.appmetadata.AppMetadataKey

trait AppMetadataRepository[F[_]] {

  def get(key: AppMetadataKey): F[Option[AppMetadata]]
  def set(appMetadata: AppMetadata): F[AppMetadata]
}
