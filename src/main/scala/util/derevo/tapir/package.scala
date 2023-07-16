package io.github.avapl
package util.derevo

import derevo.Derevo
import derevo.Derivation
import derevo.delegating
import scala.language.experimental.macros
import sttp.tapir.Schema

package object tapir {

  @delegating("sttp.tapir.Schema.derived")
  object tapirSchema extends Derivation[Schema] {
    def instance[T]: Schema[T] = macro Derevo.delegate[Schema, T]
  }
}
