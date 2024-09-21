export interface Desk {
  id: string;
  officeId: string;
  name: string;
  isAvailable: boolean;
  notes: string[];
  isStanding: boolean;
  monitorsCount: number;
  hasPhone: boolean;
  isArchived: boolean;
}
