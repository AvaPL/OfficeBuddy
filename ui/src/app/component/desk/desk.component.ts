import {Component, inject} from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {MediaMatcher} from "@angular/cdk/layout";
import {MatDialog} from "@angular/material/dialog";
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
    }
  ]

  notesTooltipQuery: MediaQueryList;
  pageSize = 10
  pageIndex = 0
  reservationsPage = this.paginateAndGroupReservations()

  constructor(
    media: MediaMatcher,
  ) {
    this.notesTooltipQuery = media.matchMedia('(max-width: 374px)');
  }

  handlePageEvent(event: PageEvent) {
    this.pageIndex = event.pageIndex
    this.reservationsPage = this.paginateAndGroupReservations()
  }

  paginateAndGroupReservations() {
    const page = this.reservations.slice(this.pageIndex * this.pageSize, (this.pageIndex + 1) * this.pageSize)
    const sortedPage = page.sort((a, b) => a.startDate.localeCompare(b.startDate))
    return sortedPage.reduce((groups, reservation) => {
      let today = new Date("2024-08-03"); // TODO: Use current date
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
