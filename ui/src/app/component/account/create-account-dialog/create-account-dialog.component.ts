import {Component, inject} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {MatDivider} from "@angular/material/divider";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {NgIf} from "@angular/common";
import {FormBuilder, ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-create-account-dialog',
  templateUrl: './create-account-dialog.component.html',
  styleUrl: './create-account-dialog.component.scss'
})
export class CreateAccountDialogComponent {

  readonly dialogRef = inject(MatDialogRef<CreateAccountDialogComponent>);
  readonly form = inject(FormBuilder).group({
    firstName: [''],
    lastName: [''],
    email: [''],
    // TODO: Role
    // TODO: Assigned office
    // TODO: Managed offices
  });

  onSubmit() {
    this.dialogRef.close(this.form.value);
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
