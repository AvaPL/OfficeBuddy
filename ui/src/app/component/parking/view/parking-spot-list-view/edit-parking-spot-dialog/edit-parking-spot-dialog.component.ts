import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";
import {ParkingSpotService} from "../../../../../service/parking-spot.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {UpdateParkingSpot} from "../../../../../service/model/parking/update-parking-spot.model";

export interface EditParkingSpotInitialValuesDialogData {
  officeName: string;
  parkingSpotId: string;
  parkingSpotName: string;
  isHandicapped: boolean;
  isUnderground: boolean;
}

@Component({
  selector: 'app-edit-parking-spot-dialog',
  templateUrl: './edit-parking-spot-dialog.component.html',
  styleUrl: './edit-parking-spot-dialog.component.scss'
})
export class EditParkingSpotDialogComponent {

  readonly dialogRef = inject(MatDialogRef<EditParkingSpotDialogComponent>);
  readonly initialValues = inject<EditParkingSpotInitialValuesDialogData>(MAT_DIALOG_DATA);
  readonly form = inject(FormBuilder).group({
    name: [this.initialValues.parkingSpotName],
    isHandicapped: [this.initialValues.isHandicapped],
    isUnderground: [this.initialValues.isUnderground],
  });
  readonly parkingSpotService = inject(ParkingSpotService);
  readonly snackbar = inject(MatSnackBar);

  async onSubmit() {
    try {
      const response = await this.parkingSpotService.updateParkingSpot(this.initialValues.parkingSpotId, this.formToUpdateParkingSpot());
      this.snackbar.open(`${this.form.value.name} edited`);
      this.dialogRef.close(response);
    } catch (error) {
      this.snackbar.open(`Unexpected error occurred when editing ${this.initialValues.parkingSpotName}`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error updating parking spot ${this.initialValues.parkingSpotName} [id: ${this.initialValues.parkingSpotId}]:`, error);
    }
  }

  formToUpdateParkingSpot(): UpdateParkingSpot {
    return {
      name: this.form.value.name ?? undefined,
      isHandicapped: this.form.value.isHandicapped ?? undefined,
      isUnderground: this.form.value.isUnderground ?? undefined,
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
