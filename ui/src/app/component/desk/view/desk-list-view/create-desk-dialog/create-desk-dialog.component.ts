import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";
import {DeskService} from "../../../../../service/desk.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CreateDesk} from "../../../../../service/model/desk/create-desk.model";

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
    name: [''],
    monitorsCount: [1],
    isStanding: [false],
    hasPhone: [false],
    isAvailable: [true]
  });
  readonly deskService = inject(DeskService);
  readonly snackbar = inject(MatSnackBar);

  async onSubmit() {
    try {
      let createDesk = this.formToCreateDesk();
      const response = await this.deskService.createDesk(createDesk)
      this.snackbar.open(`${createDesk.name} in ${this.data.officeName} created`);
      this.dialogRef.close(response);
    } catch (error) {
      // TODO: Show readable error message in case of name conflict etc. (will require proper error codes from backend)
      this.snackbar.open(`Unexpected error occurred when creating desk in ${this.data.officeName}`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error creating desk in ${this.data.officeName} [id: ${this.data.officeId}]:`, error);
    }
  }

  formToCreateDesk(): CreateDesk {
    return {
      officeId: this.data.officeId,
      name: this.form.value.name!,
      isAvailable: this.form.value.isAvailable!,
      notes: [],
      isStanding: this.form.value.isStanding!,
      monitorsCount: this.form.value.monitorsCount!,
      hasPhone: this.form.value.hasPhone!,
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
