import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {MediaMatcher} from "@angular/cdk/layout";
import {MatDialog} from "@angular/material/dialog";
import {OfficeCompact} from "../../../../service/model/office/office-compact.model";
import {OfficeService} from "../../../../service/office.service";
import {ReservationState} from "../../../../service/model/reservation/reservation-state.enum";
import {UserView} from "../../../../service/model/reservation/desk-reservation-view.model";
import {Pagination} from "../../../../service/model/pagination/pagination.model";
import {ReservationService} from "../../../../service/reservation.service";
import {LocalDate} from "../../../../service/model/date/local-date.model";
import {AuthService} from "../../../../service/auth.service";
import {
  ParkingSpotReservationView,
  ParkingSpotView
} from "../../../../service/model/reservation/parking-spot-reservation-view.model";
import {
  ParkingSpotReservationFilterDialogComponent
} from "./parking-spot-reservation-filter-dialog/parking-spot-reservation-filter-dialog.component";

type GroupedParkingSpotReservations = { [key: string]: ParkingSpotReservationView[] }

@Component({
  selector: 'app-parking-spot-reservation-view',
  templateUrl: './parking-spot-reservation-view.component.html',
  styleUrls: ['./parking-spot-reservation-view.component.scss']
})
export class ParkingSpotReservationViewComponent implements OnInit {

  @Input() selectedOfficeId!: string | null;
  @Output() selectedOfficeIdChange = new EventEmitter<string>();
  @Output() changeToParkingSpotsView = new EventEmitter();

  readonly parkingSpotFilterDialog = inject(MatDialog);
  readonly parkingSpotReservationConfirmDialog = inject(MatDialog);
  readonly createParkingSpotReservationDialog = inject(MatDialog);
  protected readonly ReservationState = ReservationState;

  currentUserId: string = "";

  notesTooltipQuery: MediaQueryList;

  offices: OfficeCompact[] = [];
  groupedReservations: GroupedParkingSpotReservations = {};

  pagination: Pagination = {
    limit: 10,
    offset: 0,
    hasMoreResults: false
  };

  selectedOffice: OfficeCompact | null = null;
  selectedReservationStates = [ReservationState.PENDING, ReservationState.CONFIRMED];
  reservationStartDate = new Date();
  reservedByYou = false;
  plateNumber: string | null = null;

  constructor(
    private officeService: OfficeService,
    private reservationService: ReservationService,
    private authService: AuthService,
    media: MediaMatcher,
  ) {
    this.notesTooltipQuery = media.matchMedia('(max-width: 374px)');
  }

  async ngOnInit() {
    await this.fetchOffices();
    await this.fetchReservations(this.pagination.limit, this.pagination.offset);
    this.currentUserId = await this.authService.getAccountId();
  }

  async fetchOffices() {
    this.offices = await this.officeService.getCompactOffices();
    this.selectedOffice = this.offices.find(
      office => office.id === this.selectedOfficeId
    ) ?? this.offices[0] ?? null;
  }

  async fetchReservations(limit: number, offset: number) {
    if (this.selectedOffice) {
      const userId = this.reservedByYou ? await this.authService.getAccountId() : null;
      let response = await this.reservationService.getParkingSpotReservationListView(
        this.selectedOffice.id,
        new LocalDate(this.reservationStartDate),
        this.selectedReservationStates,
        userId,
        this.plateNumber,
        limit,
        offset
      );
      this.groupedReservations = this.groupReservations(response.reservations);
      this.pagination = response.pagination;
    } else {
      this.groupedReservations = {};
    }
  }

  groupReservations(reservations: ParkingSpotReservationView[]): GroupedParkingSpotReservations {
    const reservationStartLocalDateString = new LocalDate(this.reservationStartDate).toString();
    return reservations.reduce((groups, reservation) => {
      const startDate = reservation.reservedFromDate > reservationStartLocalDateString ? reservation.reservedFromDate : reservationStartLocalDateString;
      if (!groups[startDate])
        groups[startDate] = [];
      groups[startDate].push(reservation);
      return groups;
    }, {} as GroupedParkingSpotReservations);
  }

  handlePageEvent(event: PageEvent) {
    this.fetchReservations(this.pagination.limit, event.pageIndex * this.pagination.limit);
  }

  handleFilter(selectedOffice: OfficeCompact, selectedReservationStates: ReservationState[], reservationStartDate: Date, reservedByYou: boolean) {
    this.selectedOffice = selectedOffice;
    this.selectedOfficeIdChange.emit(selectedOffice.id);
    this.selectedReservationStates = selectedReservationStates;
    this.reservationStartDate = reservationStartDate;
    this.reservedByYou = reservedByYou;
    this.fetchReservations(this.pagination.limit, 0);
  }

  stateChipStyle(state: string) {
    switch (state) {
      case ReservationState.CANCELLED:
        return {
          backgroundColor: "rgb(191,191,191)",
          textColor: "white"
        };
      case ReservationState.CONFIRMED:
        return {
          backgroundColor: "rgb(121,181,117)",
          textColor: "white"
        };
      case ReservationState.PENDING:
        return {
          backgroundColor: "rgb(117, 179, 240)",
          textColor: "white"
        };
      case ReservationState.REJECTED:
        return {
          backgroundColor: "rgb(250, 104, 104)",
          textColor: "white"
        };
      default:
        return {
          backgroundColor: "grey",
          textColor: "white"
        };
    }
  }

