import {Injectable} from "@angular/core";
import {KeycloakService} from "keycloak-angular";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CreateAccount} from "./model/account/create-account.model";
import {firstValueFrom} from "rxjs";
import {Account} from "./model/account/account.model";
import {AccountListView} from "./model/account/account-view.model";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private http: HttpClient, private keycloakService: KeycloakService) {
  }

  private baseUrl = '/api/internal/account';

  // TODO: Add filters
  async getAccountListView(limit: number, offset: number): Promise<AccountListView> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return firstValueFrom(this.http.get<AccountListView>(`${this.baseUrl}/view/list?limit=${limit}&offset=${offset}`, {headers}));
  }

  async createAccount(account: CreateAccount): Promise<Account> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return firstValueFrom(this.http.post<Account>(this.baseUrl, account, {headers}));
  }
}
