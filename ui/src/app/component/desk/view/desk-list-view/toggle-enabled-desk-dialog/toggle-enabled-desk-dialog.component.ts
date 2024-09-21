import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DeskService} from "../../../../../service/desk.service";
import {MatSnackBar} from "@angular/material/snack-bar";

export interface ToggleEnabledDeskDialogData {
  officeName: string
  deskId: string
  deskName: string
  enabled: boolean
}

@Component({
  selector: 'app-toggle-enabled-desk-dialog',
  templateUrl: './toggle-enabled-desk-dialog.component.html',
  styleUrl: './toggle-enabled-desk-dialog.component.scss'
})
export class ToggleEnabledDeskDialogComponent {

  readonly dialogRef = inject(MatDialogRef<ToggleEnabledDeskDialogComponent>);
  readonly data = inject<ToggleEnabledDeskDialogData>(MAT_DIALOG_DATA);
  readonly deskService = inject(DeskService);
  readonly snackbar = inject(MatSnackBar);

  async onConfirm() {
    try {
      const response = await this.deskService.updateAvailability(this.data.deskId, this.data.enabled);
      this.snackbar.open(`${this.data.deskName} in ${this.data.officeName} ${this.data.enabled ? 'enabled' : 'disabled'}`);
      this.dialogRef.close(response);
    } catch (error) {
      this.snackbar.open(`Unexpected error occurred when ${this.data.enabled ? 'enabling' : 'disabling'} ${this.data.deskName}`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error ${this.data.enabled ? 'enabling' : 'disabling'} desk ${this.data.deskName} [id: ${this.data.deskId}]:`, error);
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
