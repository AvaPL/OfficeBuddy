export interface UpdateDesk {
  name: string | null;
  isAvailable: boolean | null;
  notes: string[] | null;
  isStanding: boolean | null;
  monitorsCount: number | null;
  hasPhone: boolean | null;
}
