import {Pagination} from "../pagination/pagination.model";
import {AccountRole} from "./account-role.enum";

export interface OfficeView {
  id: string;
  name: string;
}

export interface AccountView {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  role: AccountRole;
  assignedOffice: OfficeView | null;
  managedOffices: OfficeView[] | null;
}

export interface AccountListView {
  accounts: AccountView[];
  pagination: Pagination;
}
