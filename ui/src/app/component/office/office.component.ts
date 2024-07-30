import { Component } from '@angular/core';

@Component({
  selector: 'app-office',
  templateUrl: './office.component.html',
  styleUrl: './office.component.scss'
})
export class OfficeComponent {

  offices = [
    {
      id: "1",
      name: "Wrocław Office",
      address: {
        addressLine1: "ul. Powstańców Śląskich 9",
        addressLine2: "1st floor",
        postalCode: "53-332",
        city: "Wrocław",
        country: "Poland"
      },
      officeManagers: [
        "John Doe",
        "Jane Doe"
      ],
      assignedAccounts: 123,
      desks: 9,
      parkingSpots: 2,
      rooms: 6,
      activeReservations: 42
    },
    {
      id: "2",
      name: "Kraków Office",
      address: {
        addressLine1: "ul. Krzywa 12",
        addressLine2: null,
        postalCode: "12-443",
        city: "Kraków",
        country: "Poland"
      },
      officeManagers: [
        "Sue Smith"
      ],
      assignedAccounts: 42,
      desks: 30,
      parkingSpots: 9,
      rooms: 3,
      activeReservations: 5
    },
    {
      id: "3",
      name: "Warsaw Office",
      address: {
        addressLine1: "Plac Bankowy 2",
        addressLine2: "5th floor",
        postalCode: "99-100",
        city: "Warsaw",
        country: "Poland"
      },
      officeManagers: [
        "James Brown"
      ],
      assignedAccounts: 12,
      desks: 3,
      parkingSpots: 1,
      rooms: 1,
      activeReservations: 1
    }
  ]

  editManagers(officeId: string) {
    console.log(`Assigning office manager to office ${officeId}`);
  }

  deleteOffice(officeId: string) {
    console.log(`Deleting office ${officeId}`);
  }
}
