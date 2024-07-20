package io.github.avapl
package adapters.auth

import scala.util.control.NoStackTrace

package object model {

  type PublicKey = String

  case object MissingAccountId extends NoStackTrace
}
