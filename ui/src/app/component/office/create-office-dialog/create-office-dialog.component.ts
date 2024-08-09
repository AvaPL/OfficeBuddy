import {Component, inject} from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";

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
    address: this.formBuilder.group( {
      addressLine1: [''],
      addressLine2: [''],
      postalCode: [''],
      city: [''],
      country: ['']
    })
  });

  onSubmit() {
    this.dialogRef.close(this.form.value);
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
