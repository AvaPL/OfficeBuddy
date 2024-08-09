import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";

export interface EditOfficeInitialValuesDialogData {
  name: string;
  address: {
    addressLine1: string;
    addressLine2: string | null;
    postalCode: string;
    city: string;
    country: string;
  }
}

@Component({
  selector: 'app-edit-office-dialog',
  templateUrl: './edit-office-dialog.component.html',
  styleUrl: './edit-office-dialog.component.scss'
})
export class EditOfficeDialogComponent {

  readonly dialogRef = inject(MatDialogRef<EditOfficeDialogComponent>);
  readonly initialValues = inject<EditOfficeInitialValuesDialogData>(MAT_DIALOG_DATA);
  readonly formBuilder = inject(FormBuilder);
  readonly form = this.formBuilder.group({
    name: [this.initialValues.name],
    address: this.formBuilder.group({
      addressLine1: [this.initialValues.address.addressLine1],
      addressLine2: [this.initialValues.address.addressLine2],
      postalCode: [this.initialValues.address.postalCode],
      city: [this.initialValues.address.city],
      country: [this.initialValues.address.country]
    })
  });

  onSubmit() {
    this.dialogRef.close(this.form.value);
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
