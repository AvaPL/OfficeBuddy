package io.github.avapl
package adapters.auth.model

import adapters.auth.service.ClaimsExtractorService
import cats.MonadThrow
import cats.effect.Clock
import cats.syntax.all._
import domain.model.account.Role
import java.time.Instant
import java.time.ZoneOffset
import java.util.UUID
import pdi.jwt._

case class AccessToken(
  reservedClaims: JwtClaim,
  roles: List[Role],
  accountId: UUID
)

object AccessToken {

  def decode[F[_]: Clock: MonadThrow](
    bearer: String,
    claimsExtractor: ClaimsExtractorService,
    publicKey: String
  ): F[AccessToken] =
    for {
      now <- Clock[F].realTimeInstant
      accessToken <- decode(bearer, claimsExtractor, publicKey, now)
    } yield accessToken

  private def decode[F[_]: MonadThrow](
    bearer: String,
    claimsExtractor: ClaimsExtractorService,
    publicKey: String,
    now: Instant
  ) =
    MonadThrow[F].fromTry {
      val fixedJavaClock = java.time.Clock.fixed(now, ZoneOffset.UTC)
      new Parser(fixedJavaClock, claimsExtractor).decode(bearer, publicKey, List(JwtAlgorithm.RS256))
    }

  private class Parser(
    override val clock: java.time.Clock,
    claimsExtractor: ClaimsExtractorService
  ) extends JwtCirceParser[JwtHeader, AccessToken] {

    override protected def parseClaim(claim: String): AccessToken = {
      val reservedClaims = JwtCirce.parseClaim(claim)
      val jsonJwtContent = parse(reservedClaims.content)
      val roles = claimsExtractor.extractRoles(jsonJwtContent)
      val accountId = claimsExtractor.extractAccountId(jsonJwtContent).getOrElse(throw MissingAccountId)
      AccessToken(reservedClaims, roles, accountId)
    }

    override protected def parseHeader(header: String): JwtHeader =
      JwtCirce.parseHeader(header)
    override protected def extractExpiration(claim: AccessToken): Option[Long] =
      super.extractExpiration(claim.reservedClaims)
    override protected def extractNotBefore(claim: AccessToken): Option[Long] =
      super.extractNotBefore(claim.reservedClaims)
  }
}
