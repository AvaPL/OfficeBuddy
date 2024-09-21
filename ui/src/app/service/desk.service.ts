import {Injectable} from "@angular/core";
import {KeycloakService} from "keycloak-angular";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CreateDesk} from "./model/desk/create-desk.model";
import {firstValueFrom} from "rxjs";
import {Desk} from "./model/desk/desk.model";

@Injectable({
  providedIn: 'root'
})
export class DeskService {

  constructor(private http: HttpClient, private keycloakService: KeycloakService) {
  }

  private baseUrl = '/api/internal/desk';

  async createDesk(desk: CreateDesk): Promise<Desk> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return firstValueFrom(this.http.post<Desk>(this.baseUrl, desk, {headers}));
  }
}
