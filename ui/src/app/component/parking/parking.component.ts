import { Component } from '@angular/core';

enum Mode {
  RESERVATION,
  LIST
}

@Component({
  selector: 'app-parking',
  templateUrl: './parking.component.html',
  styleUrl: './parking.component.scss'
})
export class ParkingComponent {

  mode: Mode = Mode.RESERVATION;
  selectedOfficeId: string | null = null;

  changeSelectedOfficeId(selectedOfficeId: string | null) {
    this.selectedOfficeId = selectedOfficeId;
  }

  protected readonly Mode = Mode;
}
