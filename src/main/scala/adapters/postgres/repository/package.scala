package io.github.avapl
package adapters.postgres

import cats.syntax.either._
import java.util.UUID
import skunk.Codec
import skunk.data.Arr
import skunk.data.Type

package object repository {

  lazy val _uuid: Codec[List[UUID]] =
    Codec
      .array[UUID](
        u => u.toString,
        s => Either.catchOnly[IllegalArgumentException](UUID.fromString(s)).leftMap(_.getMessage),
        Type._uuid
      )
      .imap(_.flattenTo(List))(Arr(_: _*))
}
