import {Component} from '@angular/core';
import {KeycloakProfile} from "keycloak-js";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss'
})
export class AccountComponent {

  // TODO: To be removed, only to validate integration with Keycloak

  isLoggedIn = false;
  userProfile: KeycloakProfile | null = null;
  formattedRoles: string = "<empty>";
  formattedAttributes: string = "<empty>";

  constructor(private readonly keycloak: KeycloakService) {
  }

  async ngOnInit() {
    this.isLoggedIn = this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
      this.userProfile = await this.keycloak.loadUserProfile();
      this.formattedRoles = this.keycloak.getUserRoles(true).join(", ");
      this.formattedAttributes = JSON.stringify(this.userProfile.attributes, null, 1);
    }
  }
}