  openFilterDialog() {
    const dialogRef = this.parkingSpotFilterDialog.open(ParkingSpotReservationFilterDialogComponent, {
      data: {
        offices: this.offices,
        selectedOfficeId: this.selectedOffice?.id,
        selectedReservationStates: this.selectedReservationStates,
        reservationStartDate: this.reservationStartDate,
        reservedByYou: this.reservedByYou,
        plateNumber: this.plateNumber
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const { selectedOffice, selectedReservationStates, reservationStartDate, reservedByYou } = result;
        if (selectedOffice)
          this.handleFilter(selectedOffice, selectedReservationStates, reservationStartDate, reservedByYou);
      }
    });
  }

  confirmReservation(reservationId: string, parkingSpot: ParkingSpotView, user: UserView, reservedFromDate: string, reservedToDate: string) {
    // const dialogRef = this.parkingSpotReservationConfirmDialog.open(ParkingSpotReservationConfirmDialogComponent, {
    //   data: {
    //     isWarn: false,
    //     buttonText: "Confirm",
    //     parkingSpotName: parkingSpot.name,
    //     userFirstName: user.firstName,
    //     userLastName: user.lastName,
    //     reservedFromDate,
    //     reservedToDate,
    //     snackbarSuccessText: "Parking spot reservation confirmed",
    //     snackbarErrorText: "Unexpected error occurred when confirming parking spot reservation",
    //     sendRequest: async () => {
    //       return this.reservationService.confirmParkingSpotReservation(reservationId);
    //     }
    //   }
    // });
    //
    // dialogRef.afterClosed().subscribe(isConfirmed => {
    //   if (isConfirmed) {
    //     this.fetchReservations(this.pagination.limit, 0);
    //   }
    // });
  }

  rejectReservation(reservationId: string, parkingSpot: ParkingSpotView, user: UserView, reservedFromDate: string, reservedToDate: string) {
    // const dialogRef = this.parkingSpotReservationConfirmDialog.open(ParkingSpotReservationConfirmDialogComponent, {
    //   data: {
    //     isWarn: true,
    //     buttonText: "Reject",
    //     parkingSpotName: parkingSpot.name,
    //     userFirstName: user.firstName,
    //     userLastName: user.lastName,
    //     reservedFromDate,
    //     reservedToDate,
    //     snackbarSuccessText: "Parking spot reservation rejected",
    //     snackbarErrorText: "Unexpected error occurred when rejecting parking spot reservation",
    //     sendRequest: async () => {
    //       return this.reservationService.rejectParkingSpotReservation(reservationId);
    //     }
    //   }
    // });
    //
    // dialogRef.afterClosed().subscribe(isConfirmed => {
    //   if (isConfirmed) {
    //     this.fetchReservations(this.pagination.limit, 0);
    //   }
    // });
  }

  cancelReservation(reservationId: string, parkingSpot: ParkingSpotView, reservedFromDate: string, reservedToDate: string) {
    // const dialogRef = this.parkingSpotReservationConfirmDialog.open(ParkingSpotReservationConfirmDialogComponent, {
    //   data: {
    //     isWarn: true,
    //     buttonText: "Cancel",
    //     parkingSpotName: parkingSpot.name,
    //     reservedFromDate,
    //     reservedToDate,
    //     snackbarSuccessText: "Parking spot reservation cancelled",
    //     snackbarErrorText: "Unexpected error occurred when cancelling parking spot reservation",
    //     sendRequest: async () => {
    //       return this.reservationService.cancelParkingSpotReservation(reservationId);
    //     }
    //   }
    // });
    //
    // dialogRef.afterClosed().subscribe(isConfirmed => {
    //   if (isConfirmed) {
    //     this.fetchReservations(this.pagination.limit, 0);
    //   }
    // });
  }

  createReservation(officeId: string, officeName: string) {
    // const dialogRef = this.createParkingSpotReservationDialog.open(CreateParkingSpotReservationDialogComponent, {
    //   data: { officeId, officeName }
    // });
    //
    // dialogRef.afterClosed().subscribe(createdReservation => {
    //   if (createdReservation) {
    //     this.fetchReservations(this.pagination.limit, 0);
    //   }
    // });
  }

  canConfirmReservation(reservation: ParkingSpotReservationView) {
    return reservation.state === ReservationState.PENDING &&
      this.authService.hasOfficeManagerRole();
  }

  canRejectConfirmedReservation(reservation: ParkingSpotReservationView) {
    return reservation.state === ReservationState.CONFIRMED &&
      this.authService.hasOfficeManagerRole();
  }

  canRejectPendingReservation(reservation: ParkingSpotReservationView) {
    return reservation.state === ReservationState.PENDING &&
      this.authService.hasOfficeManagerRole();
  }

  canCancelReservation(reservation: ParkingSpotReservationView) {
    return (reservation.state === ReservationState.PENDING || reservation.state === ReservationState.CONFIRMED) &&
      reservation.user.id === this.currentUserId;
  }
}
