import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {AuthGuard} from "./guard/auth.guard";
import {UserDetailsComponent} from "./component/user-details/user-details.component";
import {DesksComponent} from "./component/desks/desks.component";

const routes: Routes = [
  { path: 'user-details', component: UserDetailsComponent , canActivate: [AuthGuard]},
  { path: 'desks', component: DesksComponent }, // TODO: Add AuthGuard
  { path: '**', redirectTo: 'user-details' }
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule,
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
