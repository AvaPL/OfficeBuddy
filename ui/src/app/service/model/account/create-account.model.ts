import {AccountRole} from "./account-role.enum";

export interface CreateAccount {
  role: AccountRole,
  firstName: string,
  lastName: string,
  email: string,
  assignedOfficeId: string | null,
  managedOfficeIds: string[] | null,
}
