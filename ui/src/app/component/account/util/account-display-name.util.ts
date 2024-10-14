import {AccountRole} from "../../../service/model/account/account-role.enum";

export function displayName(role: AccountRole | string): String {
  switch (role) {
    case AccountRole.SUPER_ADMIN:
      return "Super Admin";
    case AccountRole.OFFICE_MANAGER:
      return "Office Manager";
    case AccountRole.USER:
      return "User";
    default:
      return "Unknown";
  }
}
