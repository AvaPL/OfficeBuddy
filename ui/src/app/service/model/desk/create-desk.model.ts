export interface CreateDesk {
  officeId: string;
  name: string;
  isAvailable: boolean;
  notes: string[];
  isStanding: boolean;
  monitorsCount: number;
  hasPhone: boolean;
}
