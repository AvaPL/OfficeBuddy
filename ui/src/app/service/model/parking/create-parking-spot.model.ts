export interface CreateParkingSpot {
  officeId: string;
  name: string;
  isAvailable: boolean;
  notes: string[];
  isHandicapped: boolean;
  isUnderground: boolean;
}
