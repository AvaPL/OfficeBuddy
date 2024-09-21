import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {DeskFilterDialogComponent} from "./desks-filter-dialog/desk-filter-dialog.component";
import {PageEvent} from "@angular/material/paginator";
import {DeleteDeskDialogComponent} from "./delete-desk-dialog/delete-desk-dialog.component";
import {ToggleEnabledDeskDialogComponent} from "./toggle-enabled-desk-dialog/toggle-enabled-desk-dialog.component";
import {CreateDeskDialogComponent} from "./create-desk-dialog/create-desk-dialog.component";
import {EditDeskDialogComponent, EditDeskInitialValuesDialogData} from "./edit-desk-dialog/edit-desk-dialog.component";
import {OfficeCompact} from "../../../../service/model/office/office-compact.model";
import {OfficeService} from "../../../../service/office.service";
import {DeskService} from "../../../../service/desk.service";
import {Desk} from "../../../../service/model/desk/desk.model";

@Component({
  selector: 'app-desk-list-view',
  templateUrl: './desk-list-view.component.html',
  styleUrl: './desk-list-view.component.scss'
})
export class DeskListViewComponent implements OnInit {

  @Input() selectedOfficeId!: string | null
  @Output() selectedOfficeIdChange = new EventEmitter<string>();
  @Output() changeToReservationsView = new EventEmitter();

  readonly deskFilterDialog = inject(MatDialog);
  readonly createDeskDialog = inject(MatDialog);
  readonly editDeskDialog = inject(MatDialog);
  readonly toggleEnabledDeskDialog = inject(MatDialog);
  readonly deleteDeskDialog = inject(MatDialog);

  offices: OfficeCompact[] = []

  selectedOffice: OfficeCompact | null = null

  pageSize = 18
  pageIndex = 0
  desksPage: Desk[] = [] // TODO: Remove this and commented out code

  constructor(private officeService: OfficeService, private deskService: DeskService) {
  }

  ngOnInit() {
    this.fetchOffices()
    // this.handleFilter(this.selectedOfficeId)
  }

  async fetchOffices() {
    this.offices = await this.officeService.getCompactOffices();
    this.selectedOffice = this.offices[0] ?? null // TODO: Prefer user's assigned office instead
  }

  handleFilter(selectedOfficeId: string | null) {
    console.log(`Filtering by officeId: ${selectedOfficeId}`)
    // this.selectedOffice = this.selectOffice(this.selectedOfficeId)
    // this.pageIndex = 0
    // this.desksPage = this.paginateDesks()
  }

  selectOffice(selectedOfficeId: string | null) {
    const selectedOffice = this.offices.find(office => office.id === selectedOfficeId) || this.offices[0]
    if (selectedOfficeId)
      this.selectedOfficeIdChange.emit(selectedOffice.id)
    return selectedOffice
  }

  openFilterDialog() {
    const dialogRef = this.deskFilterDialog.open(DeskFilterDialogComponent, {
      data: {
        offices: this.offices,
        selectedOfficeId: this.selectedOffice?.id,
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const {selectedOfficeId} = result
        this.handleFilter(selectedOfficeId)
      }
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

  editDesk(deskId: string, editDeskInitialValues: EditDeskInitialValuesDialogData) {
    const dialogRef = this.editDeskDialog.open(EditDeskDialogComponent, {
      data: editDeskInitialValues
    })

    dialogRef.afterClosed().subscribe(editedDesk => {
      if (editedDesk) {
        console.log(`Edited desk [${deskId}] in ${editedDesk.officeId}: `, editedDesk);
      } else {
        console.log(`Cancelled editing desk ${deskId}`);
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
    // this.desksPage = this.paginateDesks()
  }

  // paginateDesks() {
  //   return this.selectedOffice.desks.slice(this.pageIndex * this.pageSize, (this.pageIndex + 1) * this.pageSize)
  // }
}
