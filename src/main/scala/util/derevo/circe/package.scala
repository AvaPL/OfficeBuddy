package io.github.avapl
package util.derevo

import derevo.Derevo
import derevo.Derivation
import derevo.delegating
import io.circe.Decoder
import io.circe.Encoder
import scala.language.experimental.macros

package object circe {

  @delegating("io.circe.generic.semiauto.deriveEncoder")
  object circeEncoder extends Derivation[Encoder] {
    def instance[T]: Encoder[T] = macro Derevo.delegate[Encoder, T]
  }

  @delegating("io.circe.generic.semiauto.deriveDecoder")
  object circeDecoder extends Derivation[Decoder] {
    def instance[T]: Decoder[T] = macro Derevo.delegate[Decoder, T]
  }
}
