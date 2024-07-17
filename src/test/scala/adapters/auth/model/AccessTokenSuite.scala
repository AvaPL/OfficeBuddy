package io.github.avapl
package adapters.auth.model

import adapters.auth.service.RolesExtractorService
import cats.effect.{Clock => CatsClock}
import cats.effect.IO
import domain.model.account.Role
import io.circe.ParsingFailure
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.time.{Clock => JavaClock}
import java.time.Instant
import java.util.Base64
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
    val rolesExtractor: RolesExtractorService = _ => roles

    for {
      accessToken <- AccessToken.decode(encodedToken, rolesExtractor, publicKey)
    } yield {
      expect(accessToken.reservedClaims == jwtClaim)
      expect(accessToken.roles == roles)
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
      result <- AccessToken.decode(invalidToken, anyRolesExtractor, publicKey).attempt
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
      result <- AccessToken.decode(encodedToken, anyRolesExtractor, anotherPublicKey).attempt
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
      result <- AccessToken.decode(encodedToken, anyRolesExtractor, publicKey).attempt
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
        .decode(encodedToken, anyRolesExtractor, publicKey)
        .attempt
    } yield matches(result) {
      case Left(_: JwtExpirationException) => success
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

  private lazy val anyRolesExtractor: RolesExtractorService = _ => Nil

  private lazy val anyDSAKeyPair = {
    val keyPairGenerator = KeyPairGenerator.getInstance("EdDSA")
    keyPairGenerator.initialize(255, new SecureRandom(0.toString.getBytes))
    val keyPair = keyPairGenerator.generateKeyPair()
    val publicKeyString = new String(Base64.getEncoder.encode(keyPair.getPublic.getEncoded))
    (keyPair.getPrivate, publicKeyString)
  }
}
