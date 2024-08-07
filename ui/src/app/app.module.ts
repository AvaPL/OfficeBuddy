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
} from "./component/desk/desk-reservation-confirm-dialog/desk-reservation-confirm-dialog.component";
import {DeskFilterDialogComponent} from "./component/desk/desks-filter-dialog/desk-filter-dialog.component";
import {MatSelectModule} from "@angular/material/select";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {
  CreateDeskReservationDialogComponent
} from "./component/desk/create-desk-reservation-dialog/create-desk-reservation-dialog.component";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MAT_DATE_LOCALE, MatNativeDateModule} from "@angular/material/core";

@NgModule({
  declarations: [
    AccountComponent,
    AppComponent,
    CreateOfficeDialogComponent,
    DeleteOfficeDialogComponent,
    DeskComponent,
    DeskFilterDialogComponent,
    CreateDeskReservationDialogComponent,
    DeskReservationConfirmDialogComponent,
    EditOfficeDialogComponent,
    NavbarComponent,
    OfficeComponent,
    ParkingComponent,
    RoomComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    CommonModule,
    FormsModule,
    KeycloakAngularModule,
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
      provide: MAT_DATE_LOCALE,
      useValue: 'en-GB'
    },
    provideAnimationsAsync()
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
