import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {MediaMatcher} from "@angular/cdk/layout";
import {MatDialog} from "@angular/material/dialog";
import {
  DeskReservationConfirmDialogComponent
} from "./desk-reservation-confirm-dialog/desk-reservation-confirm-dialog.component";
import {DeskFilterDialogComponent} from "./desk-filter-dialog/desk-filter-dialog.component";
import {
  CreateDeskReservationDialogComponent
} from "./create-desk-reservation-dialog/create-desk-reservation-dialog.component";
import {ReservationState} from "./model/reservation-state.enum";
import {OfficeCompact} from "../../../../service/model/office/office-compact.model";
import {OfficeService} from "../../../../service/office.service";

@Component({
  selector: 'app-desk-reservation-view',
  templateUrl: './desk-reservation-view.component.html',
  styleUrl: './desk-reservation-view.component.scss'
})
export class DeskReservationViewComponent implements OnInit {

  @Input() selectedOfficeId!: string | null
  @Output() selectedOfficeIdChange = new EventEmitter<string>();
  @Output() changeToDesksView = new EventEmitter();

  readonly deskFilterDialog = inject(MatDialog);
  readonly deskReservationConfirmDialog = inject(MatDialog);
  readonly createDeskReservationDialog = inject(MatDialog);
  protected readonly ReservationState = ReservationState;

  notesTooltipQuery: MediaQueryList;

  offices: OfficeCompact[] = []
  reservations: any[] = [
    {
      id: "1",
      startDate: "2024-08-02",
      endDate: "2024-08-04",
      deskId: "1",
      deskName: "107/1",
      userId: "1",
      userName: "John Doe",
      state: "Pending",
      notes: "Please remove the duck from the desk, it scares me"
    },
    {
      id: "2",
      startDate: "2024-08-05",
      endDate: "2024-08-05",
      deskId: "2",
      deskName: "107/2",
      userId: "2",
      userName: "Jane Doe",
      state: "Confirmed",
      notes: ""
    },
    {
      id: "3",
      startDate: "2024-08-04",
      endDate: "2024-08-05",
      deskId: "3",
      deskName: "108/2",
      userId: "3",
      userName: "Sue Smith",
      state: "Pending",
      notes: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    },
    {
      id: "4",
      startDate: "2024-08-04",
      endDate: "2024-08-04",
      deskId: "4",
      deskName: "108/4",
      userId: "4",
      userName: "John Doe",
      state: "Rejected",
      notes: "Please accept!!!"
    },
    {
      id: "5",
      startDate: "2024-08-06",
      endDate: "2024-08-07",
      deskId: "5",
      deskName: "108/5",
      userId: "5",
      userName: "Jane Doe",
      state: "Pending",
      notes: "I won't join lunch"
    },
    {
      id: "6",
      startDate: "2024-08-08",
      endDate: "2024-08-09",
      deskId: "6",
      deskName: "109/1",
      userId: "6",
      userName: "Alice Johnson",
      state: "Pending",
      notes: "Need a quiet place to work"
    },
    {
      id: "7",
      startDate: "2024-08-10",
      endDate: "2024-08-11",
      deskId: "7",
      deskName: "109/2",
      userId: "7",
      userName: "Bob Brown",
      state: "Confirmed",
      notes: "Will bring my own chair"
    },
    {
      id: "8",
      startDate: "2024-08-12",
      endDate: "2024-08-13",
      deskId: "8",
      deskName: "110/1",
      userId: "8",
      userName: "Charlie Davis",
      state: "Cancelled",
      notes: "I have an important meeting"
    },
    {
      id: "9",
      startDate: "2024-08-14",
      endDate: "2024-08-15",
      deskId: "9",
      deskName: "110/2",
      userId: "9",
      userName: "Diana Evans",
      state: "Rejected",
      notes: ""
    },
    {
      id: "10",
      startDate: "2024-08-16",
      endDate: "2024-08-17",
      deskId: "10",
      deskName: "111/1",
      userId: "10",
      userName: "Eve Foster",
      state: "Pending",
      notes: "Need access to power outlet"
    },
    {
      id: "11",
      startDate: "2024-08-16",
      endDate: "2024-08-19",
      deskId: "11",
      deskName: "111/2",
      userId: "11",
      userName: "Frank Green",
      state: "Confirmed",
      notes: "Will be working late"
    },
    {
      id: "12",
      startDate: "2024-08-20",
      endDate: "2024-08-21",
      deskId: "12",
      deskName: "112/1",
      userId: "12",
      userName: "Grace Harris",
      state: "Cancelled",
      notes: ""
    },
    {
      id: "13",
      startDate: "2024-08-20",
      endDate: "2024-08-23",
      deskId: "13",
      deskName: "112/2",
      userId: "13",
      userName: "Henry Irving",
      state: "Rejected",
      notes: "Need this desk urgently"
    },
    {
      id: "14",
      startDate: "2024-08-24",
      endDate: "2024-08-25",
      deskId: "14",
      deskName: "113/1",
      userId: "14",
      userName: "Ivy Johnson",
      state: "Pending",
      notes: "Prefer a window seat"
    },
    {
      id: "15",
      startDate: "2024-08-26",
      endDate: "2024-08-27",
      deskId: "15",
      deskName: "113/2",
      userId: "15",
      userName: "Jack King",
      state: "Confirmed",
      notes: "Will need a monitor"
    },
    {
      id: "16",
      startDate: "2024-08-28",
      endDate: "2024-08-29",
      deskId: "16",
      deskName: "114/1",
      userId: "16",
      userName: "Kate Lee",
      state: "Confirmed",
      notes: ""
    }
  ]

