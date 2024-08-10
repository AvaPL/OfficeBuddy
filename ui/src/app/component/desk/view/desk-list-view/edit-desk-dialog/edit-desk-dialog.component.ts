import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";

export interface EditDeskInitialValuesDialogData {
  officeId: string
  officeName: string
  deskName: string
  monitorsCount: number
  isStanding: boolean
  hasPhone: boolean
}

@Component({
  selector: 'app-edit-desk-dialog',
  templateUrl: './edit-desk-dialog.component.html',
  styleUrl: './edit-desk-dialog.component.scss'
})
export class EditDeskDialogComponent {

  readonly dialogRef = inject(MatDialogRef<EditDeskDialogComponent>);
  readonly initialValues = inject<EditDeskInitialValuesDialogData>(MAT_DIALOG_DATA);
  readonly form = inject(FormBuilder).group({
    officeId: [this.initialValues.officeId],
    name: [this.initialValues.deskName],
    monitorsCount: [this.initialValues.monitorsCount],
    isStanding: [this.initialValues.isStanding],
    hasPhone: [this.initialValues.hasPhone],
  });

  onSubmit() {
    this.dialogRef.close(this.form.value);
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
