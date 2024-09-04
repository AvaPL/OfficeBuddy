export interface Pagination {
  limit: number;
  offset: number;
  hasMoreResults: boolean;
}

export function pageIndex(pagination: Pagination): number {
  return Math.floor(pagination.offset / pagination.limit);
}
