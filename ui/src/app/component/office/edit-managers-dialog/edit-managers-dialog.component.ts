import {Component, computed, ElementRef, inject, model, signal, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {COMMA, ENTER} from "@angular/cdk/keycodes";
import {MatChipInputEvent} from "@angular/material/chips";
import {MatAutocompleteSelectedEvent} from "@angular/material/autocomplete";
import {FormControl} from "@angular/forms";
import {map, startWith} from "rxjs";

export interface EditManagersDialogData {
  officeName: string,
  currentManagers: string[];
}

@Component({
  selector: 'app-edit-managers-dialog',
  templateUrl: './edit-managers-dialog.component.html',
  styleUrl: './edit-managers-dialog.component.scss'
})
export class EditManagersDialogComponent {

  @ViewChild('managerInput') managerInput!: ElementRef;

  readonly dialogRef = inject(MatDialogRef<EditManagersDialogComponent>);
  readonly data = inject<EditManagersDialogData>(MAT_DIALOG_DATA);

  readonly separatorKeysCodes: number[] = [ENTER, COMMA];
  readonly assignedManagers = signal(this.data.currentManagers);

  allManagers: any[] = ['John Doe (john.doe@example.com)', 'Jane Doe (jane.doe@example.com)', 'Jane Smith (js@mail.com)'];
  currentManager = new FormControl('');
  filteredManagers = this.currentManager.valueChanges.pipe(
    startWith(''),
    map(value => this.filterManagers(value || ''))
  );

  private filterManagers(value: string): string[] {
    if (value.length < 2)
      return [];
    else {
      const filterValue = value.toLowerCase();
      return this.allManagers.filter(option => option.toLowerCase().includes(filterValue));
    }
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();
    if (this.allManagers.includes(value))
      this.addUnique(value)
    this.resetManagerInput();
  }

  private addUnique(value: string) {
    this.assignedManagers.update(managers => [...new Set([...managers, value])]);
  }

  private resetManagerInput() {
    this.managerInput.nativeElement.value = '';
  }

  remove(fruit: string): void {
    this.assignedManagers.update(managers => {
      const index = managers.indexOf(fruit);
      if (index < 0)
        return managers;
      managers.splice(index, 1);
      return [...managers];
    });
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.addUnique(event.option.viewValue)
    this.resetManagerInput()
    event.option.deselect();
  }

  onAssign() {
    this.dialogRef.close(this.assignedManagers());
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
