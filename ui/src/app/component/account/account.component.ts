import {Component, inject, OnInit} from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {AccountRole} from "./model/account-role.enum";
import {FormControl} from "@angular/forms";
import {BehaviorSubject, combineLatest, map, startWith} from "rxjs";
import {DeleteAccountDialogComponent} from "./delete-account-dialog/delete-account-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {AccountFilterDialogComponent} from "./account-filter-dialog/account-filter-dialog.component";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss'
})
export class AccountComponent implements OnInit {

  readonly accountFilterDialog = inject(MatDialog);
  readonly deleteAccountDialog = inject(MatDialog);

  offices = [
    {
      id: "1",
      name: "Wroclaw Office",
    },
    {
      id: "2",
      name: "Krakow Office",
    },
    {
      id: "3",
      name: "Warsaw Office",
    }
  ]
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
    },
    {
      id: "12",
      name: "Jane Brown",
      email: "brownie@mymail.com",
      role: "User",
      assignedOffice: {
        id: "5",
        name: "New York Office",
      },
      managedOffices: []
    },
    {
      id: "13",
      name: "John Doe",
      email: "jjd@example.com",
      role: "User",
      assignedOffice: {
        id: "6",
        name: "San Francisco Office",
      },
      managedOffices: []
    }
  ]

  selectedOffice: { id: string, name: string } | null = null
  selectedRoles: AccountRole[] = Object.values(AccountRole)

  paginatorLength = this.accounts.length
  pageSize = 12;

  searchControl = new FormControl('');
  pageIndex = new BehaviorSubject<number>(0);
  searchFilteredAccounts = combineLatest([
    this.searchControl.valueChanges.pipe(startWith('')),
    this.pageIndex
  ]).pipe(
    map(([searchValue, pageIndex]) => this.filterAccounts(searchValue || '', pageIndex))
  );

  ngOnInit() {
    this.searchControl.valueChanges.subscribe(() => {
      this.pageIndex.next(0);
    });
  }

  private filterAccounts(value: string, pageIndex: number) {
    const filterValue = value.toLowerCase();

    const searchFilteredAccounts = this.accounts.filter(account =>
      account.name.toLowerCase().includes(filterValue) ||
      account.email.toLowerCase().includes(filterValue)
    );
    return this.paginateAccounts(searchFilteredAccounts, pageIndex)
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
    this.pageIndex.next(event.pageIndex);
  }

  paginateAccounts(searchFilteredAccounts: any[], pageIndex: number) {
    this.paginatorLength = searchFilteredAccounts.length
    return searchFilteredAccounts.slice(pageIndex * this.pageSize, (pageIndex + 1) * this.pageSize)
  }

  openFilterDialog() {
    const dialogRef = this.accountFilterDialog.open(AccountFilterDialogComponent, {
      data: {
        offices: this.offices,
        selectedOfficeId: this.selectedOffice?.id || null,
        selectedRoles: this.selectedRoles,
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const { selectedOfficeId, selectedReservationStates } = result;
        this.handleFilter(selectedOfficeId, selectedReservationStates);
      }
    });
  }

  handleFilter(selectedOfficeId: string | null, selectedRoles: AccountRole[]) {
    console.log(`Filtering by officeId: ${selectedOfficeId}, roles: ${selectedRoles}`);
    this.selectedOffice = this.offices.find(office => office.id === selectedOfficeId) || null
    this.selectedRoles = selectedRoles
  }

  deleteAccount(accountId: string, userName: string, email: string) {
    const dialogRef = this.deleteAccountDialog.open(DeleteAccountDialogComponent, {data: {userName, email}});

    dialogRef.afterClosed().subscribe(isConfirmed => {
      if (isConfirmed) {
        console.log(`Deleted account ${accountId}`);
      } else {
        console.log(`Cancelled deleting account ${accountId}`);
      }
    });
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
