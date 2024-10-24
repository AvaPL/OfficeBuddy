export interface CreateParkingSpotReservation {
  userId: string;
  reservedFrom: string;
  reservedTo: string;
  notes: string;
  parkingSpotId: string;
  plateNumber: string;
}
