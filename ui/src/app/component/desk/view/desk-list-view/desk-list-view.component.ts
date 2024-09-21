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
import {DeskView} from "../../../../service/model/desk/desk-view.model";
import {Pagination} from "../../../../service/model/pagination/pagination.model";

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
  desks: DeskView[] = []

  selectedOffice: OfficeCompact | null = null

  pagination: Pagination = {
    limit: 18,
    offset: 0,
    hasMoreResults: false
  };

  constructor(private officeService: OfficeService, private deskService: DeskService) {
  }

  async ngOnInit() {
    await this.fetchOffices()
    await this.fetchDesks(this.pagination.limit, this.pagination.offset)
  }

  async fetchOffices() {
    this.offices = await this.officeService.getCompactOffices();
    this.selectedOffice = this.offices[0] ?? null // TODO: Prefer user's assigned office instead
  }

  async fetchDesks(limit: number, offset: number) {
    if (this.selectedOffice) {
      let response = await this.deskService.getDeskListView(this.selectedOffice.id, limit, offset)
      this.desks = response.desks
      this.pagination = response.pagination
    } else {
      this.desks = []
    }
  }

  handleFilter(selectedOffice: OfficeCompact) {
    this.selectedOffice = selectedOffice
    this.selectedOfficeIdChange.emit(selectedOffice.id)
    this.fetchDesks(this.pagination.limit, 0)
  }

  openFilterDialog() {
    const dialogRef = this.deskFilterDialog.open(DeskFilterDialogComponent, {
      data: {
        offices: this.offices,
        selectedOfficeId: this.selectedOffice?.id,
      }
    });

    dialogRef.afterClosed().subscribe(selectedOffice => {
      if (selectedOffice)
        this.handleFilter(selectedOffice)
    });
  }

  createDesk(officeId: string, officeName: string) {
    const dialogRef = this.createDeskDialog.open(CreateDeskDialogComponent, {
      data: {officeId, officeName}
    });

    dialogRef.afterClosed().subscribe(createdDesk => {
      if (createdDesk) {
        this.fetchDesks(this.pagination.limit, 0)
      }
    });
  }

  editDesk(editDeskInitialValues: EditDeskInitialValuesDialogData) {
    const dialogRef = this.editDeskDialog.open(EditDeskDialogComponent, {
      data: editDeskInitialValues
    })

    dialogRef.afterClosed().subscribe(editedDesk => {
      if (editedDesk) {
        this.fetchDesks(this.pagination.limit, 0)
      }
    });
  }

  toggleEnabledDesk(deskId: string, deskName: string, officeName: string, enabled: boolean) {
    const dialogRef = this.toggleEnabledDeskDialog.open(ToggleEnabledDeskDialogComponent, {
      data: {
        officeName,
        deskId,
        deskName,
        enabled
      }
    });

    dialogRef.afterClosed().subscribe(editedDesk => {
      if (editedDesk) {
        this.fetchDesks(this.pagination.limit, 0)
      }
    })
  }

  deleteDesk(deskId: string, deskName: string, officeName: string) {
    const dialogRef = this.deleteDeskDialog.open(DeleteDeskDialogComponent, {data: {officeName, deskId, deskName}});

    dialogRef.afterClosed().subscribe(isConfirmed => {
      if (isConfirmed) {
        this.fetchDesks(this.pagination.limit, 0)
      }
    });
  }

  handlePageEvent(event: PageEvent) {
    this.fetchDesks(this.pagination.limit, event.pageIndex * this.pagination.limit);
  }
}
