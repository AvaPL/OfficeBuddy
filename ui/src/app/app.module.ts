import {APP_INITIALIZER, NgModule} from "@angular/core";
import {KeycloakAngularModule, KeycloakService} from "keycloak-angular";
import {initializeKeycloak} from "./init/keycloak-init.factory";
import {AppComponent} from "./app.component";
import {AppRoutingModule} from "./app-routing.module";
import {BrowserModule} from "@angular/platform-browser";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {NavbarComponent} from "./component/sidebar/navbar.component";
import {DeskComponent} from "./component/desk/desk.component";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatListModule} from "@angular/material/list";
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {ParkingComponent} from "./component/parking/parking.component";
import {RoomComponent} from "./component/room/room.component";
import {AccountComponent} from "./component/account/account.component";
import {OfficeComponent} from "./component/office/office.component";
import {MatCardModule} from "@angular/material/card";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatMenuModule} from "@angular/material/menu";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatDialogModule} from "@angular/material/dialog";
import {DeleteOfficeDialogComponent} from "./component/office/delete-office-dialog/delete-office-dialog.component";
import {CommonModule} from "@angular/common";
import {CreateOfficeDialogComponent} from "./component/office/create-office-dialog/create-office-dialog.component";
import {MAT_FORM_FIELD_DEFAULT_OPTIONS, MatFormFieldModule} from "@angular/material/form-field";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {EditOfficeDialogComponent} from "./component/office/edit-office-dialog/edit-office-dialog.component";
import {MatChipsModule} from "@angular/material/chips";
import {
  DeskReservationConfirmDialogComponent
} from "./component/desk/view/desk-reservation-view/desk-reservation-confirm-dialog/desk-reservation-confirm-dialog.component";
import {
  DeskReservationFilterDialogComponent as DeskReservationFilterDialogComponent
} from "./component/desk/view/desk-reservation-view/desk-reservation-filter-dialog/desk-reservation-filter-dialog.component";
import {MatSelectModule} from "@angular/material/select";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {
  CreateDeskReservationDialogComponent
} from "./component/desk/view/desk-reservation-view/create-desk-reservation-dialog/create-desk-reservation-dialog.component";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {DateAdapter, MatNativeDateModule} from "@angular/material/core";
import {AppDateAdapter} from "./date/app-date-adapter.service";
import {DeskListViewComponent} from "./component/desk/view/desk-list-view/desk-list-view.component";
import {
  DeskReservationViewComponent
} from "./component/desk/view/desk-reservation-view/desk-reservation-view.component";
import {
  DeskFilterDialogComponent as DeskListFilterDialogComponent
} from "./component/desk/view/desk-list-view/desks-filter-dialog/desk-filter-dialog.component";
import {
  DeleteDeskDialogComponent
} from "./component/desk/view/desk-list-view/delete-desk-dialog/delete-desk-dialog.component";
import {
  ToggleEnabledDeskDialogComponent
} from "./component/desk/view/desk-list-view/toggle-enabled-desk-dialog/toggle-enabled-desk-dialog.component";
import {
  CreateDeskDialogComponent
} from "./component/desk/view/desk-list-view/create-desk-dialog/create-desk-dialog.component";
import {
  EditDeskDialogComponent
} from "./component/desk/view/desk-list-view/edit-desk-dialog/edit-desk-dialog.component";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {DeleteAccountDialogComponent} from "./component/account/delete-account-dialog/delete-account-dialog.component";
import {AccountFilterDialogComponent} from "./component/account/account-filter-dialog/account-filter-dialog.component";
import {
  ChangeAccountRoleDialogComponent
} from "./component/account/change-account-role-dialog/change-account-role-dialog.component";
import {CreateAccountDialogComponent} from "./component/account/create-account-dialog/create-account-dialog.component";
import {provideHttpClient, withInterceptorsFromDi} from "@angular/common/http";
import {MAT_SNACK_BAR_DEFAULT_OPTIONS, MatSnackBarModule} from "@angular/material/snack-bar";
import {AssignUserDialogComponent} from "./component/office/assign-user-dialog/assign-user-dialog.component";
import {EditManagersDialogComponent} from "./component/office/edit-managers-dialog/edit-managers-dialog.component";
import {
  ParkingSpotListViewComponent
} from "./component/parking/view/parking-spot-list-view/parking-spot-list-view.component";
import {
  ParkingSpotFilterDialogComponent
} from "./component/parking/view/parking-spot-list-view/parking-spot-filter-dialog/parking-spot-filter-dialog.component";
import {
  DeleteParkingSpotDialogComponent
} from "./component/parking/view/parking-spot-list-view/delete-parking-spot-dialog/delete-parking-spot-dialog.component";
import {
  ToggleEnabledParkingSpotDialogComponent
} from "./component/parking/view/parking-spot-list-view/toggle-enabled-parking-spot-dialog/toggle-enabled-parking-spot-dialog.component";
import {
  CreateParkingSpotDialogComponent
} from "./component/parking/view/parking-spot-list-view/create-parking-spot-dialog/create-parking-spot-dialog.component";
import {
  EditParkingSpotDialogComponent
} from "./component/parking/view/parking-spot-list-view/edit-parking-spot-dialog/edit-parking-spot-dialog.component";
import {
  ParkingSpotReservationViewComponent
} from "./component/parking/view/parking-spot-reservation-view/parking-spot-reservation-view.component";
import {
  ParkingSpotReservationFilterDialogComponent
} from "./component/parking/view/parking-spot-reservation-view/parking-spot-reservation-filter-dialog/parking-spot-reservation-filter-dialog.component";
import {
  ParkingSpotReservationConfirmDialogComponent
} from "./component/parking/view/parking-spot-reservation-view/parking-spot-reservation-confirm-dialog/parking-spot-reservation-confirm-dialog.component";
import {
  CreateParkingSpotReservationDialogComponent
} from "./component/parking/view/parking-spot-reservation-view/create-parking-spot-reservation-dialog/create-parking-spot-reservation-dialog.component";

