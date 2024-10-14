import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {displayName} from "../util/account-display-name.util";
import {AccountService} from "../../../service/account.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AccountRole} from "../../../service/model/account/account-role.enum";

export interface ChangeAccountRoleDialogData {
  accountId: string
  firstName: string
  lastName: string
  currentRole: AccountRole
}

@Component({
  selector: 'app-change-account-role-dialog',
  templateUrl: './change-account-role-dialog.component.html',
  styleUrl: './change-account-role-dialog.component.scss'
})
export class ChangeAccountRoleDialogComponent {

  readonly dialogRef = inject(MatDialogRef<ChangeAccountRoleDialogComponent>);
  readonly data = inject<ChangeAccountRoleDialogData>(MAT_DIALOG_DATA);
  readonly accountService = inject(AccountService);
  readonly snackbar = inject(MatSnackBar);

  selectedRole = this.data.currentRole

  async onConfirm() {
    try {
      await this.accountService.updateRole(this.data.accountId, this.selectedRole);
      const displayRole = displayName(this.selectedRole);
      this.snackbar.open(`${this.data.firstName} ${this.data.lastName}'s role updated to ${displayRole}`);
      this.dialogRef.close({selectedRole: this.selectedRole});
    } catch (error) {
      this.snackbar.open(`Unexpected error occurred when updating ${this.data.firstName} ${this.data.lastName} role`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error updating ${this.data.firstName} ${this.data.lastName} account role [id: ${this.data.accountId}]:`, error);
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }

  protected readonly Object = Object;
  protected readonly AccountRole = AccountRole;
  protected readonly displayName = displayName;
}
