import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {OfficeCompact} from "../../../../service/model/office/office-compact.model";
import {OfficeService} from "../../../../service/office.service";
import {ParkingSpotService} from "../../../../service/parking-spot.service";
import {Pagination} from "../../../../service/model/pagination/pagination.model";
import {AuthService} from "../../../../service/auth.service";
import {ParkingSpotView} from "../../../../service/model/parking/parking-spot-view.model";
import {ParkingSpotFilterDialogComponent} from "../parking-spot-filter-dialog/parking-spot-filter-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {DeleteParkingSpotDialogComponent} from "../delete-parking-spot-dialog/delete-parking-spot-dialog.component";

@Component({
  selector: 'app-parking-spot-list-view',
  templateUrl: './parking-spot-list-view.component.html',
  styleUrl: './parking-spot-list-view.component.scss'
})
export class ParkingSpotListViewComponent implements OnInit {

  @Input() selectedOfficeId!: string | null
  @Output() selectedOfficeIdChange = new EventEmitter<string>();
  @Output() changeToReservationsView = new EventEmitter();

  readonly parkingSpotFilterDialog = inject(MatDialog);
  // readonly createParkingSpotDialog = inject(MatDialog);
  // readonly editParkingSpotDialog = inject(MatDialog);
  // readonly toggleEnabledParkingSpotDialog = inject(MatDialog);
  readonly deleteParkingSpotDialog = inject(MatDialog);

  offices: OfficeCompact[] = []
  parkingSpots: ParkingSpotView[] = []

  selectedOffice: OfficeCompact | null = null

  pagination: Pagination = {
    limit: 18,
    offset: 0,
    hasMoreResults: false
  };

  constructor(private officeService: OfficeService, private parkingSpotService: ParkingSpotService, protected authService: AuthService) {
  }

  async ngOnInit() {
    await this.fetchOffices()
    await this.fetchParkingSpots(this.pagination.limit, this.pagination.offset)
  }

  async fetchOffices() {
    this.offices = await this.officeService.getCompactOffices();
    this.selectedOffice = this.offices.find(
      office => office.id === this.selectedOfficeId
    ) ?? this.offices[0] ?? null // TODO: Prefer user's assigned office instead as fallback
  }

  async fetchParkingSpots(limit: number, offset: number) {
    if (this.selectedOffice) {
      let response = await this.parkingSpotService.getParkingSpotListView(this.selectedOffice.id, limit, offset)
      this.parkingSpots = response.parkingSpots
      this.pagination = response.pagination
    } else {
      this.parkingSpots = []
    }
  }

  handleFilter(selectedOffice: OfficeCompact) {
    this.selectedOffice = selectedOffice
    this.selectedOfficeIdChange.emit(selectedOffice.id)
    this.fetchParkingSpots(this.pagination.limit, 0)
  }

  openFilterDialog() {
    const dialogRef = this.parkingSpotFilterDialog.open(ParkingSpotFilterDialogComponent, {
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

  createParkingSpot(officeId: string, officeName: string) {
    // const dialogRef = this.createParkingSpotDialog.open(CreateParkingSpotDialogComponent, {
    //   data: {officeId, officeName}
    // });
    //
    // dialogRef.afterClosed().subscribe(createdParkingSpot => {
    //   if (createdParkingSpot) {
    //     this.fetchParkingSpots(this.pagination.limit, 0)
    //   }
    // });
  }

  editParkingSpot(editParkingSpotInitialValues: any) {
    // editParkingSpot(editParkingSpotInitialValues: EditParkingSpotInitialValuesDialogData) {
    // const dialogRef = this.editParkingSpotDialog.open(EditParkingSpotDialogComponent, {
    //   data: editParkingSpotInitialValues
    // })
    //
    // dialogRef.afterClosed().subscribe(editedParkingSpot => {
    //   if (editedParkingSpot) {
    //     this.fetchParkingSpots(this.pagination.limit, 0)
    //   }
    // });
  }

  toggleEnabledParkingSpot(parkingSpotId: string, parkingSpotName: string, officeName: string, enabled: boolean) {
    // const dialogRef = this.toggleEnabledParkingSpotDialog.open(ToggleEnabledParkingSpotDialogComponent, {
    //   data: {
    //     officeName,
    //     parkingSpotId,
    //     parkingSpotName,
    //     enabled
    //   }
    // });
    //
    // dialogRef.afterClosed().subscribe(editedParkingSpot => {
    //   if (editedParkingSpot) {
    //     this.fetchParkingSpots(this.pagination.limit, 0)
    //   }
    // })
  }

  deleteParkingSpot(parkingSpotId: string, parkingSpotName: string, officeName: string) {
    const dialogRef = this.deleteParkingSpotDialog.open(DeleteParkingSpotDialogComponent, {data: {officeName, parkingSpotId, parkingSpotName}});

    dialogRef.afterClosed().subscribe(isConfirmed => {
      if (isConfirmed) {
        this.fetchParkingSpots(this.pagination.limit, 0)
      }
    });
  }

  handlePageEvent(event: PageEvent) {
    this.fetchParkingSpots(this.pagination.limit, event.pageIndex * this.pagination.limit);
  }
}
