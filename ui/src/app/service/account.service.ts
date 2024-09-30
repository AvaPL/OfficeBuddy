import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {CreateAccount} from "./model/account/create-account.model";
import {firstValueFrom} from "rxjs";
import {Account} from "./model/account/account.model";
import {AccountListView} from "./model/account/account-view.model";
import {AccountRole} from "./model/account/account-role.enum";
import {AccountCompact} from "./model/account/account-compact.model";
import {AuthService} from "./auth.service";
import {requestHeaders} from "./util/header.util";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  private baseUrl = '/api/internal/account';

  async getAccountListView(
    textSearchQuery: string | null,
    officeId: string | null,
    roles: AccountRole[] | null,
    limit: number,
    offset: number
  ): Promise<AccountListView> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    let url = `${this.baseUrl}/view/list?limit=${limit}&offset=${offset}`;
    if (textSearchQuery) url += `&text_search_query=${textSearchQuery}`;
    if (officeId) url += `&office_id=${officeId}`;
    if (roles) url += `&roles=${roles.join(',')}`;

    return firstValueFrom(this.http.get<AccountListView>(url, {headers}));
  }

  async getCompactAccounts(
    textSearchQuery: string | null,
    roles: AccountRole[] | null
  ): Promise<AccountCompact[]> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    // TODO: Introduce a dedicated endpoint that'll return only the id, name, and email of the accounts
    // return firstValueFrom(this.http.get<AccountCompact[]>(`${this.baseUrl}/view/compact`, {headers}));

    let url = `${this.baseUrl}/view/list?limit=1000&offset=0`;
    if (textSearchQuery) url += `&text_search_query=${textSearchQuery}`;
    if (roles) url += `&roles=${roles.join(',')}`;

    const accountListView = await firstValueFrom(this.http.get<AccountListView>(url, {headers}));
    return this.accountListViewToCompactAccounts(accountListView);
  }

  accountListViewToCompactAccounts(accountListView: AccountListView): AccountCompact[] {
    return accountListView.accounts.map(account => {
      return {
        id: account.id,
        firstName: account.firstName,
        lastName: account.lastName,
        email: account.email
      };
    });
  }

  async createAccount(account: CreateAccount): Promise<Account> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.post<Account>(this.baseUrl, account, {headers}));
  }

  async updateRole(accountId: string, role: AccountRole): Promise<void> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.put<void>(`${this.baseUrl}/${accountId}/role/${role}`, {}, {headers}));
  }

  async assignOffice(accountId: string, officeId: string): Promise<void> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.put<void>(`${this.baseUrl}/${accountId}/assigned-office-id/${officeId}`, {}, {headers}));
  }

  async archiveAccount(accountId: string): Promise<void> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.delete<void>(`${this.baseUrl}/${accountId}`, {headers}));
  }
}
