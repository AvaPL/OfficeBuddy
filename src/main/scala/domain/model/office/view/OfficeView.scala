package io.github.avapl
package domain.model.office.view

import java.util.UUID

case class OfficeView(
  id: UUID,
  name: String,
  notes: List[String],
  address: AddressView,
  officeManagers: List[OfficeManagerView],
  assignedAccountsCount: Int,
  desksCount: Int,
  parkingSpotsCount: Int,
  roomsCount: Int,
  activeReservationsCount: Int,
  isArchived: Boolean
)
