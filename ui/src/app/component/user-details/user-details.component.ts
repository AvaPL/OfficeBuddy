import { Component } from '@angular/core';
import {KeycloakService} from "keycloak-angular";
import {KeycloakProfile} from "keycloak-js";

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrl: './user-details.component.scss'
})
// TODO: To be removed, only to validate integration with Keycloak
export class UserDetailsComponent {

  isLoggedIn = false;
  userProfile: KeycloakProfile | null = null;
  formattedRoles: string = "<empty>";
  formattedAttributes: string = "<empty>";

  constructor(private readonly keycloak: KeycloakService) {}

  async ngOnInit() {
    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
      this.userProfile = await this.keycloak.loadUserProfile();
      this.formattedRoles = this.keycloak.getUserRoles(true).join(", ");
      this.formattedAttributes = JSON.stringify(this.userProfile.attributes, null, 1);
    }
  }

  logout() {
    this.keycloak.logout();
  }
}
