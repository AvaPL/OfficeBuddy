import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { MediaMatcher } from "@angular/cdk/layout";
import { ReservationState } from "../../../../../service/model/reservation/reservation-state.enum";
import { OfficeCompact } from "../../../../../service/model/office/office-compact.model";

export interface ParkingSpotFilterDialogData {
  offices: { id: string, name: string }[],
  selectedOfficeId: string,
  selectedReservationStates: ReservationState[],
  reservationStartDate: Date,
  reservedByYou: boolean,
  plateNumber: string | null
}

@Component({
  selector: 'app-parking-spot-reservation-filter-dialog',
  templateUrl: './parking-spot-reservation-filter-dialog.component.html',
  styleUrls: ['./parking-spot-reservation-filter-dialog.component.scss']
})
export class ParkingSpotReservationFilterDialogComponent {

  readonly dialogRef = inject(MatDialogRef<ParkingSpotReservationFilterDialogComponent>);
  readonly data = inject<ParkingSpotFilterDialogData>(MAT_DIALOG_DATA);

  touchUiQuery: MediaQueryList;

  selectedOffice: OfficeCompact | null =
    this.data.offices.find(office => office.id === this.data.selectedOfficeId) ?? null;
  selectedReservationStates: ReservationState[] = this.data.selectedReservationStates;
  reservationStartDate: Date = this.data.reservationStartDate;
  reservedByYou: boolean = this.data.reservedByYou;
  plateNumber: string | null = this.data.plateNumber;

  constructor(media: MediaMatcher) {
    this.touchUiQuery = media.matchMedia('(max-width: 600px)');
  }

  onApply() {
    this.dialogRef.close({
      selectedOffice: this.selectedOffice,
      selectedReservationStates: this.selectedReservationStates,
      reservationStartDate: this.reservationStartDate,
      reservedByYou: this.reservedByYou,
      plateNumber: this.plateNumber
    });
  }

  onCancel() {
    this.dialogRef.close(false);
  }

  protected readonly Object = Object;
  protected readonly ReservationState = ReservationState;
}