  selectedOffice: OfficeCompact | null = null

  pageSize = 10
  pageIndex = 0
  selectedReservationStates = [ReservationState.PENDING, ReservationState.CONFIRMED]
  reservationStartDate = new Date()
  reservedByYou = false
  filteredReservations = this.filterReservations()
  reservationsPage = this.paginateAndGroupReservations()
  paginatorLength = this.totalReservations()

  constructor(
    private officeService: OfficeService,
    media: MediaMatcher,
  ) {
    this.notesTooltipQuery = media.matchMedia('(max-width: 374px)');
  }

  async ngOnInit() {
    await this.fetchOffices()
    this.handleFilter(this.selectedOfficeId, this.selectedReservationStates, this.reservationStartDate, this.reservedByYou)
  }

  async fetchOffices() {
    this.offices = await this.officeService.getCompactOffices();
    // TODO: Prefer user's assigned office instead
    this.selectedOffice = this.offices[0] ?? null
  }

  handlePageEvent(event: PageEvent) {
    this.pageIndex = event.pageIndex
    this.reservationsPage = this.paginateAndGroupReservations()
    this.paginatorLength = this.totalReservations()
  }

  selectOffice(selectedOfficeId: string | null) {
    const selectedOffice = this.offices.find(office => office.id === selectedOfficeId) || this.offices[0]
    if (selectedOfficeId)
      this.selectedOfficeIdChange.emit(selectedOffice.id)
    return selectedOffice
  }

  handleFilter(selectedOfficeId: string | null, selectedReservationStates: ReservationState[], reservationStartDate: Date, reservedByYou: boolean) {
    // TODO: Replace with more robust code, the below is temporary only
    console.log(`Filtering by officeId: ${selectedOfficeId}, selectedReservationStates: ${selectedReservationStates}, reservationStartDate: ${reservationStartDate}, reservedByYou: ${reservedByYou}`)
    this.reservedByYou = reservedByYou
    this.selectedOffice = this.selectOffice(selectedOfficeId)
    this.selectedReservationStates = selectedReservationStates
    this.reservationStartDate = reservationStartDate
    this.filteredReservations = this.filterReservations()
    this.pageIndex = 0
    this.reservationsPage = this.paginateAndGroupReservations()
    this.paginatorLength = this.totalReservations()
  }

  filterReservations() {
    return this.reservations.filter(reservation => !this.selectedReservationStates || this.selectedReservationStates.includes(reservation.state))
  }

