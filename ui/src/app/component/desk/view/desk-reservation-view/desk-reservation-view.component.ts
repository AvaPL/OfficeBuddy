import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {MediaMatcher} from "@angular/cdk/layout";
import {MatDialog} from "@angular/material/dialog";
import {
  DeskReservationConfirmDialogComponent
} from "./desk-reservation-confirm-dialog/desk-reservation-confirm-dialog.component";
import {
  DeskReservationFilterDialogComponent
} from "./desk-reservation-filter-dialog/desk-reservation-filter-dialog.component";
import {
  CreateDeskReservationDialogComponent
} from "./create-desk-reservation-dialog/create-desk-reservation-dialog.component";
import {OfficeCompact} from "../../../../service/model/office/office-compact.model";
import {OfficeService} from "../../../../service/office.service";
import {ReservationState} from "../../../../service/model/reservation/reservation-state.enum";
import {DeskReservationView, DeskView, UserView} from "../../../../service/model/reservation/desk-reservation-view.model";
import {Pagination} from "../../../../service/model/pagination/pagination.model";
import {ReservationService} from "../../../../service/reservation.service";
import {LocalDate} from "../../../../service/model/date/local-date.model";
import {AuthService} from "../../../../service/auth.service";

type GroupedDeskReservations = { [key: string]: DeskReservationView[] }

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

  currentUserId: string = ""

  notesTooltipQuery: MediaQueryList;

  offices: OfficeCompact[] = []
  groupedReservations: GroupedDeskReservations = {}

  pagination: Pagination = {
    limit: 10,
    offset: 0,
    hasMoreResults: false
  };

  selectedOffice: OfficeCompact | null = null
  selectedReservationStates = [ReservationState.PENDING, ReservationState.CONFIRMED]
  reservationStartDate = new Date()
  reservedByYou = false

  constructor(
    private officeService: OfficeService,
    private reservationService: ReservationService,
    private authService: AuthService,
    media: MediaMatcher,
  ) {
    this.notesTooltipQuery = media.matchMedia('(max-width: 374px)');
  }

  async ngOnInit() {
    await this.fetchOffices()
    await this.fetchReservations(this.pagination.limit, this.pagination.offset)
    this.currentUserId = await this.authService.getAccountId()
  }

  async fetchOffices() {
    this.offices = await this.officeService.getCompactOffices();
    this.selectedOffice = this.offices.find(
      office => office.id === this.selectedOfficeId
    ) ?? this.offices[0] ?? null // TODO: Prefer user's assigned office instead as fallback
  }

  async fetchReservations(limit: number, offset: number) {
    if (this.selectedOffice) {
      const userId = this.reservedByYou ? await this.authService.getAccountId() : null
      let response = await this.reservationService.getDeskReservationListView(
        this.selectedOffice.id,
        new LocalDate(this.reservationStartDate),
        this.selectedReservationStates,
        userId,
        limit,
        offset
      )
      this.groupedReservations = this.groupReservations(response.reservations)
      this.pagination = response.pagination
    } else {
      this.groupedReservations = {}
    }
  }

  groupReservations(reservations: DeskReservationView[]): GroupedDeskReservations {
    const reservationStartLocalDateString = new LocalDate(this.reservationStartDate).toString()
    return reservations.reduce((groups, reservation) => {
      const startDate = reservation.reservedFromDate > reservationStartLocalDateString ? reservation.reservedFromDate : reservationStartLocalDateString;
      if (!groups[startDate])
        groups[startDate] = [];
      groups[startDate].push(reservation);
      return groups;
    }, {} as GroupedDeskReservations);
  }

  handlePageEvent(event: PageEvent) {
    this.fetchReservations(this.pagination.limit, event.pageIndex * this.pagination.limit)
  }

  handleFilter(selectedOffice: OfficeCompact, selectedReservationStates: ReservationState[], reservationStartDate: Date, reservedByYou: boolean) {
    this.selectedOffice = selectedOffice
    this.selectedOfficeIdChange.emit(selectedOffice.id)
    this.selectedReservationStates = selectedReservationStates
    this.reservationStartDate = reservationStartDate
    this.reservedByYou = reservedByYou
    this.fetchReservations(this.pagination.limit, 0)
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
    const dialogRef = this.deskFilterDialog.open(DeskReservationFilterDialogComponent, {
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
        const {selectedOffice, selectedReservationStates, reservationStartDate, reservedByYou} = result
        if (selectedOffice)
          this.handleFilter(selectedOffice, selectedReservationStates, reservationStartDate, reservedByYou)
      }
    });
  }

  confirmReservation(reservationId: string, desk: DeskView, user: UserView, reservedFromDate: string, reservedToDate: string) {
    const dialogRef = this.deskReservationConfirmDialog.open(DeskReservationConfirmDialogComponent, {
      data: {
        isWarn: false,
        buttonText: "Confirm",
        deskName: desk.name,
        userFirstName: user.firstName,
        userLastName: user.lastName,
        reservedFromDate,
        reservedToDate,
        snackbarSuccessText: "Desk reservation confirmed",
        snackbarErrorText: "Unexpected error occurred when confirming desk reservation",
        sendRequest: async () => {
          return this.reservationService.confirmReservation('desk', reservationId)
        }
      }
    });

    dialogRef.afterClosed().subscribe(isConfirmed => {
      if (isConfirmed) {
        this.fetchReservations(this.pagination.limit, 0)
      }
    });
  }

  rejectReservation(reservationId: string, desk: DeskView, user: UserView, reservedFromDate: string, reservedToDate: string) {
    const dialogRef = this.deskReservationConfirmDialog.open(DeskReservationConfirmDialogComponent, {
      data: {
        isWarn: true,
        buttonText: "Reject",
        deskName: desk.name,
        userFirstName: user.firstName,
        userLastName: user.lastName,
        reservedFromDate,
        reservedToDate,
        snackbarSuccessText: "Desk reservation rejected",
        snackbarErrorText: "Unexpected error occurred when rejecting desk reservation",
        sendRequest: async () => {
          return this.reservationService.rejectReservation('desk', reservationId)
        }
      }
    });

    dialogRef.afterClosed().subscribe(isConfirmed => {
      if (isConfirmed) {
        this.fetchReservations(this.pagination.limit, 0)
      }
    });
  }

  cancelReservation(reservationId: string, desk: DeskView, reservedFromDate: string, reservedToDate: string) {
    const dialogRef = this.deskReservationConfirmDialog.open(DeskReservationConfirmDialogComponent, {
      data: {
        isWarn: true,
        buttonText: "Cancel",
        deskName: desk.name,
        reservedFromDate,
        reservedToDate,
        snackbarSuccessText: "Desk reservation cancelled",
        snackbarErrorText: "Unexpected error occurred when cancelling desk reservation",
        sendRequest: async () => {
          return this.reservationService.cancelReservation('desk', reservationId)
        }
      }
    });

    dialogRef.afterClosed().subscribe(isConfirmed => {
      if (isConfirmed) {
        this.fetchReservations(this.pagination.limit, 0)
      }
    });
  }

  createReservation(officeId: string, officeName: string) {
    const dialogRef = this.createDeskReservationDialog.open(CreateDeskReservationDialogComponent, {
      data: {officeId, officeName}
    });

    dialogRef.afterClosed().subscribe(createdReservation => {
      if (createdReservation) {
        this.fetchReservations(this.pagination.limit, 0)
      }
    });
  }

  canConfirmReservation(reservation: DeskReservationView) {
    return reservation.state === ReservationState.PENDING &&
      this.authService.hasOfficeManagerRole()
  }

  canRejectConfirmedReservation(reservation: DeskReservationView) {
    return reservation.state === ReservationState.CONFIRMED &&
      this.authService.hasOfficeManagerRole()
  }

  canRejectPendingReservation(reservation: DeskReservationView) {
    return reservation.state === ReservationState.PENDING &&
      this.authService.hasOfficeManagerRole()
  }

  canCancelReservation(reservation: DeskReservationView) {
    return (reservation.state === ReservationState.PENDING || reservation.state === ReservationState.CONFIRMED) &&
      reservation.user.id === this.currentUserId
  }
}
