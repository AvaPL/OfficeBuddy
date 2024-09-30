import {Injectable} from "@angular/core";
import {KeycloakService} from "keycloak-angular";

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
}
