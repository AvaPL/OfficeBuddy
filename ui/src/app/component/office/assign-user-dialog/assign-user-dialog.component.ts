import {Component, inject, OnInit} from '@angular/core';
import {FormControl} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AccountCompact} from "../../../service/model/account/account-compact.model";
import {AccountService} from "../../../service/account.service";
import {MatSnackBar} from "@angular/material/snack-bar";

export interface AssignUserDialogData {
  officeId: string;
  officeName: string;
}

@Component({
  selector: 'app-assign-user-dialog',
  templateUrl: './assign-user-dialog.component.html',
  styleUrl: './assign-user-dialog.component.scss'
})
export class AssignUserDialogComponent implements OnInit {

  readonly dialogRef = inject(MatDialogRef<AssignUserDialogComponent>);
  readonly data = inject<AssignUserDialogData>(MAT_DIALOG_DATA);
  readonly accountService = inject(AccountService);
  readonly snackbar = inject(MatSnackBar);

  accountControl: FormControl<string | AccountCompact | null> = new FormControl('');
  accounts: AccountCompact[] = [];

  ngOnInit() {
    this.accountControl.valueChanges.subscribe(value => {
      if (typeof value === 'string' || value === null) {
        this.filterAccounts(value);
      }
    });
  }

  async filterAccounts(value: string | null) {
    if (value && value.length >= 2) {
      const filterValue = value.toLowerCase();
      this.accounts = await this.accountService.getCompactAccounts(filterValue, null);
    } else {
      this.accounts = [];
    }
  }

  displayAccount(account: AccountCompact | undefined): string {
    return account ? `${account.firstName} ${account.lastName} (${account.email})` : '';
  }

  async onAssign() {
    const account = this.accountControl.value as AccountCompact;
    try {
      await this.accountService.assignOffice(account.id, this.data.officeId);
      this.snackbar.open(`${account.firstName} ${account.lastName} assigned to ${this.data.officeName}`);
      this.dialogRef.close(true);
    } catch (error) {
      this.snackbar.open(`Unexpected error occurred when assigning ${account.firstName} ${account.lastName} [id: ${account.id}] to ${this.data.officeName} [id: ${this.data.officeId}]`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error assigning ${account.firstName} ${account.lastName} [id: ${account.id}] to ${this.data.officeName} office [id: ${this.data.officeId}]:`, error);
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
