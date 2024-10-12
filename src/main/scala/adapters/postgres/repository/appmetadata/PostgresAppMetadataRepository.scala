package io.github.avapl
package adapters.postgres.repository.appmetadata

import cats.effect.kernel.MonadCancelThrow
import cats.effect.kernel.Resource
import cats.syntax.all._
import domain.model.appmetadata.AppMetadata
import domain.model.appmetadata.AppMetadataKey
import domain.repository.appmetadata.AppMetadataRepository
import io.circe.Json
import skunk._
import skunk.circe.codec.all._
import skunk.codec.all._
import skunk.implicits._

class PostgresAppMetadataRepository[F[_]: MonadCancelThrow](
  session: Resource[F, Session[F]]
) extends AppMetadataRepository[F] {

  import PostgresAppMetadataRepository._

  override def get(key: AppMetadataKey): F[Option[AppMetadata]] =
    session.use { session =>
      for {
        sql <- session.prepare(getSql)
        appMetadata <- sql.option(key)
      } yield appMetadata
    }

  private lazy val getSql: Query[AppMetadataKey, AppMetadata] = {
    val sql: Query[String, AppMetadata] = sql"""
      SELECT key, value
      FROM app_metadata
      WHERE key = $varchar
    """.query(appMetadataDecoder)
    sql.contramap(_.value)
  }

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
      appMetadata.key.value *:
        appMetadata.valueJson *:
        EmptyTuple
    }

  private lazy val appMetadataDecoder: Decoder[AppMetadata] =
    (
      varchar *: // key
        jsonb // value
    ).emap {
      case key *: valueJson *: EmptyTuple =>
        for {
          key <- parseKey(key)
          appMetadata <- parseValue(key, valueJson)
        } yield appMetadata
    }

  private def parseKey(key: String) =
    AppMetadataKey.withValueEither(key).leftMap(_.getMessage)

  private def parseValue(key: AppMetadataKey, valueJson: Json) =
    key match {
      case AppMetadataKey.IsDemoDataLoaded =>
        valueJson
          .as[Boolean]
          .map(AppMetadata.IsDemoDataLoaded)
          .leftMap(_.message)
    }
}
