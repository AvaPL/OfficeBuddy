import {ChangeDetectorRef, Component, OnDestroy} from '@angular/core';
import {MediaMatcher} from "@angular/cdk/layout";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnDestroy {
  mobileQuery: MediaQueryList;

  private _mobileQueryListener: () => void;

  constructor(changeDetectorRef: ChangeDetectorRef, media: MediaMatcher) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  // TODO: Set icons
  menuItems = [
    {icon: 'person', name: 'User Details', route: 'user-details'},
    {icon: 'table_restaurant', name: 'Desks', route: 'desks'},
    {icon: 'local_parking', name: 'Parking', route: 'parking'},
    {icon: 'meeting_room', name: 'Meeting rooms', route: 'meeting-rooms'},
    {icon: 'supervisor_account', name: 'Accounts', route: 'accounts'}
  ];

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }
}
