import {Injectable} from "@angular/core";
import {KeycloakService} from "keycloak-angular";
import {AccountRole} from "./model/account/account-role.enum";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(public keycloakService: KeycloakService) {
  }

  async getToken() {
    return this.keycloakService.getToken();
  }

  async getAccountId() {
    const userProfile = await this.keycloakService.loadUserProfile();
    const attributes = userProfile.attributes!;
    return (attributes['account_id'] as string[])[0]
  }

  hasSuperAdminRole() {
    return this.hasRequiredRole(AccountRole.SUPER_ADMIN);
  }

  hasOfficeManagerRole() {
    return this.hasRequiredRole(AccountRole.OFFICE_MANAGER) || this.hasSuperAdminRole();
  }

  hasUserRole() {
    return this.hasRequiredRole(AccountRole.USER) || this.hasOfficeManagerRole();
  }

  private hasRequiredRole(requiredRole: AccountRole) {
    const requiredRoleString = {
      [AccountRole.SUPER_ADMIN]: 'office_buddy_super_admin',
      [AccountRole.OFFICE_MANAGER]: 'office_buddy_office_manager',
      [AccountRole.USER]: 'office_buddy_user'
    }[requiredRole];
    const roleStrings = this.keycloakService.getUserRoles(true);
    return roleStrings.includes(requiredRoleString);
  }
}
