package io.github.avapl
package adapters.postgres

import cats.syntax.either._
import java.util.UUID
import skunk._
import skunk.Codec
import skunk.Command
import skunk.Void
import skunk.data.Arr
import skunk.data.Type
import skunk.implicits._

package object repository {

  lazy val _uuid: Codec[List[UUID]] =
    Codec
      .array[UUID](
        u => u.toString,
        s => Either.catchOnly[IllegalArgumentException](UUID.fromString(s)).leftMap(_.getMessage),
        Type._uuid
      )
      .imap(_.flattenTo(List))(Arr(_: _*))

  implicit class SessionOps[F[_]](session: Session[F]) {

    /**
     * Prepare a command if a condition is met, otherwise prepare a no-op command.
     */
    def prepareIf[A](condition: Boolean)(command: Command[A]): F[PreparedCommand[F, A]] =
      if (condition) session.prepare(command)
      else session.prepare(noOpCommand)

    private def noOpCommand[A]: Command[A] =
      sql"DO $$$$ BEGIN END $$$$".command.contramap(_ => Void)
  }
}
