import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AccountRole, AccountRoleCompanion} from "../model/account-role.enum";

export interface AccountFilterDialogData {
  offices: { id: string, name: string }[],
  selectedOfficeId: string,
  selectedRoles: AccountRole[],
}

@Component({
  selector: 'app-account-filter-dialog',
  templateUrl: './account-filter-dialog.component.html',
  styleUrl: './account-filter-dialog.component.scss'
})
export class AccountFilterDialogComponent {

  readonly dialogRef = inject(MatDialogRef<AccountFilterDialogComponent>);
  readonly data = inject<AccountFilterDialogData>(MAT_DIALOG_DATA);

  selectedOfficeId: string = this.data.selectedOfficeId
  accountRoles: AccountRole[] = this.data.selectedRoles

  onApply() {
    this.dialogRef.close({
      selectedOfficeId: this.selectedOfficeId,
      selectedReservationStates: this.accountRoles,
    });
  }

  onCancel() {
    this.dialogRef.close();
  }

  protected readonly AccountRole = AccountRole;
  protected readonly Object = Object;
  protected readonly AccountRoleCompanion = AccountRoleCompanion;
}
