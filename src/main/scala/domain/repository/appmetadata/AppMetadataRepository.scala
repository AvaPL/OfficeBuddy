package io.github.avapl
package domain.repository.appmetadata

import domain.model.appmetadata.AppMetadata
import scala.reflect.ClassTag

trait AppMetadataRepository[F[_]] {

  def get[M <: AppMetadata: ClassTag]: F[Option[M]]
  def set(appMetadata: AppMetadata): F[AppMetadata]
}
