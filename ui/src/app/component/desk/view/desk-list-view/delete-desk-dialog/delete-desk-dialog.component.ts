import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
import {DeskService} from "../../../../../service/desk.service";

export interface DeleteDeskDialogData {
  officeName: string;
  deskId: string;
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
  readonly deskService = inject(DeskService);
  readonly snackbar = inject(MatSnackBar);

  async onConfirm() {
    try {
      await this.deskService.archiveDesk(this.data.deskId);
      this.snackbar.open(`${this.data.deskName} in ${this.data.officeName} deleted`);
      this.dialogRef.close(true);
    } catch (error) {
      this.snackbar.open(`Unexpected error occurred when deleting ${this.data.deskName} in ${this.data.officeName}`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error deleting desk ${this.data.deskName} [id: ${this.data.deskId}]:`, error);
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
