package io.github.avapl
package domain.repository.appmetadata

import domain.model.appmetadata.AppMetadata
import domain.model.appmetadata.AppMetadataKey

trait AppMetadataRepository[F[_]] {

  // TODO: How to make the return type more specific?
  //  E.g. Return type should be IsDemoDataLoaded for AppMetadataKey.IsDemoDataLoaded
  def get(key: AppMetadataKey): F[Option[AppMetadata]]
  def set(appMetadata: AppMetadata): F[AppMetadata]
}
