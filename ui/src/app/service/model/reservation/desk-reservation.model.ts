import {ReservationState} from "./reservation-state.enum";

export interface DeskReservation {
  id: string;
  userId: string;
  createdAt: string;
  reservedFromDate: string;
  reservedToDate: string;
  state: ReservationState;
  notes: string;
  deskId: string;
}
