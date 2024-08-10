import {Component, inject} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {DeskFilterDialogComponent} from "./desks-filter-dialog/desk-filter-dialog.component";

@Component({
  selector: 'app-desk-list-view',
  templateUrl: './desk-list-view.component.html',
  styleUrl: './desk-list-view.component.scss'
})
export class DeskListViewComponent {

  readonly deskFilterDialog = inject(MatDialog);

  offices = [
    {
      id: "1",
      name: "Wroclaw Office",
    },
    {
      id: "2",
      name: "Krakow Office",
    },
    {
      id: "3",
      name: "Warsaw Office",
    }
  ]

  selectedOffice = this.offices[0]
  pageSize = 10
  pageIndex = 0
  paginatorLength = this.totalDesks()

  handleFilter(selectedOfficeId: string) {
    // TODO: Replace with more robust code, the below is temporary only
    console.log(`Filtering by officeId: ${selectedOfficeId}`)
    this.selectedOffice = this.offices.find(office => office.id === selectedOfficeId) || this.offices[0]
    this.pageIndex = 0
    this.paginatorLength = this.totalDesks()
  }

  totalDesks() {
    return 100;
  }

  openFilterDialog() {
    const dialogRef = this.deskFilterDialog.open(DeskFilterDialogComponent, {
      data: {
        offices: this.offices,
        selectedOfficeId: this.selectedOffice.id,
      }
    });

    dialogRef.afterClosed().subscribe(selectedOfficeId => {
      this.handleFilter(selectedOfficeId)
    });
  }

  openReservationView() {
    console.log('Opening reservation view');
  }
}
