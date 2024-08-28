import {Component, inject} from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";
import {OfficeService} from "../../../service/office.service";

@Component({
  selector: 'app-create-office-dialog',
  templateUrl: './create-office-dialog.component.html',
  styleUrl: './create-office-dialog.component.scss'
})
export class CreateOfficeDialogComponent {

  readonly dialogRef = inject(MatDialogRef<CreateOfficeDialogComponent>);
  readonly formBuilder = inject(FormBuilder);
  readonly form = this.formBuilder.group({
    name: [''],
    address: this.formBuilder.group({
      addressLine1: [''],
      addressLine2: [''],
      postalCode: [''],
      city: [''],
      country: ['']
    })
  });
  readonly officeService = inject(OfficeService);

  async onSubmit() {
    try {
      const response = await this.officeService.createOffice(this.form.value);
      this.dialogRef.close(response);
    } catch (error) {
      // TODO: Add toast notification
      console.error('Error creating office:', error);
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
