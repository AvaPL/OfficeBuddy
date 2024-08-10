import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

export interface DeskFilterDialogData {
  offices: { id: string, name: string }[],
  selectedOfficeId: string,
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

  onApply() {
    this.dialogRef.close({
      selectedOfficeId: this.selectedOfficeId
    });
  }

  onCancel() {
    this.dialogRef.close();
  }
}
