import {Component, inject} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption} from "@angular/material/core";
import {MatSelect} from "@angular/material/select";
import {MatSlideToggle} from "@angular/material/slide-toggle";
import {
  DeskFilterDialogData
} from "../../desk/view/desk-reservation-view/desk-filter-dialog/desk-filter-dialog.component";
import {ReservationState} from "../../desk/view/desk-reservation-view/model/reservation-state.enum";
import {AccountRole} from "../model/account-role.enum";

export interface AccountFilterDialogData {
  offices: { id: string, name: string }[],
  selectedOfficeId: string,
  selectedRoles: AccountRole[],
}

@Component({
  selector: 'app-account-filter-dialog',
  templateUrl: './account-filter-dialog.component.html',
  styleUrl: './account-filter-dialog.component.scss'
})
export class AccountFilterDialogComponent {

  readonly dialogRef = inject(MatDialogRef<AccountFilterDialogComponent>);
  readonly data = inject<AccountFilterDialogData>(MAT_DIALOG_DATA);

  selectedOfficeId: string = this.data.selectedOfficeId
  accountRoles: AccountRole[] = this.data.selectedRoles

  onApply() {
    this.dialogRef.close({
      selectedOfficeId: this.selectedOfficeId,
      selectedReservationStates: this.accountRoles,
    });
  }

  onCancel() {
    this.dialogRef.close();
  }

  protected readonly AccountRole = AccountRole;
  protected readonly Object = Object;
}
