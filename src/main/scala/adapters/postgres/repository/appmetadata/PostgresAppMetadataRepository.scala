package io.github.avapl
package adapters.postgres.repository.appmetadata

import cats.effect.kernel.MonadCancelThrow
import cats.effect.kernel.Resource
import cats.syntax.all._
import domain.model.appmetadata.AppMetadata
import domain.model.appmetadata.AppMetadata.IsDemoDataLoaded
import domain.repository.appmetadata.AppMetadataRepository
import scala.reflect.ClassTag
import skunk._
import skunk.circe.codec.all._
import skunk.codec.all._
import skunk.implicits._

class PostgresAppMetadataRepository[F[_]: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends AppMetadataRepository[F] {

  import PostgresAppMetadataRepository._

  override def get[M <: AppMetadata](implicit classTag: ClassTag[M]): F[Option[M]] = {
    val (key, decoder) = getKeyAndDecoder[M]
    session.use { session =>
      for {
        sql <- session.prepare(getSql(decoder))
        appMetadata <- sql.option(key)
      } yield appMetadata
    }
  }

  private def getKeyAndDecoder[M <: AppMetadata](implicit classTag: ClassTag[M]): (String, Decoder[M]) = {
    val decoders = Map[Class[_], (String, Decoder[_])](
      classOf[IsDemoDataLoaded] -> (IsDemoDataLoaded.key, isDemoDataLoadedDecoder)
    )
    decoders(classTag.runtimeClass).map(_.asInstanceOf[Decoder[M]])
  }

  private def getSql[M <: AppMetadata](decoder: Decoder[M]): Query[String, M] =
    sql"""
      SELECT value
      FROM app_metadata
      WHERE key = $varchar
    """.query(decoder)

  override def set(appMetadata: AppMetadata): F[AppMetadata] =
    session.use { session =>
      for {
        sql <- session.prepare(setSql)
        _ <- sql.execute(appMetadata)
      } yield appMetadata
    }

  private lazy val setSql: Command[AppMetadata] =
    sql"""
      INSERT INTO app_metadata
      VALUES ($appMetadataEncoder)
      ON CONFLICT (key)
      DO UPDATE SET value = EXCLUDED.value
    """.command
}

object PostgresAppMetadataRepository {

  private lazy val appMetadataEncoder: Encoder[AppMetadata] =
    (
      varchar *: // key
        jsonb // value
    ).contramap[AppMetadata] { appMetadata =>
      appMetadata.key *:
        appMetadata.valueJson *:
        EmptyTuple
    }

  private lazy val isDemoDataLoadedDecoder: Decoder[IsDemoDataLoaded] =
    jsonb.emap {
      _.as[Boolean]
        .map(IsDemoDataLoaded(_))
        .leftMap(_.message)
    }
}
