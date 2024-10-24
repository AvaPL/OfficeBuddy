// ui/src/app/component/parking/view/parking-spot-reservation-view/create-parking-spot-reservation-dialog/create-parking-spot-reservation-dialog.component.ts
import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MediaMatcher} from "@angular/cdk/layout";
import {FormBuilder, Validators} from "@angular/forms";
import {ParkingSpotService} from "../../../../../service/parking-spot.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {LocalDate} from "../../../../../service/model/date/local-date.model";
import {CreateParkingSpotReservation} from "../../../../../service/model/reservation/create-parking-spot-reservation.model";
import {ReservationService} from "../../../../../service/reservation.service";
import {AuthService} from "../../../../../service/auth.service";
import {ReservableParkingSpotView} from "../../../../../service/model/parking/reservable-parking-spot-view.model";

export interface CreateParkingSpotReservationDialogData {
  officeId: string;
  officeName: string;
}

@Component({
  selector: 'app-create-parking-spot-reservation-dialog',
  templateUrl: './create-parking-spot-reservation-dialog.component.html',
  styleUrls: ['./create-parking-spot-reservation-dialog.component.scss'],
})
export class CreateParkingSpotReservationDialogComponent {

  parkingSpots: ReservableParkingSpotView[] = [];

  readonly dialogRef = inject(MatDialogRef<CreateParkingSpotReservationDialogComponent>);
  readonly data = inject<CreateParkingSpotReservationDialogData>(MAT_DIALOG_DATA);
  readonly form = inject(FormBuilder).group({
    parkingSpotId: [[""], Validators.pattern(/.+/)],
    reservationFrom: [],
    reservationTo: [],
    plateNumber: ["", Validators.required],
    comment: [""]
  });
  readonly parkingSpotService = inject(ParkingSpotService);
  readonly reservationService = inject(ReservationService);
  readonly authService = inject(AuthService);
  readonly snackBar = inject(MatSnackBar);

  touchUiQuery: MediaQueryList;
  minDate: Date;

  constructor(
    media: MediaMatcher,
  ) {
    this.touchUiQuery = media.matchMedia('(max-width: 600px)');
    this.minDate = new Date();
    this.minDate.setHours(0, 0, 0, 0);
  }

  async fetchParkingSpots() {
    this.parkingSpots = [];

    const reservationFromValue = this.form.value.reservationFrom;
    const reservationToValue = this.form.value.reservationTo;

    if (reservationFromValue && reservationToValue) {
      const reservationFrom = new LocalDate(reservationFromValue);
      const reservationTo = new LocalDate(reservationToValue);
      this.parkingSpots = await this.parkingSpotService.getReservableParkingSpotViewList(this.data.officeId, reservationFrom, reservationTo);
    }
  }

  async onSubmit() {
    try {
      let createParkingSpotReservation = await this.formToCreateParkingSpotReservation();
      const response = await this.reservationService.createParkingSpotReservation(createParkingSpotReservation);
      this.snackBar.open(`Parking spot reservation created and waiting for approval`);
      this.dialogRef.close(response);
    } catch (error) {
      this.snackBar.open(`Unexpected error occurred when creating parking spot reservation`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error creating parking spot reservation in ${this.data.officeName} [id: ${this.data.officeId}] for parking spot [id: ${this.form.value.parkingSpotId}]:`, error);
    }
  }

  async formToCreateParkingSpotReservation(): Promise<CreateParkingSpotReservation> {
    const userId = await this.authService.getAccountId();
    return {
      userId: userId,
      reservedFrom: new LocalDate(this.form.value.reservationFrom!).toString(),
      reservedTo: new LocalDate(this.form.value.reservationTo!).toString(),
      plateNumber: this.form.value.plateNumber!,
      notes: this.form.value.comment!,
      parkingSpotId: this.form.value.parkingSpotId![0],
    };
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
