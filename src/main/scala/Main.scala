package io.github.avapl

import adapters.http.office.OfficeRoutes
import cats.effect.Async
import cats.effect.IO
import cats.effect.IOApp
import cats.syntax.all._
import com.comcast.ip4s.Host
import com.comcast.ip4s.IpLiteralSyntax
import com.comcast.ip4s.Port
import domain.model.office.Address
import domain.model.office.Office
import domain.repository.office.OfficeRepository
import domain.service.office.OfficeService
import java.util.UUID
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router

object Main extends IOApp.Simple {

  // TODO: Add logging
  override def run: IO[Unit] = runF[IO]

  private def runF[F[_]: Async] =
    runHttpServer[F](
      host = ipv4"0.0.0.0", // TODO: Introduce config
      port = port"8080"
    ).useForever

  private def runHttpServer[F[_]: Async](host: Host, port: Port) = {
    val officeRepository = new OfficeRepository[F] { // TODO: Add proper adapter
      override def create(office: Office): F[Unit] = ().pure
      override def read(officeId: UUID): F[Office] = anyOffice.pure
      override def update(office: Office): F[Unit] = ().pure
      override def delete(officeId: UUID): F[Unit] = ().pure

      private lazy val anyOffice = Office(
        id = anyOfficeId,
        name = anyOfficeName,
        notes = anyOfficeNotes,
        address = anyOfficeAddress
      )

      private lazy val anyOfficeId =
        UUID.fromString("4f99984c-e371-4b77-a184-7003f6281b8d")

      private lazy val anyOfficeName =
        "Test Office"

      private lazy val anyOfficeNotes =
        List("Test", "Notes")

      private lazy val anyOfficeAddress = Address(
        addressLine1 = "Test Street",
        addressLine2 = "Building 42",
        postalCode = "12-345",
        city = "Wroclaw",
        country = "Poland"
      )
    }
    val officeService = new OfficeService[F](officeRepository)
    val officeRoutes = new OfficeRoutes[F](officeService)
    EmberServerBuilder
      .default[F]
      .withHost(host)
      .withPort(port)
      .withHttpApp(Router("/" -> officeRoutes.routes).orNotFound)
      .build
  }
}