@NgModule({
  declarations: [
    AccountComponent,
    AccountFilterDialogComponent,
    AppComponent,
    AssignUserDialogComponent,
    ChangeAccountRoleDialogComponent,
    CreateAccountDialogComponent,
    CreateDeskDialogComponent,
    CreateDeskReservationDialogComponent,
    CreateOfficeDialogComponent,
    CreateParkingSpotDialogComponent,
    CreateParkingSpotReservationDialogComponent,
    DeleteAccountDialogComponent,
    DeleteDeskDialogComponent,
    DeleteOfficeDialogComponent,
    DeleteParkingSpotDialogComponent,
    DeskComponent,
    DeskListFilterDialogComponent,
    DeskListViewComponent,
    DeskReservationFilterDialogComponent,
    DeskReservationViewComponent,
    DeskReservationConfirmDialogComponent,
    EditDeskDialogComponent,
    EditManagersDialogComponent,
    EditOfficeDialogComponent,
    EditParkingSpotDialogComponent,
    NavbarComponent,
    OfficeComponent,
    ParkingComponent,
    ParkingSpotFilterDialogComponent,
    ParkingSpotListViewComponent,
    ParkingSpotReservationConfirmDialogComponent,
    ParkingSpotReservationFilterDialogComponent,
    ParkingSpotReservationViewComponent,
    RoomComponent,
    ToggleEnabledDeskDialogComponent,
    ToggleEnabledParkingSpotDialogComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    CommonModule,
    FormsModule,
    KeycloakAngularModule,
    MatAutocompleteModule,
    MatButtonModule,
    MatCardModule,
    MatChipsModule,
    MatDatepickerModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatSelectModule,
    MatSidenavModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatTooltipModule,
    MatToolbarModule,
    ReactiveFormsModule
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService],
    },
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: {
        appearance: 'outline'
      }
    },
    {
      provide: DateAdapter,
      useClass: AppDateAdapter
    },
    {
      provide: MAT_SNACK_BAR_DEFAULT_OPTIONS,
      useValue: {
        duration: 3000,
        horizontalPosition: 'right',
        verticalPosition: 'top'
      }
    },
    provideAnimationsAsync(),
    provideHttpClient(withInterceptorsFromDi())
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
