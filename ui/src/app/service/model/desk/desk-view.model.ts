import {Pagination} from "../pagination/pagination.model";

export interface DeskView {
  id: string;
  name: string;
  isAvailable: boolean;
  isStanding: boolean;
  monitorsCount: number;
  hasPhone: boolean;
}

export interface DeskListView {
  desks: DeskView[];
  pagination: Pagination;
}
