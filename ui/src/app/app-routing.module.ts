import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {AuthGuard} from "./guard/auth.guard";
import {DeskComponent} from "./component/desk/desk.component";
import {ParkingComponent} from "./component/parking/parking.component";
import {RoomComponent} from "./component/room/room.component";
import {AccountComponent} from "./component/account/account.component";
import {OfficeComponent} from "./component/office/office.component";

const routes: Routes = [
  {path: 'desk', component: DeskComponent, canActivate: [AuthGuard]},
  {path: 'parking', component: ParkingComponent, canActivate: [AuthGuard]},
  {path: 'room', component: RoomComponent, canActivate: [AuthGuard]},
  {path: 'account', component: AccountComponent, canActivate: [AuthGuard]},
  {path: 'office', component: OfficeComponent, canActivate: [AuthGuard]},
  {path: '**', redirectTo: 'desk'}
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule,
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
