import {Component} from '@angular/core';

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
    },
    {
      id: "4",
      name: "London Office",
      address: {
        addressLine1: "123 Baker Street",
        addressLine2: null,
        postalCode: "W1U 6TY",
        city: "London",
        country: "United Kingdom"
      },
      officeManagers: [
        "Sherlock Holmes"
      ],
      assignedAccounts: 1,
      desks: 1,
      parkingSpots: 0,
      rooms: 1,
      activeReservations: 0
    },
    {
      id: "5",
      name: "New York Office",
      address: {
        addressLine1: "123 Wall Street",
        addressLine2: null,
        postalCode: "10005",
        city: "New York",
        country: "United States"
      },
      officeManagers: [
        "John Smith"
      ],
      assignedAccounts: 0,
      desks: 0,
      parkingSpots: 0,
      rooms: 0,
      activeReservations: 0
    },
    {
      id: "6",
      name: "San Francisco Office",
      address: {
        addressLine1: "123 Market Street",
        addressLine2: null,
        postalCode: "94105",
        city: "San Francisco",
        country: "United States"
      },
      officeManagers: [
        "Jane Smith"
      ],
      assignedAccounts: 0,
      desks: 0,
      parkingSpots: 0,
      rooms: 0,
      activeReservations: 0
    },
    {
      id: "7",
      name: "Sydney Office",
      address: {
        addressLine1: "123 George Street",
        addressLine2: null,
        postalCode: "2000",
        city: "Sydney",
        country: "Australia"
      },
      officeManagers: [
        "Alice Brown"
      ],
      assignedAccounts: 0,
      desks: 0,
      parkingSpots: 0,
      rooms: 0,
      activeReservations: 0
    },
    {
      id: "8",
      name: "Tokyo Office",
      address: {
        addressLine1: "123 Ginza Street",
        addressLine2: null,
        postalCode: "104-0061",
        city: "Tokyo",
        country: "Japan"
      },
      officeManagers: [
        "Bob Johnson"
      ],
      assignedAccounts: 0,
      desks: 0,
      parkingSpots: 0,
      rooms: 0,
      activeReservations: 0
    },
    {
      id: "9",
      name: "Singapore Office",
      address: {
        addressLine1: "123 Orchard Road",
        addressLine2: null,
        postalCode: "238879",
        city: "Singapore",
        country: "Singapore"
      },
      officeManagers: [
        "Eve White"
      ],
      assignedAccounts: 0,
      desks: 0,
      parkingSpots: 0,
      rooms: 0,
      activeReservations: 0
    },
    {
      id: "10",
      name: "Dubai Office",
      address: {
        addressLine1: "123 Sheikh Zayed Road",
        addressLine2: null,
        postalCode: "123456",
        city: "Dubai",
        country: "United Arab Emirates"
      },
      officeManagers: [
        "Michael Black"
      ],
      assignedAccounts: 0,
      desks: 0,
      parkingSpots: 0,
      rooms: 0,
      activeReservations: 0
    }
  ]

  editManagers(officeId: string) {
    console.log(`Editing office managers of office ${officeId}`);
  }

  deleteOffice(officeId: string) {
    console.log(`Deleting office ${officeId}`);
  }

  addOffice() {
    console.log(`Adding new office`);
  }
}
