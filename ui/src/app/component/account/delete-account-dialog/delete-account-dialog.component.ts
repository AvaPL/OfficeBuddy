import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AccountService} from "../../../service/account.service";

export interface DeleteAccountDialogData {
  accountId: string;
  firstName: string;
  lastName: string;
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
  readonly accountService = inject(AccountService);
  readonly snackbar = inject(MatSnackBar);

  async onConfirm() {
    try {
      await this.accountService.archiveAccount(this.data.accountId)
      this.snackbar.open(`${this.data.firstName} ${this.data.lastName} deleted`);
      this.dialogRef.close(true);
    } catch (error) {
      this.snackbar.open(`Unexpected error occurred when deleting ${this.data.firstName} ${this.data.lastName}`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error deleting ${this.data.firstName} ${this.data.lastName} (${this.data.email}) account [id: ${this.data.accountId}]:`, error);
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
