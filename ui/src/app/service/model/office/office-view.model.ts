import {Pagination} from "../pagination/pagination.model";

export interface AddressView {
  addressLine1: string;
  addressLine2: string | null;
  postalCode: string;
  city: string;
  country: string;
}

export interface OfficeManagerView {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
}

export interface OfficeView {
  id: string;
  name: string;
  address: AddressView;
  officeManagers: OfficeManagerView[];
  assignedAccountsCount: number;
  desksCount: number;
  parkingSpotsCount: number;
  roomsCount: number;
  activeReservationsCount: number;
}

export interface OfficeListView {
  offices: OfficeView[];
  pagination: Pagination;
}
