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

  selectedOfficeId: string | null = this.data.selectedOfficeId

  onApply() {
    this.dialogRef.close({
      selectedOfficeId: this.selectedOfficeId
    });
  }

  onCancel() {
    this.dialogRef.close();
  }
}
