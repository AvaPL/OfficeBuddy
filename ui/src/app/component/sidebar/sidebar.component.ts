import {Component} from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent {

  // TODO: Set icons
  menuItems = [
    {icon: 'dashboard', name: 'User Details', route: 'user-details'},
    {icon: 'video_library', name: 'Desks', route: 'desks'},
  ];
}
