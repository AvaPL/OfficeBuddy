import {Component} from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {MediaMatcher} from "@angular/cdk/layout";
import {AccountRole} from "./model/account-role.enum";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss'
})
export class AccountComponent {

  accounts = [
    {
      id: "1",
      name: "John Doe",
      email: "john.doe@example.com",
      role: "SuperAdmin",
      assignedOffice: {
        id: "1",
        name: "Wroclaw Office",
      },
      managedOffices: [
        {
          id: "1",
          name: "Wroclaw Office",
        },
        {
          id: "2",
          name: "Krakow Office",
        }
      ]
    },
    {
      id: "2",
      name: "Jane Doe",
      email: "jane.doe@example.com",
      role: "OfficeManager",
      assignedOffice: {
        id: "2",
        name: "Krakow Office",
      },
      managedOffices: [
        {
          id: "2",
          name: "Krakow Office",
        }
      ]
    },
    {
      id: "3",
      name: "Sue Smith",
      email: "sue.smith@mymail.com",
      role: "User",
      assignedOffice: {
        id: "2",
        name: "Krakow Office",
      },
      managedOffices: []
    },
    {
      id: "4",
      name: "John Smith",
      email: "john.smith@mymail.com",
      role: "User",
      assignedOffice: {
        id: "3",
        name: "Warsaw Office",
      },
      managedOffices: []
    },
    {
      id: "5",
      name: "James Brown",
      email: "jb@example.com",
      role: "User",
      assignedOffice: {
        id: "3",
        name: "Warsaw Office",
      },
      managedOffices: []
    },
    {
      id: "6",
      name: "Jack Black",
      email: "jackb@mymail.eu",
      role: "OfficeManager",
      assignedOffice: {
        id: "4",
        name: "London Office",
      },
      managedOffices: [
        {
          id: "4",
          name: "London Office",
        }
      ]
    },
    {
      id: "7",
      name: "John Smith",
      email: "js@example.com",
      role: "User",
      assignedOffice: {
        id: "4",
        name: "London Office",
      },
      managedOffices: []
    },
    {
      id: "8",
      name: "Jane Smith",
      email: "jane@mymail.com",
      role: "User",
      assignedOffice: {
        id: "1",
        name: "Wroclaw Office",
      },
      managedOffices: []
    },
    {
      id: "9",
      name: "Jack Smith",
      email: "jackjack@smith.com",
      role: "User",
      assignedOffice: {
        id: "1",
        name: "Wroclaw Office",
      },
      managedOffices: []
    },
    {
      id: "10",
      name: "Jane Black",
      email: "jane.was.taken@mymail.com",
      role: "User",
      assignedOffice: {
        id: "4",
        name: "London Office",
      },
      managedOffices: []
    },
    {
      id: "11",
      name: "John Brown",
      email: "johnny@example.com",
      role: "SuperAdmin",
      assignedOffice: {
        id: "5",
        name: "New York Office",
      },
      managedOffices: [
        {
          id: "5",
          name: "New York Office",
        }
      ]
    }
  ]

  squeezeOffices: MediaQueryList;

  pageSize = 10;
  pageIndex = 0;
  accountsPage = this.paginateAccounts()

  constructor(
    media: MediaMatcher,
  ) {
    this.squeezeOffices = media.matchMedia('(max-width: 700px)');
  }

  roleChipStyle(role: string) {
    switch (role) {
      case AccountRole.SUPER_ADMIN:
        return {
          displayName: "Super Admin",
          backgroundColor: "rgb(64,64,64)",
        }
      case AccountRole.OFFICE_MANAGER:
        return {
          displayName: "Office Manager",
          backgroundColor: "rgb(117, 179, 240)",
        }
      case AccountRole.USER:
        return {
          displayName: "User",
          backgroundColor: "rgb(121,181,117)",
        }
      default:
        return {
          displayName: "Unknown",
          backgroundColor: "grey",
        }
    }
  }

  joinWithComma(managedOffices: { id: string, name: string }[]) {
    return managedOffices.map(office => office.name).join(", ");
  }

  handlePageEvent(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.accountsPage = this.paginateAccounts();
  }

  paginateAccounts() {
    return this.accounts.slice(this.pageIndex * this.pageSize, (this.pageIndex + 1) * this.pageSize)
  }

  // TODO: To be removed, only to validate integration with Keycloak
  // isLoggedIn = false;
  // userProfile: KeycloakProfile | null = null;
  // formattedRoles: string = "<empty>";
  // formattedAttributes: string = "<empty>";
  //
  // constructor(private readonly keycloak: KeycloakService) {
  // }
  //
  // async ngOnInit() {
  //   this.isLoggedIn = this.keycloak.isLoggedIn();
  //
  //   if (this.isLoggedIn) {
  //     this.userProfile = await this.keycloak.loadUserProfile();
  //     this.formattedRoles = this.keycloak.getUserRoles(true).join(", ");
  //     this.formattedAttributes = JSON.stringify(this.userProfile.attributes, null, 1);
  //   }
  // }
}
