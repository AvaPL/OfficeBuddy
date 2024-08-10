import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

export interface DeleteDeskDialogData {
  officeName: string;
  deskName: string
}

@Component({
  selector: 'app-delete-desk-dialog',
  templateUrl: './delete-desk-dialog.component.html',
  styleUrl: './delete-desk-dialog.component.scss'
})
export class DeleteDeskDialogComponent {

  readonly dialogRef = inject(MatDialogRef<DeleteDeskDialogComponent>);
  readonly data = inject<DeleteDeskDialogData>(MAT_DIALOG_DATA);

  onCancel() {
    this.dialogRef.close(false);
  }

  onConfirm() {
    this.dialogRef.close(true);
  }
}
