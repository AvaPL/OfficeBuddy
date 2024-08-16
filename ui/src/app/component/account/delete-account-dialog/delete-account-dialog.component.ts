import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

export interface DeleteAccountDialogData {
  userName: string;
  email: string;
}

@Component({
  selector: 'app-delete-account-dialog',
  templateUrl: './delete-account-dialog.component.html',
  styleUrl: './delete-account-dialog.component.scss'
})
export class DeleteAccountDialogComponent {

  readonly dialogRef = inject(MatDialogRef<DeleteAccountDialogComponent>);
  readonly data = inject<DeleteAccountDialogData>(MAT_DIALOG_DATA);

  onCancel() {
    this.dialogRef.close(false);
  }

  onConfirm() {
    this.dialogRef.close(true);
  }
}
