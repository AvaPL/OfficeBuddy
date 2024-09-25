export interface CreateDeskReservation {
  userId: string;
  reservedFrom: string;
  reservedTo: string;
  notes: string;
  deskId: string;
}
