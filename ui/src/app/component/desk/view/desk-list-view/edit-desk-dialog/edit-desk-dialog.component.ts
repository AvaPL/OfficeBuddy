import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";
import {UpdateDesk} from "../../../../../service/model/desk/update-desk.model";
import {DeskService} from "../../../../../service/desk.service";
import {MatSnackBar} from "@angular/material/snack-bar";

export interface EditDeskInitialValuesDialogData {
  officeName: string
  deskId: string
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
    name: [this.initialValues.deskName],
    monitorsCount: [this.initialValues.monitorsCount],
    isStanding: [this.initialValues.isStanding],
    hasPhone: [this.initialValues.hasPhone],
  });
  readonly deskService = inject(DeskService);
  readonly snackbar = inject(MatSnackBar);

  async onSubmit() {
    try {
      const response = await this.deskService.updateDesk(this.initialValues.deskId, this.formToUpdateDesk());
      this.snackbar.open(`${this.form.value.name} edited`);
      this.dialogRef.close(response);
    } catch (error) {
      this.snackbar.open(`Unexpected error occurred when editing ${this.initialValues.deskName}`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error updating desk ${this.initialValues.deskName} [id: ${this.initialValues.deskId}]:`, error);
    }
  }

  formToUpdateDesk(): UpdateDesk {
    return {
      name: this.form.value.name ?? undefined,
      isStanding: this.form.value.isStanding ?? undefined,
      monitorsCount: this.form.value.monitorsCount ?? undefined,
      hasPhone: this.form.value.hasPhone ?? undefined,
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
