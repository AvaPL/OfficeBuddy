import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";

export interface ReservationConfirmDialogData {
  isWarn: boolean;
  buttonText: string;
  deskName: string;
  userFirstName: string | null;
  userLastName: string | null;
  reservedFromDate: string;
  reservedToDate: string;
  snackbarSuccessText: string;
  snackbarErrorText: string;
  sendRequest: () => Promise<any>;
}

@Component({
  selector: 'app-desk-reservation-confirm-dialog',
  templateUrl: './desk-reservation-confirm-dialog.component.html',
  styleUrl: './desk-reservation-confirm-dialog.component.scss'
})
export class DeskReservationConfirmDialogComponent {

  readonly dialogRef = inject(MatDialogRef<DeskReservationConfirmDialogComponent>);
  readonly data = inject<ReservationConfirmDialogData>(MAT_DIALOG_DATA);
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
