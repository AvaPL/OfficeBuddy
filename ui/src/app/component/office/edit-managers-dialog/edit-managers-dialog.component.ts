import {Component, ElementRef, inject, OnInit, signal, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {COMMA, ENTER} from "@angular/cdk/keycodes";
import {MatChipInputEvent} from "@angular/material/chips";
import {MatAutocompleteSelectedEvent} from "@angular/material/autocomplete";
import {FormControl} from "@angular/forms";
import {AccountCompact} from "../../../service/model/account/account-compact.model";
import {AccountService} from "../../../service/account.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {OfficeService} from "../../../service/office.service";

export interface EditManagersDialogData {
  officeId: string,
  officeName: string,
  currentManagers: AccountCompact[];
}

@Component({
  selector: 'app-edit-managers-dialog',
  templateUrl: './edit-managers-dialog.component.html',
  styleUrl: './edit-managers-dialog.component.scss'
})
export class EditManagersDialogComponent implements OnInit {

  @ViewChild('managerInput') managerInput!: ElementRef;

  readonly dialogRef = inject(MatDialogRef<EditManagersDialogComponent>);
  readonly data = inject<EditManagersDialogData>(MAT_DIALOG_DATA);
  readonly accountService = inject(AccountService);
  readonly officeService = inject(OfficeService);
  readonly snackbar = inject(MatSnackBar);

  readonly separatorKeysCodes: number[] = [ENTER, COMMA];
  readonly assignedManagers = signal(this.data.currentManagers);

  managerControl = new FormControl('');
  managers: AccountCompact[] = [];

  ngOnInit() {
    this.managerControl.valueChanges.subscribe(value => {
      if (typeof value === 'string' || value === null) {
        this.filterManagers(value)
      }
    })
  }

  async filterManagers(value: string | null) {
    if (value && value.length >= 2) {
      const filterValue = value.toLowerCase();
      this.managers = await this.accountService.getCompactAccounts(filterValue, null);
    } else {
      this.managers = [];
    }
  }

  displayManager(account: AccountCompact | undefined): string {
    return account ? `${account.firstName} ${account.lastName} (${account.email})` : '';
  }

  add(event: MatChipInputEvent): void {
    const accountId = (event.value || '').trim();
    this.addUnique(accountId)
    this.resetManagerInput();
  }

  private addUnique(accountId: string) {
    const managerToAdd = this.managers.find(manager => manager.id === accountId)
    if (managerToAdd)
      this.assignedManagers.update(managers => [...new Set([...managers, managerToAdd])]);
  }

  private resetManagerInput() {
    this.managerInput.nativeElement.value = '';
  }

  remove(manager: AccountCompact): void {
    this.assignedManagers.update(managers => {
      const index = managers.indexOf(manager);
      if (index < 0)
        return managers;
      managers.splice(index, 1);
      return [...managers];
    });
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.addUnique(event.option.value)
    this.resetManagerInput()
    event.option.deselect();
  }

  async onAssign() {
    try {
      // TODO: Implement managers assignment on the backend
      // await this.officeService.assignManagers(this.data.officeId, this.assignedManagers());
      this.snackbar.open(`Managers assigned to ${this.data.officeName}`);
      this.dialogRef.close(true);
    } catch (error) {
      this.snackbar.open(`Unexpected error occurred when assigning managers to ${this.data.officeName} [id: ${this.data.officeId}]`, undefined, {panelClass: ['error-snackbar']});
      console.error(`Error assigning managers to ${this.data.officeName} office [id: ${this.data.officeId}]:`, error);
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
