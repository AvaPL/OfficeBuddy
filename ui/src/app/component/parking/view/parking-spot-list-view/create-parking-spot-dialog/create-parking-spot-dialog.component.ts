import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder} from "@angular/forms";
import {ParkingSpotService} from "../../../../../service/parking-spot.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CreateParkingSpot} from "../../../../../service/model/parking/create-parking-spot.model";

export interface CreateParkingSpotInitialValuesDialogData {
  officeId: string
  officeName: string
}

@Component({
  selector: 'app-create-parking-spot-dialog',
  templateUrl: './create-parking-spot-dialog.component.html',
  styleUrl: './create-parking-spot-dialog.component.scss'
})
export class CreateParkingSpotDialogComponent {

  readonly dialogRef = inject(MatDialogRef<CreateParkingSpotDialogComponent>);
  readonly data = inject<CreateParkingSpotInitialValuesDialogData>(MAT_DIALOG_DATA);
  readonly form = inject(FormBuilder).group({
    name: [''],
    isHandicapped: [false],
    isUnderground: [false],
    isAvailable: [true]
  });
  readonly parkingSpotService = inject(ParkingSpotService);
  readonly snackbar = inject(MatSnackBar);

  async onSubmit() {
    try {
      let createParkingSpot = this.formToCreateParkingSpot();
      const response = await this.parkingSpotService.createParkingSpot(createParkingSpot)
      this.snackbar.open(`${createParkingSpot.name} in ${this.data.officeName} created`);
      this.dialogRef.close(response);
    } catch (error) {
      this.snackbar.open(`Unexpected error occurred when creating parking spot in ${this.data.officeName}`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error creating parking spot in ${this.data.officeName} [id: ${this.data.officeId}]:`, error);
    }
  }

  formToCreateParkingSpot(): CreateParkingSpot {
    return {
      officeId: this.data.officeId,
      name: this.form.value.name!,
      isAvailable: this.form.value.isAvailable!,
      notes: [],
      isHandicapped: this.form.value.isHandicapped!,
      isUnderground: this.form.value.isUnderground!,
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
