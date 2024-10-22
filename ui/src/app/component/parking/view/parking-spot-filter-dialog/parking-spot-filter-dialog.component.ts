import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {OfficeCompact} from "../../../../service/model/office/office-compact.model";

export interface ParkingSpotFilterDialogData {
  offices: OfficeCompact[],
  selectedOfficeId: string | null,
}

@Component({
  selector: 'app-parking-spot-reservation-filter-dialog',
  templateUrl: './parking-spot-filter-dialog.component.html',
  styleUrl: './parking-spot-filter-dialog.component.scss'
})
export class ParkingSpotFilterDialogComponent {

  readonly dialogRef = inject(MatDialogRef<ParkingSpotFilterDialogComponent>);
  readonly data = inject<ParkingSpotFilterDialogData>(MAT_DIALOG_DATA);

  selectedOffice: OfficeCompact | null =
    this.data.offices.find(office => office.id === this.data.selectedOfficeId) ?? null;

  onApply() {
    this.dialogRef.close(this.selectedOffice);
  }

  onCancel() {
    this.dialogRef.close();
  }
}
