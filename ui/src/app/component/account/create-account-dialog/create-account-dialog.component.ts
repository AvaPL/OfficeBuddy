import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";
import {AccountRoleCompanion} from "../model/account-role.enum";
import {AccountFilterDialogData} from "../account-filter-dialog/account-filter-dialog.component";
import {AccountService} from "../../../service/account.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CreateAccount} from "../../../service/model/account/create-account.model";
import {AccountRole} from '../../../service/model/account/account-role.enum';

export interface CreateAccountDialogData {
  offices: { id: string, name: string }[]
}

@Component({
  selector: 'app-create-account-dialog',
  templateUrl: './create-account-dialog.component.html',
  styleUrl: './create-account-dialog.component.scss'
})
export class CreateAccountDialogComponent {

  readonly dialogRef = inject(MatDialogRef<CreateAccountDialogComponent>);
  readonly data = inject<AccountFilterDialogData>(MAT_DIALOG_DATA);
  readonly form = inject(FormBuilder).group({
    firstName: [''],
    lastName: [''],
    email: [''],
    role: [AccountRole.USER],
    assignedOfficeId: [null],
    managedOfficesIds: [[]]
  });
  readonly accountService = inject(AccountService);
  readonly snackbar = inject(MatSnackBar);

  async onSubmit() {
    try {
      let createAccount = this.formToCreateAccount();
      const response = await this.accountService.createAccount(createAccount);
      this.snackbar.open(`${createAccount.firstName} ${createAccount.lastName} account created`);
      this.dialogRef.close(response);
    } catch (error) {
      this.snackbar.open(`Unexpected error occurred when creating ${this.form.value.firstName} ${this.form.value.lastName} account`, undefined, {panelClass: ['mat-warn']});
      console.error(`Error creating ${this.form.value.firstName} ${this.form.value.lastName} account:`, error);
    }
  }

  formToCreateAccount(): CreateAccount {
    return {
      firstName: this.form.value.firstName!,
      lastName: this.form.value.lastName!,
      email: this.form.value.email!,
      role: this.form.value.role!,
      assignedOfficeId: this.form.value.assignedOfficeId || undefined,
      managedOfficeIds: this.form.value.managedOfficesIds || undefined
    };
  }

  onCancel() {
    this.dialogRef.close(false);
  }

  protected readonly AccountRole = AccountRole;
  protected readonly AccountRoleCompanion = AccountRoleCompanion;
  protected readonly Object = Object;
}
