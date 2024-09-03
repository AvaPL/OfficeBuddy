import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {OfficeService} from "../../../service/office.service";
import {MatSnackBar} from "@angular/material/snack-bar";

export interface DeleteOfficeDialogData {
  officeId: string;
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
  readonly officeService = inject(OfficeService);
  readonly snackbar = inject(MatSnackBar);

  async onConfirm() {
    try {
      await this.officeService.archiveOffice(this.data.officeId);
      this.snackbar.open(`${this.data.officeName} deleted`);
      this.dialogRef.close(true);
    } catch (error) {
      this.snackbar.open(`Unexpected error occurred when deleting ${this.data.officeName}`, undefined, {panelClass: ['mat-warn']});
      console.error('Error deleting office:', error);
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
