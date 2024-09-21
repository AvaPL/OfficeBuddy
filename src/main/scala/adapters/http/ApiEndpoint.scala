package io.github.avapl
package adapters.http

import adapters.auth.model.AccessToken
import adapters.auth.model.MissingAccountId
import adapters.auth.repository.PublicKeyRepository
import adapters.auth.service.ClaimsExtractorService
import cats.MonadThrow
import cats.effect.Clock
import cats.syntax.all._
import domain.model.account.Role
import io.circe.ParsingFailure
import pdi.jwt.exceptions.JwtException
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.PartialServerEndpoint

sealed trait ApiEndpoint {

  protected def apiEndpointName: String

  private[http] lazy val endpointBase: Endpoint[Unit, Unit, ApiError, Unit, Any] =
    endpoint
      .in(apiEndpointName)
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(
            statusCode(StatusCode.BadRequest) and jsonBody[ApiError.BadRequest]
              .description("Malformed parameters or request body")
          ),
          oneOfDefaultVariant(
            statusCode(StatusCode.InternalServerError) and jsonBody[ApiError.InternalServerError]
              .description("Internal server error")
          )
        )
      )
}

trait PublicApiEndpoint extends ApiEndpoint {

  protected[http] lazy val publicEndpoint: Endpoint[Unit, Unit, ApiError, Unit, Any] =
    endpointBase
      .withTag(apiEndpointName)
}

trait SecuredApiEndpoint[F[_]] extends ApiEndpoint {

  protected def claimsExtractor: ClaimsExtractorService
  protected def publicKeyRepository: PublicKeyRepository[F]

  protected[http] def securedEndpoint(
    requiredRole: Role
  )(implicit
    clock: Clock[F],
    monadThrow: MonadThrow[F]
  ): PartialServerEndpoint[String, AccessToken, Unit, ApiError, Unit, Any, F] =
    endpointBase
      .withTag(s"$apiEndpointName (secured)")
      .securityIn(auth.bearer[String]())
      .serverSecurityLogic(authorize(_, requiredRole))
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.Unauthorized) and emptyOutputAs(ApiError.Unauthorized)
            .description("Unauthorized")
        )
      )
      .errorOutVariantPrepend(
        oneOfVariant(
          statusCode(StatusCode.Forbidden) and emptyOutputAs(ApiError.Forbidden)
            .description("Forbidden")
        )
      )

  private def authorize(bearer: String, requiredRole: Role)(implicit
    clock: Clock[F],
    monadThrow: MonadThrow[F]
  ): F[Either[ApiError, AccessToken]] =
    for {
      publicKey <- publicKeyRepository.get
      accessToken <- authorize(bearer, publicKey, requiredRole)
    } yield accessToken

  private def authorize(bearer: String, publicKey: String, requiredRole: Role)(implicit
    clock: Clock[F],
    monadThrow: MonadThrow[F]
  ) =
    AccessToken
      .decode(bearer, claimsExtractor, publicKey)
      .map { accessToken =>
        if (accessToken.roles.exists(_.hasAccess(requiredRole))) accessToken.asRight[ApiError]
        else ApiError.Forbidden.asLeft
      }
      .recover {
        case _: JwtException   => ApiError.Unauthorized.asLeft
        case _: ParsingFailure => ApiError.Unauthorized.asLeft
        case MissingAccountId  => ApiError.Unauthorized.asLeft
      }
}
