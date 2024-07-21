import {KeycloakService} from "keycloak-angular";

export function initializeKeycloak(
  keycloak: KeycloakService
) {
  return () =>
    keycloak.init({
      config: {
        // TODO: Inject these instead of hardcoding
        url: 'http://localhost:8888',
        realm: 'office-buddy',
        clientId: 'office-buddy-ui',
      },
      initOptions: {
        checkLoginIframe: false
      }
    });
}
