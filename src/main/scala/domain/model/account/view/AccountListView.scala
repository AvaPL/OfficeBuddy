package io.github.avapl
package domain.model.account.view

import domain.model.view.Pagination

case class AccountListView(
  accounts: List[AccountView],
  pagination: Pagination
)
