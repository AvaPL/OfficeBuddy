import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MediaMatcher} from "@angular/cdk/layout";
import {ReservationState} from "../../../../../service/model/reservation/reservation-state.enum";
import {OfficeCompact} from "../../../../../service/model/office/office-compact.model";

export interface DeskFilterDialogData {
  offices: { id: string, name: string }[],
  selectedOfficeId: string,
  selectedReservationStates: ReservationState[],
  reservationStartDate: Date,
  reservedByYou: boolean
}

@Component({
  selector: 'app-desk-reservation-filter-dialog',
  templateUrl: './desk-reservation-filter-dialog.component.html',
  styleUrl: './desk-reservation-filter-dialog.component.scss'
})
export class DeskReservationFilterDialogComponent {

  readonly dialogRef = inject(MatDialogRef<DeskReservationFilterDialogComponent>);
  readonly data = inject<DeskFilterDialogData>(MAT_DIALOG_DATA);

  touchUiQuery: MediaQueryList;

  selectedOffice: OfficeCompact | null =
    this.data.offices.find(office => office.id === this.data.selectedOfficeId) ?? null;
  selectedReservationStates: ReservationState[] = this.data.selectedReservationStates
  reservationStartDate: Date = this.data.reservationStartDate
  reservedByYou: boolean = this.data.reservedByYou

  constructor(media: MediaMatcher) {
    this.touchUiQuery = media.matchMedia('(max-width: 600px)');
  }

  onApply() {
    this.dialogRef.close({
      selectedOffice: this.selectedOffice,
      selectedReservationStates: this.selectedReservationStates,
      reservationStartDate: this.reservationStartDate,
      reservedByYou: this.reservedByYou
    });
  }

  onCancel() {
    this.dialogRef.close(false);
  }

  protected readonly Object = Object;
  protected readonly ReservationState = ReservationState;
}
