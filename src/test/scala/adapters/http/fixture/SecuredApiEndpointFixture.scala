package io.github.avapl
package adapters.http.fixture

import adapters.auth.model.PublicKey
import adapters.auth.repository.PublicKeyRepository
import adapters.auth.service.ClaimsExtractorService
import cats.effect.IO
import domain.model.account.Role
import io.circe.Json
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.time.Clock
import java.time.Instant
import java.util.Base64
import java.util.UUID
import pdi.jwt.JwtAlgorithm
import pdi.jwt.JwtCirce
import pdi.jwt.JwtClaim
import sttp.client3.Request
import sttp.client3.Response
import sttp.client3.testing.SttpBackendStub
import sttp.tapir.integ.cats.effect.CatsMonadError
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.stub.TapirStubInterpreter

trait SecuredApiEndpointFixture {

  val publicKeyRepository: PublicKeyRepository[IO] = new PublicKeyRepository[IO] {
    override def get: IO[PublicKey] = IO.pure(publicKey)
  }

  def sendSecuredApiEndpointRequest(
    request: Request[Either[String, String], Any],
    role: Role,
    accountId: UUID = anyAccountId
  )(
    endpoints: ClaimsExtractorService => List[ServerEndpoint[Any, IO]]
  ): IO[Response[Either[PublicKey, PublicKey]]] = {
    val claimsExtractorService = new ClaimsExtractorService {
      override def extractRoles(json: Json): List[Role] = List(role)
      override def extractAccountId(json: Json): Option[UUID] = Some(accountId)
    }
    val backendStub = TapirStubInterpreter(SttpBackendStub(new CatsMonadError[IO]))
      .whenServerEndpointsRunLogic(endpoints(claimsExtractorService))
      .backend()
    request.auth.bearer(bearer).send(backendStub)
  }

  private lazy val anyAccountId: UUID = UUID.fromString("53eea01d-4129-4098-9fe5-f07d768c937e")

  private lazy val (bearer, publicKey) = {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
    keyPairGenerator.initialize(2048, new SecureRandom(0.toString.getBytes))
    val keyPair = keyPairGenerator.generateKeyPair()
    val fixedJavaClock = Clock.fixed(Instant.parse("2024-07-16T12:00:00Z"), java.time.ZoneOffset.UTC)
    val encodedToken = JwtCirce(fixedJavaClock).encode(JwtClaim(), keyPair.getPrivate, JwtAlgorithm.RS256)
    val publicKeyString = new String(Base64.getEncoder.encode(keyPair.getPublic.getEncoded))
    (encodedToken, publicKeyString)
  }
}
