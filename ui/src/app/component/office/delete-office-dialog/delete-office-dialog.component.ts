import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

export interface DeleteOfficeDialogData {
  officeName: string;
}

@Component({
  selector: 'app-delete-office-dialog',
  templateUrl: './delete-office-dialog.component.html',
  styleUrl: './delete-office-dialog.component.scss',
})
export class DeleteOfficeDialogComponent {

  readonly dialogRef = inject(MatDialogRef<DeleteOfficeDialogComponent>);
  readonly data = inject<DeleteOfficeDialogData>(MAT_DIALOG_DATA);

  onCancel() {
    this.dialogRef.close(false);
  }

  onConfirm() {
    this.dialogRef.close(true);
  }
}
