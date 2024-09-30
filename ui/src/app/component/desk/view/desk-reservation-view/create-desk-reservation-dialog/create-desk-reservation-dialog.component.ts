import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MediaMatcher} from "@angular/cdk/layout";
import {FormBuilder, Validators} from "@angular/forms";
import {ReservableDeskView} from "../../../../../service/model/desk/reservable-desk-view.model";
import {DeskService} from "../../../../../service/desk.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {LocalDate} from "../../../../../service/model/date/local-date.model";
import {CreateDeskReservation} from "../../../../../service/model/reservation/create-desk-reservation.model";
import {ReservationService} from "../../../../../service/reservation.service";
import {AuthService} from "../../../../../service/auth.service";

export interface CreateDeskReservationDialogData {
  officeId: string
  officeName: string
}

@Component({
  selector: 'app-create-desk-reservation-dialog',
  templateUrl: './create-desk-reservation-dialog.component.html',
  styleUrl: './create-desk-reservation-dialog.component.scss',
})
export class CreateDeskReservationDialogComponent {

  desks: ReservableDeskView[] = []

  readonly dialogRef = inject(MatDialogRef<CreateDeskReservationDialogComponent>);
  readonly data = inject<CreateDeskReservationDialogData>(MAT_DIALOG_DATA);
  readonly form = inject(FormBuilder).group({
    deskId: [[""], Validators.pattern(/.+/)],
    reservationFrom: [],
    reservationTo: [],
    comment: [""]
  })
  readonly deskService = inject(DeskService);
  readonly reservationService = inject(ReservationService);
  readonly authService = inject(AuthService);
  readonly snackBar = inject(MatSnackBar);

  touchUiQuery: MediaQueryList;

  constructor(
    media: MediaMatcher,
  ) {
    this.touchUiQuery = media.matchMedia('(max-width: 600px)');
  }

  async fetchDesks() {
    this.desks = []

    const reservationFromValue = this.form.value.reservationFrom;
    const reservationToValue = this.form.value.reservationTo;

    if (reservationFromValue && reservationToValue) {
      const reservationFrom = new LocalDate(reservationFromValue);
      const reservationTo = new LocalDate(reservationToValue);
      this.desks = await this.deskService.getReservableDeskViewList(this.data.officeId, reservationFrom, reservationTo);
    }
  }

  async onSubmit() {
    try {
      let createDeskReservation = await this.formToCreateDeskReservation();
      const response = await this.reservationService.createDeskReservation(createDeskReservation)
      this.snackBar.open(`Desk reservation created and waiting for approval`);
      this.dialogRef.close(response);
    } catch (error) {
      this.snackBar.open(`Unexpected error occurred when creating desk reservation`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error creating desk reservation in ${this.data.officeName} [id: ${this.data.officeId}] for desk [id: ${this.form.value.deskId}]:`, error);
    }
  }

  async formToCreateDeskReservation(): Promise<CreateDeskReservation> {
    const userId = await this.authService.getAccountId();
    return {
      userId: userId,
      reservedFrom: new LocalDate(this.form.value.reservationFrom!).toString(),
      reservedTo: new LocalDate(this.form.value.reservationTo!).toString(),
      notes: this.form.value.comment!,
      deskId: this.form.value.deskId![0],
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
