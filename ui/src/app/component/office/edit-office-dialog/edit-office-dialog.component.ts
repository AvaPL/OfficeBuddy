import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";
import {OfficeService} from "../../../service/office.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {UpdateOffice} from "../../../service/model/office/update-office.model";

export interface EditOfficeInitialValuesDialogData {
  id: string;
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
  readonly officeService = inject(OfficeService);
  readonly snackbar = inject(MatSnackBar);

  async onSubmit() {
    try {
      const response = await this.officeService.updateOffice(this.initialValues.id, this.formToUpdateOffice());
      this.snackbar.open(`${this.form.value.name} edited`);
      this.dialogRef.close(response);
    } catch (error) {
      this.snackbar.open(`Unexpected error occurred when editing ${this.initialValues.name}`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error updating office ${this.initialValues.name}:`, error);
    }
  }

  formToUpdateOffice(): UpdateOffice {
    return {
      name: this.form.value.name ?? undefined,
      address: {
        addressLine1: this.form.value.address!.addressLine1 ?? undefined,
        addressLine2: this.form.value.address!.addressLine2 ?? undefined,
        postalCode: this.form.value.address!.postalCode ?? undefined,
        city: this.form.value.address!.city ?? undefined,
        country: this.form.value.address!.country ?? undefined
      }
    };
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
