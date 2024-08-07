import {Component, inject} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {MediaMatcher} from "@angular/cdk/layout";

@Component({
  selector: 'app-create-desk-reservation-dialog',
  templateUrl: './create-desk-reservation-dialog.component.html',
  styleUrl: './create-desk-reservation-dialog.component.scss'
})
export class CreateDeskReservationDialogComponent {

  readonly dialogRef = inject(MatDialogRef<CreateDeskReservationDialogComponent>);

  touchUiQuery: MediaQueryList;

  constructor(
    media: MediaMatcher
  ) {
    this.touchUiQuery = media.matchMedia('(max-width: 600px)');
  }

  desks = [
    {
      name: "107/1",
      isAvailable: true,
      isStanding: true,
      monitorsCount: 2,
      hasPhone: true,
    },
    {
      name: "107/2",
      isAvailable: false,
      isStanding: false,
      monitorsCount: 1,
      hasPhone: false,
    },
    {
      name: "107/3",
      isAvailable: true,
      isStanding: false,
      monitorsCount: 1,
      hasPhone: true,
    },
    {
      name: "107/4",
      isAvailable: false,
      isStanding: true,
      monitorsCount: 2,
      hasPhone: false
    },
    {
      name: "108/1",
      isAvailable: true,
      isStanding: true,
      monitorsCount: 2,
      hasPhone: true,
    },
    {
      name: "108/2",
      isAvailable: false,
      isStanding: false,
      monitorsCount: 1,
      hasPhone: false,
    },
    {
      name: "108/3",
      isAvailable: true,
      isStanding: false,
      monitorsCount: 1,
      hasPhone: true,
    },
    {
      name: "108/4",
      isAvailable: false,
      isStanding: true,
      monitorsCount: 2,
      hasPhone: false
    }
  ]

  onConfirm() {
    this.dialogRef.close(true);
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
