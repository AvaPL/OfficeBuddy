import {Pagination} from "../pagination/pagination.model";
import {ReservationState} from "./reservation-state.enum";

export interface UserView {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
}

export interface ParkingSpotView {
  id: string;
  name: string;
}

export interface ParkingSpotReservationView {
  id: string;
  reservedFromDate: string;
  reservedToDate: string;
  state: ReservationState;
  notes: string;
  user: UserView;
  parkingSpot: ParkingSpotView;
  plateNumber: string;
}

export interface ParkingSpotReservationListView {
  reservations: ParkingSpotReservationView[];
  pagination: Pagination;
}
