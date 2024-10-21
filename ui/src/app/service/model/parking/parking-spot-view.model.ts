import {Pagination} from "../pagination/pagination.model";

export interface ParkingSpotView {
  id: string;
  name: string;
  isAvailable: boolean;
  isHandicapped: boolean;
  isUnderground: boolean;
}

export interface ParkingSpotListView {
  parkingSpots: ParkingSpotView[];
  pagination: Pagination;
}
