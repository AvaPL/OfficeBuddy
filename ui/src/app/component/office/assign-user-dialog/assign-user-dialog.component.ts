import {Component, inject} from '@angular/core';
import {FormControl} from "@angular/forms";
import {map, startWith} from "rxjs";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

export interface DeleteOfficeDialogData {
  officeId: string;
  officeName: string;
}

@Component({
  selector: 'app-assign-user-dialog',
  templateUrl: './assign-user-dialog.component.html',
  styleUrl: './assign-user-dialog.component.scss'
})
export class AssignUserDialogComponent {

  readonly dialogRef = inject(MatDialogRef<AssignUserDialogComponent>);
  readonly data = inject<DeleteOfficeDialogData>(MAT_DIALOG_DATA);

  accountControl = new FormControl('');
  accounts: any[] = ['John Doe (john.doe@example.com)', 'Jane Doe (jane.doe@example.com)', 'Jane Smith (js@mail.com)'];
  filteredAccounts = this.accountControl.valueChanges.pipe(
    startWith(''),
    map(value => this.filterAccounts(value || '')),
  );

  private filterAccounts(value: string): string[] {
    if (value.length < 2)
      return []
    else {
      const filterValue = value.toLowerCase();
      return this.accounts.filter(option => option.toLowerCase().includes(filterValue));
    }
  }

  onAssign() {
    this.dialogRef.close(this.accountControl.value);
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
