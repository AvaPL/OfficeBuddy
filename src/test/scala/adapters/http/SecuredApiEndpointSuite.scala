package io.github.avapl
package adapters.http

import adapters.auth.repository.PublicKeyRepository
import adapters.auth.service.ClaimsExtractorService
import cats.effect.{Clock => CatsClock}
import cats.effect.IO
import cats.syntax.all._
import domain.model.account.Role
import domain.model.account.Role.OfficeManager
import domain.model.account.Role.SuperAdmin
import domain.model.account.Role.User
import io.circe.Json
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
import scala.concurrent.duration.DurationInt
import sttp.client3._
import sttp.client3.testing.SttpBackendStub
import sttp.model.StatusCode
import sttp.tapir.integ.cats.effect.CatsMonadError
import sttp.tapir.server.stub.TapirStubInterpreter
import weaver.SimpleIOSuite

object SecuredApiEndpointSuite extends SimpleIOSuite with MockitoSugar with MockitoCats {

  test(
    """GIVEN secured endpoint
      | WHEN the JWT token is valid and contains the required role
      | THEN 200 OK status code and body is returned
      |""".stripMargin
  ) {
    val requiredRole = User
    val extractedRoles = List(User)
    val (bearer, publicKey) = generateJwt()
    for {
      response <- sendRequest(bearer, publicKey, requiredRole, extractedRoles)
    } yield expect.all(
      response.code == StatusCode.Ok,
      response.body == "".asRight
    )
  }

  test(
    """GIVEN secured endpoint
      | WHEN the JWT token contains role with broader access than required
      | THEN 200 OK status code and body is returned
      |""".stripMargin
  ) {
    val requiredRole = User
    val extractedRoles = List(OfficeManager)
    val (bearer, publicKey) = generateJwt()
    for {
      response <- sendRequest(bearer, publicKey, requiredRole, extractedRoles)
    } yield expect.all(
      response.code == StatusCode.Ok,
      response.body == "".asRight
    )
  }

  test(
    """GIVEN secured endpoint
      | WHEN the output is a BadRequest
      | THEN 400 BadRequest status code and body with an error message is returned
      |""".stripMargin
  ) {
    val error = ApiError.BadRequest("test message")

    for {
      response <- sendAuthorizedRequest(error.asLeft.pure[IO])
    } yield expect.all(
      response.code == StatusCode.BadRequest,
      response.body == s"""{"message":"${error.message}"}""".asLeft
    )
  }

  test(
    """GIVEN secured endpoint
      | WHEN the output is an InternalServerError
      | THEN 500 InternalServerError status code and body with an error message is returned
      |""".stripMargin
  ) {
    val error = ApiError.InternalServerError("test message")

    for {
      response <- sendAuthorizedRequest(error.asLeft.pure[IO])
    } yield expect.all(
      response.code == StatusCode.InternalServerError,
      response.body == s"""{"message":"${error.message}"}""".asLeft
    )
  }

  test(
    """GIVEN secured endpoint
      | WHEN the server logic fails with an exception
      | THEN 500 InternalServerError status code and body with a generic error message is returned
      |""".stripMargin
  ) {
    val exception = new RuntimeException("test message")

    for {
      response <- sendAuthorizedRequest(exception.raiseError)
    } yield expect.all(
      response.code == StatusCode.InternalServerError,
      response.body == "Internal server error".asLeft
    )
  }

  test(
    """GIVEN secured endpoint
      | WHEN the JWT token is invalid
      | THEN 401 Unauthorized status code is returned
      |""".stripMargin
  ) {
    val bearer = "invalid.jwt.token"
    val publicKey = "publicKey"
    for {
      response <- sendRequest(bearer, publicKey)
    } yield expect(response.code == StatusCode.Unauthorized)
  }

  test(
    """GIVEN secured endpoint
      | WHEN the JWT token is expired
      | THEN 401 Unauthorized status code is returned
      |""".stripMargin
  ) {
    val expirationSeconds = 100
    val (bearer, publicKey) = generateJwt(JwtClaim(expiration = Some(expirationSeconds)))

    implicit val fixedCatsClockAfterExpiration: CatsClock[IO] =
      whenF(mock[CatsClock[IO]].realTime) thenReturn (expirationSeconds + 1).seconds

    for {
      response <- sendRequest(bearer, publicKey)
    } yield expect(response.code == StatusCode.Unauthorized)
  }

  test(
    """GIVEN secured endpoint
      | WHEN the JWT token doesn't contain role the required role nor a role with broader access
      | THEN 403 Forbidden status code is returned
      |""".stripMargin
  ) {
    val requiredRole = SuperAdmin
    val extractedRoles = List(User)
    val (bearer, publicKey) = generateJwt()
    for {
      response <- sendRequest(bearer, publicKey, requiredRole, extractedRoles)
    } yield expect(response.code == StatusCode.Forbidden)
  }

  private def sendAuthorizedRequest(
    result: IO[Either[ApiError, Unit]]
  ) = {
    val requiredRole = User
    val (bearer, publicKey) = generateJwt()
    val extractedRoles = List(User)
    sendRequest(
      bearer = bearer,
      publicKey = publicKey,
      requiredRole = requiredRole,
      extractedRoles = extractedRoles,
      result = result
    )
  }

  private def generateJwt(jwtClaim: JwtClaim = JwtClaim()) = {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
    keyPairGenerator.initialize(2048, new SecureRandom(0.toString.getBytes))
    val keyPair = keyPairGenerator.generateKeyPair()
    val fixedJavaClock = JavaClock.fixed(Instant.parse("2024-07-14T12:00:00Z"), java.time.ZoneOffset.UTC)
    val encodedToken = JwtCirce(fixedJavaClock).encode(jwtClaim, keyPair.getPrivate, JwtAlgorithm.RS256)
    val publicKeyString = new String(Base64.getEncoder.encode(keyPair.getPublic.getEncoded))
    (encodedToken, publicKeyString)
  }

  private def sendRequest(
    bearer: String,
    publicKey: String,
    requiredRole: Role = User,
    extractedRoles: List[Role] = List(User),
    extractedAccountId: UUID = UUID.fromString("11d1bc49-0a02-4bb0-aa95-8f9082e839d7"),
    result: IO[Either[ApiError, Unit]] = ().asRight.pure[IO]
  )(implicit clock: CatsClock[IO]) = {
    val name = "test"
    val endpoint = new SecuredApiEndpoint[IO] {
      val apiEndpointName: String = name
      val claimsExtractor: ClaimsExtractorService = new ClaimsExtractorService {
        override def extractRoles(json: Json): List[Role] = extractedRoles
        override def extractAccountId(json: Json): Option[UUID] = Some(extractedAccountId)
      }
      val publicKeyRepository: PublicKeyRepository[IO] = new PublicKeyRepository[IO] {
        def get: IO[String] = publicKey.pure[IO]
      }
    }.securedEndpoint(requiredRole).get.serverLogic(_ => _ => result)
    val request = basicRequest.get(uri"http://test.com/$name").auth.bearer(bearer)
    val backendStub = TapirStubInterpreter(SttpBackendStub(new CatsMonadError[IO]))
      .whenServerEndpointRunLogic(endpoint)
      .backend()
    request.send(backendStub)
  }
}
