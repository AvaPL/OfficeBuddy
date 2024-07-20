package io.github.avapl
package adapters.auth.model

import adapters.auth.service.ClaimsExtractorService
import cats.effect.{Clock => CatsClock}
import cats.effect.IO
import domain.model.account.Role
import io.circe.Json
import io.circe.ParsingFailure
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.time.{Clock => JavaClock}
import java.time.Instant
import java.util.Base64
import java.util.UUID
import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import pdi.jwt.JwtAlgorithm
import pdi.jwt.JwtCirce
import pdi.jwt.JwtClaim
import pdi.jwt.exceptions.JwtExpirationException
import pdi.jwt.exceptions.JwtValidationException
import scala.concurrent.duration.DurationInt
import weaver.SimpleIOSuite

object AccessTokenSuite extends SimpleIOSuite with MockitoSugar with MockitoCats {

  test(
    """GIVEN a valid JWT token and roles extractor
      | WHEN the token is decoded
      | THEN it should return correct AccessToken
      |""".stripMargin
  ) {
    val jwtClaim = JwtClaim()
    val (privateKey, publicKey) = generateRSAKeyPair()
    val encodedToken = JwtCirce(fixedJavaClock).encode(jwtClaim, privateKey, JwtAlgorithm.RS256)

    val roles = List(Role.User)
    val accountId = UUID.fromString("e1f5b3f0-7e1d-4d4c-8d5e-9b6f5b4f6b1e")
    val claimsExtractor: ClaimsExtractorService = new ClaimsExtractorService {
      override def extractRoles(json: Json): List[Role] = roles
      override def extractAccountId(json: Json): Option[UUID] = Some(accountId)
    }

    for {
      accessToken <- AccessToken.decode(encodedToken, claimsExtractor, publicKey)
    } yield {
      expect(accessToken.reservedClaims == jwtClaim)
      expect(accessToken.roles == roles)
      expect(accessToken.accountId == accountId)
    }
  }

  test(
    """GIVEN an invalid JWT token
      | WHEN the token is decoded
      | THEN it should fail with a parsing error
      |""".stripMargin
  ) {
    val invalidToken = "invalid.jwt.token"
    val (_, publicKey) = generateRSAKeyPair()

    for {
      result <- AccessToken.decode(invalidToken, anyClaimsExtractor, publicKey).attempt
    } yield matches(result) {
      case Left(_: ParsingFailure) => success
    }
  }

  test(
    """GIVEN a valid JWT token and not matching the public key
      | WHEN the token is decoded
      | THEN it should fail with a signature validation error
      |""".stripMargin
  ) {
    val jwtClaim = JwtClaim()
    val (privateKey, _) = generateRSAKeyPair(seed = 1)
    val encodedToken = JwtCirce(fixedJavaClock).encode(jwtClaim, privateKey, JwtAlgorithm.RS256)

    val (_, anotherPublicKey) = generateRSAKeyPair(seed = 2)

    for {
      result <- AccessToken.decode(encodedToken, anyClaimsExtractor, anotherPublicKey).attempt
    } yield matches(result) {
      case Left(_: JwtValidationException) => success
    }
  }

  test(
    """GIVEN a valid non-RS256 JWT token
      | WHEN the token is decoded
      | THEN it should fail with a decoding error
      |""".stripMargin
  ) {
    val jwtClaim = JwtClaim()
    val (privateKey, publicKey) = anyDSAKeyPair
    val encodedToken = JwtCirce(fixedJavaClock).encode(jwtClaim, privateKey, JwtAlgorithm.EdDSA)

    for {
      result <- AccessToken.decode(encodedToken, anyClaimsExtractor, publicKey).attempt
    } yield matches(result) {
      case Left(_: JwtValidationException) => success
    }
  }

  test(
    """GIVEN a valid JWT token that is expired
      | WHEN the token is decoded
      | THEN it should fail with an expiration error
      |""".stripMargin
  ) {
    val expirationSeconds = 100
    val jwtClaim = JwtClaim(expiration = Some(expirationSeconds))
    val (privateKey, publicKey) = generateRSAKeyPair()
    val encodedToken = JwtCirce(fixedJavaClock).encode(jwtClaim, privateKey, JwtAlgorithm.RS256)

    implicit val fixedCatsClockAfterExpiration: CatsClock[IO] =
      whenF(mock[CatsClock[IO]].realTime) thenReturn (expirationSeconds + 1).seconds

    for {
      result <- AccessToken
        .decode(encodedToken, anyClaimsExtractor, publicKey)
        .attempt
    } yield matches(result) {
      case Left(_: JwtExpirationException) => success
    }
  }

  test(
    """GIVEN a valid JWT token that doesn't contain an account ID
      | WHEN the token is decoded
      | THEN it should fail with a MissingAccountId error
      |""".stripMargin
  ) {
    val jwtClaim = JwtClaim()
    val (privateKey, publicKey) = generateRSAKeyPair()
    val encodedToken = JwtCirce(fixedJavaClock).encode(jwtClaim, privateKey, JwtAlgorithm.RS256)

    val claimsExtractor: ClaimsExtractorService = new ClaimsExtractorService {
      override def extractRoles(json: Json): List[Role] = Nil
      override def extractAccountId(json: Json): Option[UUID] = None
    }

    for {
      result <- AccessToken.decode(encodedToken, claimsExtractor, publicKey).attempt
    } yield matches(result) {
      case Left(MissingAccountId) => success
    }
  }

  private def generateRSAKeyPair(seed: Int = 0) = {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
    keyPairGenerator.initialize(2048, new SecureRandom(seed.toString.getBytes))
    val keyPair = keyPairGenerator.generateKeyPair()
    val publicKeyString = new String(Base64.getEncoder.encode(keyPair.getPublic.getEncoded))
    (keyPair.getPrivate, publicKeyString)
  }

  private lazy val fixedJavaClock =
    JavaClock.fixed(Instant.parse("2024-07-14T12:00:00Z"), java.time.ZoneOffset.UTC)

  private lazy val anyClaimsExtractor: ClaimsExtractorService = new ClaimsExtractorService {

    override def extractRoles(json: Json): List[Role] = Nil
    override def extractAccountId(json: Json): Option[UUID] = Some {
      UUID.fromString("a8921813-2677-475a-a25a-b1e74fa43481")
    }
  }

  private lazy val anyDSAKeyPair = {
    val keyPairGenerator = KeyPairGenerator.getInstance("EdDSA")
    keyPairGenerator.initialize(255, new SecureRandom(0.toString.getBytes))
    val keyPair = keyPairGenerator.generateKeyPair()
    val publicKeyString = new String(Base64.getEncoder.encode(keyPair.getPublic.getEncoded))
    (keyPair.getPrivate, publicKeyString)
  }
}
