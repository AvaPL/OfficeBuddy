export type Account = User | OfficeManager | SuperAdmin;

export interface User {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  isArchived: boolean;
  assignedOfficeId: string;
}

export interface OfficeManager {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  isArchived: boolean;
  assignedOfficeId: string;
  managedOfficeIds: string[];
}

export interface SuperAdmin {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  isArchived: boolean;
  assignedOfficeId: string;
  managedOfficeIds: string[];
}
