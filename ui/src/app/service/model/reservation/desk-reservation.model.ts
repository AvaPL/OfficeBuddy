import {ReservationState} from "../../../component/desk/view/desk-reservation-view/model/reservation-state.enum";

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
