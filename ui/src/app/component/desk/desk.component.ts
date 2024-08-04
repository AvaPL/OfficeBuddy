import {ChangeDetectorRef, Component, inject, resolveForwardRef} from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {MediaMatcher} from "@angular/cdk/layout";
import {MatDialog} from "@angular/material/dialog";
import {CreateOfficeDialogComponent} from "../office/create-office-dialog/create-office-dialog.component";
import {DeskReservationConfirmDialogComponent} from "./confirm-dialog/desk-reservation-confirm-dialog.component";

enum ReservationState {
  CANCELLED = "Cancelled",
  CONFIRMED = "Confirmed",
  PENDING = "Pending",
  REJECTED = "Rejected"
}

@Component({
  selector: 'app-desks',
  templateUrl: './desk.component.html',
  styleUrl: './desk.component.scss'
})
export class DeskComponent {

  readonly deskReservationConfirmDialog = inject(MatDialog);
  protected readonly ReservationState = ReservationState;

  reservations = [
    {
      id: "1",
      startDate: "2024-08-03",
      endDate: "2024-08-03",
      deskId: "1",
      deskName: "107/1",
      userId: "1",
      userName: "John Doe",
      state: "Pending",
      notes: "Please remove the duck from the desk, it scares me"
    },
    {
      id: "2",
      startDate: "2024-08-03",
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
      state: "Cancelled",
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
      startDate: "2024-08-04",
      endDate: "2024-08-06",
      deskId: "5",
      deskName: "108/5",
      userId: "5",
      userName: "Jane Doe",
      state: "Pending",
      notes: "I won't join lunch"
    }
  ]

  notesTooltipQuery: MediaQueryList;
  pageSize = 20
  pageIndex = 0
  reservationsPage = this.paginateReservations()

  constructor(
    media: MediaMatcher,
  ) {
    this.notesTooltipQuery = media.matchMedia('(max-width: 374px)');
  }

  handlePageEvent(event: PageEvent) {
    this.pageIndex = event.pageIndex
    this.reservationsPage = this.paginateReservations()
  }

  paginateReservations() {
    return this.reservations.slice(this.pageIndex * this.pageSize, (this.pageIndex + 1) * this.pageSize)
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
}