  paginateAndGroupReservations(): { [key: string]: any[] } {
    const page = this.filteredReservations.slice(this.pageIndex * this.pageSize, (this.pageIndex + 1) * this.pageSize)
    const sortedPage = page.sort((a, b) => a.startDate.localeCompare(b.startDate))
    return sortedPage.reduce((groups, reservation) => {
      let today = new Date("2024-08-03"); // TODO: Allow setting this date, default to today
      const offset = today.getTimezoneOffset()
      today = new Date(today.getTime() - (offset * 60 * 1000))
      const todayString = today.toISOString().split('T')[0]
      const startDate = reservation.startDate > todayString ? reservation.startDate : todayString;
      if (!groups[startDate])
        groups[startDate] = [];
      groups[startDate].push(reservation);
      return groups;
    }, {} as { [key: string]: any[] });
  }

  totalReservations() {
    return Object.keys(this.filteredReservations).length
  }

  stateChipStyle(state: string) {
    switch (state) {
      case ReservationState.CANCELLED:
        return {
          backgroundColor: "rgb(191,191,191)",
          textColor: "white"
        }
      case ReservationState.CONFIRMED:
        return {
          backgroundColor: "rgb(121,181,117)",
          textColor: "white"
        }
      case ReservationState.PENDING:
        return {
          backgroundColor: "rgb(117, 179, 240)",
          textColor: "white"
        }
      case ReservationState.REJECTED:
        return {
          backgroundColor: "rgb(250, 104, 104)",
          textColor: "white"
        }
      default:
        return {
          backgroundColor: "grey",
          textColor: "white"
        }
    }
  }

  openFilterDialog() {
    const dialogRef = this.deskFilterDialog.open(DeskFilterDialogComponent, {
      data: {
        offices: this.offices,
        selectedOfficeId: this.selectedOffice?.id,
        selectedReservationStates: this.selectedReservationStates,
        reservationStartDate: this.reservationStartDate,
        reservedByYou: this.reservedByYou
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const {selectedOfficeId, selectedReservationStates, reservationStartDate, reservedByYou} = result
        this.handleFilter(selectedOfficeId, selectedReservationStates, reservationStartDate, reservedByYou)
      }
    });
  }

  confirmReservation(reservationId: string, deskName: string, userName: string, startDate: string, endDate: string) {
    const dialogRef = this.deskReservationConfirmDialog.open(DeskReservationConfirmDialogComponent, {
      data: {
        isWarn: false,
        buttonText: "Confirm",
        deskName,
        userName,
        startDate,
        endDate
      }
    });

    dialogRef.afterClosed().subscribe(isConfirmed => {
      if (isConfirmed) {
        console.log(`Confirmed reservation ${reservationId}`);
      } else {
        console.log(`Cancelled confirming reservation ${reservationId}`);
      }
    });
  }

  rejectReservation(reservationId: string, deskName: string, userName: string, startDate: string, endDate: string) {
    const dialogRef = this.deskReservationConfirmDialog.open(DeskReservationConfirmDialogComponent, {
      data: {
        isWarn: true,
        buttonText: "Reject",
        deskName,
        userName,
        startDate,
        endDate
      }
    });

    dialogRef.afterClosed().subscribe(isConfirmed => {
      if (isConfirmed) {
        console.log(`Rejected reservation ${reservationId}`);
      } else {
        console.log(`Cancelled rejecting reservation ${reservationId}`);
      }
    });
  }

  cancelReservation(reservationId: string, deskName: string, startDate: string, endDate: string) {
    const dialogRef = this.deskReservationConfirmDialog.open(DeskReservationConfirmDialogComponent, {
      data: {
        isWarn: true,
        buttonText: "Cancel",
        deskName,
        startDate,
        endDate
      }
    });

    dialogRef.afterClosed().subscribe(isConfirmed => {
      if (isConfirmed) {
        console.log(`Cancelled reservation ${reservationId}`);
      } else {
        console.log(`Cancelled cancelling reservation ${reservationId}`);
      }
    });
  }

  createReservation() {
    const dialogRef = this.createDeskReservationDialog.open(CreateDeskReservationDialogComponent,
      {
        data: {
          officeId: this.selectedOffice!.id,
          officeName: this.selectedOffice!.name
        }
      });

    dialogRef.afterClosed().subscribe(createdReservation => {
      if (createdReservation) {
        // TODO: Reload reservations after a new one is created
        console.log(`Created new reservation: `, createdReservation);
      }
    });
  }
}
