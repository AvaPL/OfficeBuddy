import {Component, ElementRef, inject, OnInit, ViewChild} from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {AccountRole, AccountRoleCompanion} from "./model/account-role.enum";
import {FormControl} from "@angular/forms";
import {BehaviorSubject, combineLatest, map, startWith} from "rxjs";
import {DeleteAccountDialogComponent} from "./delete-account-dialog/delete-account-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {AccountFilterDialogComponent} from "./account-filter-dialog/account-filter-dialog.component";
import {ChangeAccountRoleDialogComponent} from "./change-account-role-dialog/change-account-role-dialog.component";
import {CreateAccountDialogComponent} from "./create-account-dialog/create-account-dialog.component";
import {AccountView, OfficeView} from "../../service/model/account/account-view.model";
import {Pagination} from "../../service/model/pagination/pagination.model";
import {AccountService} from "../../service/account.service";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss'
})
export class AccountComponent implements OnInit {

  @ViewChild('accountCards') accountCards!: ElementRef;

  readonly accountFilterDialog = inject(MatDialog);
  readonly createAccountDialog = inject(MatDialog);
  readonly changeAccountRoleDialog = inject(MatDialog);
  readonly deleteAccountDialog = inject(MatDialog);

  offices = [
    {
      id: "339dc14c-c22c-4114-9227-114c447bdac3",
      name: "Wroclaw Office",
    },
    {
      id: "4ca02ab8-11f1-40a1-a1e2-01cccdb000c5",
      name: "Krakow Office",
    },
    {
      id: "90ef9700-e8a8-4c55-a119-5f0743e2b2c5",
      name: "Warsaw Office",
    }
  ]
  accounts: AccountView[] = []

  selectedOffice: { id: string, name: string } | null = null
  selectedRoles: AccountRole[] = Object.values(AccountRole)

  pagination: Pagination = {
    limit: 12,
    offset: 0,
    hasMoreResults: false
  };

  searchControl = new FormControl('');

  constructor(private accountService: AccountService) {
  }

  ngOnInit() {
    this.fetchAccounts(this.pagination.limit, this.pagination.offset);
    this.searchControl.valueChanges.subscribe(() => {
      this.fetchAccounts(this.pagination.limit, 0);
    });
  }

  async fetchAccounts(limit: number, offset: number) {
    let response = await this.accountService.getAccountListView(
      this.searchControl.value,
      this.selectedOffice?.id || null,
      this.selectedRoles,
      limit,
      offset
    );
    this.accounts = response.accounts;
    this.pagination = response.pagination;
    this.accountCards.nativeElement.scrollIntoView(true);
  }

  roleChipBackgroundColor(role: string) {
    switch (role) {
      case AccountRole.SUPER_ADMIN:
        return "rgb(64,64,64)"
      case AccountRole.OFFICE_MANAGER:
        return "rgb(117, 179, 240)"
      case AccountRole.USER:
        return "rgb(121,181,117)"
      default:
        return "grey"
    }
  }

  joinWithComma(managedOffices: OfficeView[]) {
    return managedOffices.map(office => office.name).join(", ");
  }

  handlePageEvent(event: PageEvent) {
    this.fetchAccounts(this.pagination.limit, event.pageIndex * this.pagination.limit);
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
        const {selectedOfficeId, selectedReservationStates} = result;
        this.handleFilter(selectedOfficeId, selectedReservationStates);
      }
    });
  }

  handleFilter(selectedOfficeId: string | null, selectedRoles: AccountRole[]) {
    this.selectedOffice = this.offices.find(office => office.id === selectedOfficeId) || null
    this.selectedRoles = selectedRoles
    this.fetchAccounts(this.pagination.limit, 0)
  }

  createAccount() {
    const dialogRef = this.createAccountDialog.open(CreateAccountDialogComponent, {data: {offices: this.offices}});

    dialogRef.afterClosed().subscribe(createdAccount => {
      if (createdAccount) {
        this.fetchAccounts(this.pagination.limit, 0)
      }
    });
  }

  changeRole(accountId: string, firstName: string, lastName: string, currentRole: AccountRole) {
    const dialogRef = this.changeAccountRoleDialog.open(ChangeAccountRoleDialogComponent, {
      data: {
        accountId,
        firstName,
        lastName,
        currentRole
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log(`Changed role for account ${accountId} to ${result.selectedRole}`);
      } else {
        console.log(`Cancelled changing role for account ${accountId}`);
      }
    });
  }

  deleteAccount(accountId: string, firstName: string, lastName: string, email: string) {
    const dialogRef = this.deleteAccountDialog.open(DeleteAccountDialogComponent, {data: {firstName, lastName, email}});

    dialogRef.afterClosed().subscribe(isConfirmed => {
      if (isConfirmed) {
        console.log(`Deleted account ${accountId}`);
      } else {
        console.log(`Cancelled deleting account ${accountId}`);
      }
    });
  }

  protected readonly AccountRole = AccountRole;
  protected readonly AccountRoleCompanion = AccountRoleCompanion;
}
