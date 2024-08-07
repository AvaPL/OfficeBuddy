import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

export interface ReservationConfirmDialogData {
  isWarn: boolean;
  buttonText: string;
  deskName: string;
  userName: string | null;
  startDate: string;
  endDate: string;
}

@Component({
  selector: 'app-desk-reservation-confirm-dialog',
  templateUrl: './desk-reservation-confirm-dialog.component.html',
  styleUrl: './desk-reservation-confirm-dialog.component.scss'
})
export class DeskReservationConfirmDialogComponent {

  readonly dialogRef = inject(MatDialogRef<DeskReservationConfirmDialogComponent>);
  readonly data = inject<ReservationConfirmDialogData>(MAT_DIALOG_DATA);

  onCancel() {
    this.dialogRef.close(false);
  }

  onConfirm() {
    this.dialogRef.close(true);
  }
}
