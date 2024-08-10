import {Component, EventEmitter, inject, Output} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {DeskFilterDialogComponent} from "./desks-filter-dialog/desk-filter-dialog.component";
import {PageEvent} from "@angular/material/paginator";
import {DeleteDeskDialogComponent} from "./delete-desk-dialog/delete-desk-dialog.component";
import {ToggleEnabledDeskDialogComponent} from "./toggle-enabled-desk-dialog/toggle-enabled-desk-dialog.component";
import {CreateDeskDialogComponent} from "./create-desk-dialog/create-desk-dialog.component";

@Component({
  selector: 'app-desk-list-view',
  templateUrl: './desk-list-view.component.html',
  styleUrl: './desk-list-view.component.scss'
})
export class DeskListViewComponent {

  @Output() changeToReservationsView = new EventEmitter();

  readonly deskFilterDialog = inject(MatDialog);
  readonly createDeskDialog = inject(MatDialog);
  readonly toggleEnabledDeskDialog = inject(MatDialog);
  readonly deleteDeskDialog = inject(MatDialog);

  offices = [
    {
      id: "1",
      name: "Wroclaw Office",
      desks: [
        {
          id: "1",
          name: "107/1",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: true,
        },
        {
          id: "2",
          name: "107/2",
          isAvailable: false,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: false,
        },
        {
          id: "3",
          name: "107/3",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: true,
        },
        {
          id: "4",
          name: "107/4",
          isAvailable: false,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: false
        },
        {
          id: "5",
          name: "108/1",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: true,
        },
        {
          id: "6",
          name: "108/2",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: false,
        },
        {
          id: "7",
          name: "108/3",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: true,
        },
        {
          id: "8",
          name: "108/4",
          isAvailable: false,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: false
        },
        {
          id: "9",
          name: "109/1",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: true,
        },
        {
          id: "10",
          name: "109/2",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: false,
        },
        {
          id: "11",
          name: "109/3",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: true,
        },
        {
          id: "12",
          name: "109/4",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: false
        },
        {
          id: "13",
          name: "110/1",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: true,
        },
        {
          id: "14",
          name: "110/2",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: false,
        },
        {
          id: "15",
          name: "110/3",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: true,
        },
        {
          id: "16",
          name: "110/4",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: false
        },
        {
          id: "17",
          name: "111/1",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: true,
        },
        {
          id: "18",
          name: "111/2",
          isAvailable: false,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: false,
        },
        {
          id: "19",
          name: "111/3",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: true,
        },
        {
          id: "20",
          name: "111/4",
          isAvailable: false,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: false
        },
        {
          id: "21",
          name: "112/1",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: true,
        },
        {
          id: "22",
          name: "112/2",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: false,
        },
        {
          id: "23",
          name: "112/3",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: true,
        },
        {
          id: "24",
          name: "112/4",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: false
        },
        {
          id: "25",
          name: "113/1",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: true,
        },
        {
          id: "26",
          name: "113/2",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: false,
        },
        {
          id: "27",
          name: "113/3",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: true,
        },
        {
          id: "28",
          name: "113/4",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: false
        },
        {
          id: "29",
          name: "114/1",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: true,
        },
        {
          id: "30",
          name: "114/2",
          isAvailable: false,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: false,
        },
        {
          id: "31",
          name: "114/3",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: true,
        },
        {
          id: "32",
          name: "114/4",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: false
        },
        {
          id: "33",
          name: "115/1",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: true,
        },
        {
          id: "34",
          name: "115/2",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: false,
        },
        {
          id: "35",
          name: "115/3",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: true,
        }
      ]
    },
    {
      id: "2",
      name: "Krakow Office",
      desks: [
        {
          id: "9",
          name: "109/1",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: true,
        },
        {
          id: "10",
          name: "109/2",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: false,
        },
        {
          id: "11",
          name: "109/3",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: true,
        },
        {
          id: "12",
          name: "109/4",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: false
        },
        {
          id: "13",
          name: "110/1",
          isAvailable: true,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: true,
        },
        {
          id: "14",
          name: "110/2",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: false,
        },
        {
          id: "15",
          name: "110/3",
          isAvailable: true,
          isStanding: false,
          monitorsCount: 1,
          hasPhone: true,
        },
        {
          id: "16",
          name: "110/4",
          isAvailable: false,
          isStanding: true,
          monitorsCount: 2,
          hasPhone: false
        }
      ]
    },
    {
      id: "3",
      name: "Warsaw Office",
      desks: []
    }
  ]

  selectedOffice = this.offices[0]
  pageSize = 18
  pageIndex = 0
  desksPage = this.paginateDesks()

  handleFilter(selectedOfficeId: string) {
    console.log(`Filtering by officeId: ${selectedOfficeId}`)
    this.selectedOffice = this.offices.find(office => office.id === selectedOfficeId) || this.offices[0]
    this.pageIndex = 0
    this.desksPage = this.paginateDesks()
  }

  openFilterDialog() {
    const dialogRef = this.deskFilterDialog.open(DeskFilterDialogComponent, {
      data: {
        offices: this.offices,
        selectedOfficeId: this.selectedOffice.id,
      }
    });

    dialogRef.afterClosed().subscribe(({selectedOfficeId}) => {
      this.handleFilter(selectedOfficeId)
    });
  }

  createDesk(officeId: string, officeName: string) {
    const dialogRef = this.createDeskDialog.open(CreateDeskDialogComponent, {
      data: {officeId, officeName}
    });

    dialogRef.afterClosed().subscribe(createdDesk => {
      if (createdDesk) {
        console.log(`Created new desk in office ${createdDesk.officeId}: `, createdDesk);
      } else {
        console.log(`Cancelled creating new desk`);
      }
    });
  }

  toggleEnabledDesk(deskId: string, deskName: string, officeName: string, enabled: boolean) {
    const dialogRef = this.toggleEnabledDeskDialog.open(ToggleEnabledDeskDialogComponent, {
      data: {
        officeName,
        deskName,
        enabled
      }
    });

    dialogRef.afterClosed().subscribe(isConfirmed => {
      if (isConfirmed) {
        if (enabled) console.log(`Enabled desk ${deskId}`);
        else console.log(`Disabled desk ${deskId}`);
      } else {
        console.log(`Cancelled toggling enabled desk ${deskId}`);
      }
    })
  }

  deleteDesk(deskId: string, deskName: string, officeName: string) {
    const dialogRef = this.deleteDeskDialog.open(DeleteDeskDialogComponent, {data: {officeName, deskName}});

    dialogRef.afterClosed().subscribe(isConfirmed => {
      if (isConfirmed) {
        console.log(`Deleted desk ${deskId}`);
      } else {
        console.log(`Cancelled deleting desk ${deskId}`);
      }
    });
  }

  handlePageEvent(event: PageEvent) {
    this.pageIndex = event.pageIndex
    this.desksPage = this.paginateDesks()
  }

  paginateDesks() {
    return this.selectedOffice.desks.slice(this.pageIndex * this.pageSize, (this.pageIndex + 1) * this.pageSize)
  }
}
