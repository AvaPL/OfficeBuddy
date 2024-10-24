import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { MatSnackBar } from "@angular/material/snack-bar";

export interface ParkingSpotReservationConfirmDialogData {
  isWarn: boolean;
  buttonText: string;
  parkingSpotName: string;
  userFirstName: string | null;
  userLastName: string | null;
  plateNumber: string;
  reservedFromDate: string;
  reservedToDate: string;
  snackbarSuccessText: string;
  snackbarErrorText: string;
  sendRequest: () => Promise<any>;
}

@Component({
  selector: 'app-parking-spot-reservation-confirm-dialog',
  templateUrl: './parking-spot-reservation-confirm-dialog.component.html',
  styleUrls: ['./parking-spot-reservation-confirm-dialog.component.scss']
})
export class ParkingSpotReservationConfirmDialogComponent {

  readonly dialogRef = inject(MatDialogRef<ParkingSpotReservationConfirmDialogComponent>);
  readonly data = inject<ParkingSpotReservationConfirmDialogData>(MAT_DIALOG_DATA);
  readonly snackBar = inject(MatSnackBar);

  async onConfirm() {
    try {
      await this.data.sendRequest();
      this.snackBar.open(this.data.snackbarSuccessText);
      this.dialogRef.close(true);
    } catch (error) {
      this.snackBar.open(this.data.snackbarErrorText);
      console.error(`Error confirming reservation operation`, error);
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
