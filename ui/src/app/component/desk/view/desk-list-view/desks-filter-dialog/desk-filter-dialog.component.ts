import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {OfficeCompact} from "../../../../../service/model/office/office-compact.model";

export interface DeskFilterDialogData {
  offices: OfficeCompact[],
  selectedOfficeId: string | null,
}

@Component({
  selector: 'app-desk-reservation-filter-dialog',
  templateUrl: './desk-filter-dialog.component.html',
  styleUrl: './desk-filter-dialog.component.scss'
})
export class DeskFilterDialogComponent {

  readonly dialogRef = inject(MatDialogRef<DeskFilterDialogComponent>);
  readonly data = inject<DeskFilterDialogData>(MAT_DIALOG_DATA);

  selectedOffice: OfficeCompact | null =
    this.data.offices.find(office => office.id === this.data.selectedOfficeId) ?? null;

  onApply() {
    this.dialogRef.close(this.selectedOffice);
  }

  onCancel() {
    this.dialogRef.close();
  }
}
