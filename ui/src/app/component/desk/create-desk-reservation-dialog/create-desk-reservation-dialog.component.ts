import {Component, inject} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {MediaMatcher} from "@angular/cdk/layout";
import {FormBuilder} from "@angular/forms";

@Component({
  selector: 'app-create-desk-reservation-dialog',
  templateUrl: './create-desk-reservation-dialog.component.html',
  styleUrl: './create-desk-reservation-dialog.component.scss',
})
export class CreateDeskReservationDialogComponent {

  desks = [
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
      isAvailable: false,
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
    }
  ]

  readonly dialogRef = inject(MatDialogRef<CreateDeskReservationDialogComponent>);
  readonly form = inject(FormBuilder).group({
    deskId: [[this.desks[0].id]],
    startDate: [],
    endDate: [],
    comment: [""]
  })

  touchUiQuery: MediaQueryList;

  constructor(
    media: MediaMatcher
  ) {
    this.touchUiQuery = media.matchMedia('(max-width: 600px)');
  }

  onSubmit() {
    this.dialogRef.close(this.form.value);
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
