package io.github.avapl
package adapters.http.account.model.view

import adapters.http.model.view.ApiPagination
import derevo.derive
import domain.model.account.view.AccountListView
import io.scalaland.chimney.dsl._
import sttp.tapir.Schema.annotations.encodedName
import util.derevo.circe.circeDecoder
import util.derevo.circe.circeEncoder
import util.derevo.tapir.tapirSchema

@derive(circeEncoder, circeDecoder, tapirSchema)
@encodedName("AccountListView")
case class ApiAccountListView(
  accounts: List[ApiAccountView],
  pagination: ApiPagination
)

object ApiAccountListView {

  def fromDomain(accountListView: AccountListView): ApiAccountListView =
    accountListView.transformInto[ApiAccountListView]
}
