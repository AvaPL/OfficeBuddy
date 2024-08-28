import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {OfficeService} from "../../../service/office.service";

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

  async onConfirm() {
    try {
      const response = await this.officeService.archiveOffice(this.data.officeId);
      this.dialogRef.close(response);
    } catch (error) {
      // TODO: Add toast notification
      console.error('Error deleting office:', error);
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
