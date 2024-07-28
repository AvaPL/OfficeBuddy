import {ChangeDetectorRef, Component, OnDestroy} from '@angular/core';
import {MediaMatcher} from "@angular/cdk/layout";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnDestroy {
  mobileQuery: MediaQueryList;

  private _mobileQueryListener: () => void;

  constructor(
    changeDetectorRef: ChangeDetectorRef,
    media: MediaMatcher,
    private readonly keycloak: KeycloakService
  ) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  menuItems = [
    {icon: 'table_restaurant', name: 'Desks', route: 'desk'},
    {icon: 'local_parking', name: 'Parking', route: 'parking'},
    {icon: 'meeting_room', name: 'Rooms', route: 'room'},
    {icon: 'supervisor_account', name: 'Accounts', route: 'account'},
    {icon: 'business', name: 'Offices', route: 'office'}
  ];

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }

  logout() {
    this.keycloak.logout();
  }
}
