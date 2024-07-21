import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {AuthGuard} from "./guard/auth.guard";
import {UserDetailsComponent} from "./component/user-details/user-details.component";

const routes: Routes = [
  { path: '', component: UserDetailsComponent , canActivate: [AuthGuard]},
  { path: '**', redirectTo: '' }
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
