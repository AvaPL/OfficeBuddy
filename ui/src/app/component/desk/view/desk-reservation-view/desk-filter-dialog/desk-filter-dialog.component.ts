import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ReservationState} from "../model/reservation-state.enum";

export interface DeskFilterDialogData {
  offices: { id: string, name: string }[],
  selectedOfficeId: string,
  selectedReservationStates: ReservationState[],
  reservedByYou: boolean
}

@Component({
  selector: 'app-desk-reservation-filter-dialog',
  templateUrl: './desk-filter-dialog.component.html',
  styleUrl: './desk-filter-dialog.component.scss'
})
export class DeskFilterDialogComponent {

  readonly dialogRef = inject(MatDialogRef<DeskFilterDialogComponent>);
  readonly data = inject<DeskFilterDialogData>(MAT_DIALOG_DATA);

  selectedOfficeId: string = this.data.selectedOfficeId
  selectedReservationStates: ReservationState[] = this.data.selectedReservationStates
  reservedByYou: boolean = this.data.reservedByYou

  onApply() {
    this.dialogRef.close({
      selectedOfficeId: this.selectedOfficeId,
      selectedReservationStates: this.selectedReservationStates,
      reservedByYou: this.reservedByYou
    });
  }

  onCancel() {
    this.dialogRef.close();
  }

  protected readonly Object = Object;
  protected readonly ReservationState = ReservationState;
}
