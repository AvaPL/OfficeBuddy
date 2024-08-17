import {Component, inject} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption} from "@angular/material/core";
import {MatSelect} from "@angular/material/select";
import {
  DeskFilterDialogData
} from "../../desk/view/desk-reservation-view/desk-filter-dialog/desk-filter-dialog.component";
import {AccountRole} from "../model/account-role.enum";

export interface ChangeAccountRoleDialogData {
  accountId: string
  userName: string
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

  selectedRole = this.data.currentRole

  onConfirm() {
    this.dialogRef.close({
      selectedRole: this.selectedRole
    });
  }

  onCancel() {
    this.dialogRef.close();
  }

  protected readonly Object = Object;
  protected readonly AccountRole = AccountRole;
}
