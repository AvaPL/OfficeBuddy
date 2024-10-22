import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ParkingSpotService} from "../../../../../service/parking-spot.service";

export interface ToggleEnabledParkingSpotDialogData {
  officeName: string
  parkingSpotId: string
  parkingSpotName: string
  enabled: boolean
}

@Component({
  selector: 'app-toggle-enabled-parking-spot-dialog',
  templateUrl: './toggle-enabled-parking-spot-dialog.component.html',
  styleUrl: './toggle-enabled-parking-spot-dialog.component.scss'
})
export class ToggleEnabledParkingSpotDialogComponent {

  readonly dialogRef = inject(MatDialogRef<ToggleEnabledParkingSpotDialogComponent>);
  readonly data = inject<ToggleEnabledParkingSpotDialogData>(MAT_DIALOG_DATA);
  readonly parkingSpotService = inject(ParkingSpotService);
  readonly snackbar = inject(MatSnackBar);

  async onConfirm() {
    try {
      const response = await this.parkingSpotService.updateAvailability(this.data.parkingSpotId, this.data.enabled);
      this.snackbar.open(`${this.data.parkingSpotName} in ${this.data.officeName} ${this.data.enabled ? 'enabled' : 'disabled'}`);
      this.dialogRef.close(response);
    } catch (error) {
      this.snackbar.open(`Unexpected error occurred when ${this.data.enabled ? 'enabling' : 'disabling'} ${this.data.parkingSpotName}`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error ${this.data.enabled ? 'enabling' : 'disabling'} parking spot ${this.data.parkingSpotName} [id: ${this.data.parkingSpotId}]:`, error);
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
