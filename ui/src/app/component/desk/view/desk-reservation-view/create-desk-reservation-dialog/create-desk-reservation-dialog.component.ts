import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MediaMatcher} from "@angular/cdk/layout";
import {FormBuilder, Validators} from "@angular/forms";
import {ReservableDeskView} from "../../../../../service/model/desk/reservable-desk-view.model";
import {DeskService} from "../../../../../service/desk.service";
import {MatSnackBar} from "@angular/material/snack-bar";

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
  readonly snackBar = inject(MatSnackBar);

  touchUiQuery: MediaQueryList;

  constructor(
    media: MediaMatcher,
  ) {
    this.touchUiQuery = media.matchMedia('(max-width: 600px)');
  }

  async fetchDesks() {
    this.desks = []

    const reservationFrom = this.form.value.reservationFrom;
    const reservationTo = this.form.value.reservationTo;

    if (reservationFrom && reservationTo)
      this.desks = await this.deskService.getReservableDeskViewList(this.data.officeId, reservationFrom!, reservationTo!);
  }

  onSubmit() {

    try {
      // TODO: Implement desk reservation request
      // let createDeskReservation = this.formToCreateDeskReservation();
      // const response = await this.deskService.createDeskReservation(createDeskReservation)
      this.snackBar.open(`Desk reservation created and waiting for approval`);
      // this.dialogRef.close(response);
      this.dialogRef.close(this.form.value); // TODO: Remove
    } catch (error) {
      this.snackBar.open(`Unexpected error occurred when creating desk reservation`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error creating desk reservation in ${this.data.officeName} [id: ${this.data.officeId}] for desk [id: ${this.form.value.deskId}]:`, error);
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
