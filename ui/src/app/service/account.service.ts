import {Injectable} from "@angular/core";
import {KeycloakService} from "keycloak-angular";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CreateAccount} from "./model/account/create-account.model";
import {firstValueFrom} from "rxjs";
import {Account} from "./model/account/account.model";
import {AccountListView} from "./model/account/account-view.model";
import {AccountRole} from "./model/account/account-role.enum";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private http: HttpClient, private keycloakService: KeycloakService) {
  }

  private baseUrl = '/api/internal/account';

  async getAccountListView(
    textSearchQuery: string | null,
    officeId: string | null,
    roles: AccountRole[] | null,
    limit: number,
    offset: number
  ): Promise<AccountListView> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    let url = `${this.baseUrl}/view/list?limit=${limit}&offset=${offset}`;
    if (textSearchQuery) url += `&text_search_query=${textSearchQuery}`;
    if (officeId) url += `&office_id=${officeId}`;
    if (roles) url += `&roles=${roles.join(',')}`;

    return firstValueFrom(this.http.get<AccountListView>(url, {headers}));
  }

  async createAccount(account: CreateAccount): Promise<Account> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return firstValueFrom(this.http.post<Account>(this.baseUrl, account, {headers}));
  }

  async archiveAccount(accountId: string): Promise<void> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return firstValueFrom(this.http.delete<void>(`${this.baseUrl}/${accountId}`, {headers}));
  }
}
