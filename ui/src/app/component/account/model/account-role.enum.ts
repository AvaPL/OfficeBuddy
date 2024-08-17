export enum AccountRole {
  SUPER_ADMIN = "SuperAdmin",
  OFFICE_MANAGER = "OfficeManager",
  USER = "User"
}

export namespace AccountRoleCompanion {
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
}
