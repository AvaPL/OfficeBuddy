package io.github.avapl
package domain.model.office.view

import java.util.UUID

/**
 * @param activeReservationsCount
 *   the number of reservations in Pending or Confirmed state for this office. Intended to be used in conjunction with
 *   date that can be used to determine whether the reservation is active or from the past.
 */
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
  activeReservationsCount: Int
)
