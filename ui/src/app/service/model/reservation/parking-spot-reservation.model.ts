import {ReservationState} from "./reservation-state.enum";

export interface ParkingSpotReservation {
  id: string;
  userId: string;
  createdAt: string;
  reservedFromDate: string;
  reservedToDate: string;
  state: ReservationState;
  notes: string;
  parkingSpotId: string;
  plateNumber: string;
}
