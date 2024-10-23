import {Pagination} from "../pagination/pagination.model";
import {ReservationState} from "./reservation-state.enum";

export interface UserView {
  id: string,
  firstName: string,
  lastName: string,
  email: string
}

export interface DeskView {
  id: string,
  name: string,
}

export interface DeskReservationView {
  id: string,
  reservedFromDate: string,
  reservedToDate: string,
  state: ReservationState
  notes: string,
  user: UserView,
  desk: DeskView
}

export interface DeskReservationListView {
  reservations: DeskReservationView[];
  pagination: Pagination;
}
