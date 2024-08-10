import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

export interface ToggleEnabledDeskDialogData {
  officeName: string
  deskName: string
  enabled: boolean
}

@Component({
  selector: 'app-toggle-enabled-desk-dialog',
  templateUrl: './toggle-enabled-desk-dialog.component.html',
  styleUrl: './toggle-enabled-desk-dialog.component.scss'
})
export class ToggleEnabledDeskDialogComponent {

  readonly dialogRef = inject(MatDialogRef<ToggleEnabledDeskDialogComponent>);
  readonly data = inject<ToggleEnabledDeskDialogData>(MAT_DIALOG_DATA);

  onCancel() {
    this.dialogRef.close(false);
  }

  onConfirm() {
    this.dialogRef.close(true);
  }
}
