import {Component, inject} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatDivider} from "@angular/material/divider";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {NgIf} from "@angular/common";
import {FormBuilder, ReactiveFormsModule} from "@angular/forms";
import {AccountRole} from "../model/account-role.enum";
import {AccountFilterDialogData} from "../account-filter-dialog/account-filter-dialog.component";

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

  onSubmit() {
    this.dialogRef.close(this.form.value);
  }

  onCancel() {
    this.dialogRef.close(false);
  }

  protected readonly AccountRole = AccountRole;
  protected readonly Object = Object;
}
