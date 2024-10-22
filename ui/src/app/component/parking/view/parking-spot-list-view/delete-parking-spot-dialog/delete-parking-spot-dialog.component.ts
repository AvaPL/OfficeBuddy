import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ParkingSpotService} from "../../../../../service/parking-spot.service";

export interface DeleteParkingSpotDialogData {
  officeName: string;
  parkingSpotId: string;
  parkingSpotName: string
}

@Component({
  selector: 'app-delete-parking-spot-dialog',
  templateUrl: './delete-parking-spot-dialog.component.html',
  styleUrl: './delete-parking-spot-dialog.component.scss'
})
export class DeleteParkingSpotDialogComponent {

  readonly dialogRef = inject(MatDialogRef<DeleteParkingSpotDialogComponent>);
  readonly data = inject<DeleteParkingSpotDialogData>(MAT_DIALOG_DATA);
  readonly parkingSpotService = inject(ParkingSpotService);
  readonly snackbar = inject(MatSnackBar);

  async onConfirm() {
    try {
      await this.parkingSpotService.archiveParkingSpot(this.data.parkingSpotId);
      this.snackbar.open(`${this.data.parkingSpotName} in ${this.data.officeName} deleted`);
      this.dialogRef.close(true);
    } catch (error) {
      this.snackbar.open(`Unexpected error occurred when deleting ${this.data.parkingSpotName} in ${this.data.officeName}`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error deleting parking spot ${this.data.parkingSpotName} [id: ${this.data.parkingSpotId}]:`, error);
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
