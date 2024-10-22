export interface ParkingSpot {
  id: string;
  officeId: string;
  name: string;
  isAvailable: boolean;
  notes: string[];
  isHandicapped: boolean;
  isUnderground: boolean;
  isArchived: boolean;
}
