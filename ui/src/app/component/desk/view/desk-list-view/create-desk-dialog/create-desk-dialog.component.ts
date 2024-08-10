import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";

export interface CreateDeskInitialValuesDialogData {
  officeId: string
  officeName: string
}

@Component({
  selector: 'app-create-desk-dialog',
  templateUrl: './create-desk-dialog.component.html',
  styleUrl: './create-desk-dialog.component.scss'
})
export class CreateDeskDialogComponent {

  readonly dialogRef = inject(MatDialogRef<CreateDeskDialogComponent>);
  readonly data = inject<CreateDeskInitialValuesDialogData>(MAT_DIALOG_DATA);
  readonly form = inject(FormBuilder).group({
    officeId: [this.data.officeId],
    name: [''],
    monitorsCount: [1],
    isStanding: [false],
    hasPhone: [false],
    isAvailable: [true]
  });

  onSubmit() {
    this.dialogRef.close(this.form.value);
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
