import {Component} from '@angular/core';

enum Mode {
  RESERVATION,
  LIST
}

@Component({
  selector: 'app-desks',
  templateUrl: './desk.component.html',
  styleUrl: './desk.component.scss'
})
export class DeskComponent {

  mode: Mode = Mode.RESERVATION;
  selectedOfficeId: string | null = null;

  changeSelectedOfficeId(selectedOfficeId: string | null) {
    this.selectedOfficeId = selectedOfficeId;
  }

  protected readonly Mode = Mode;
}
