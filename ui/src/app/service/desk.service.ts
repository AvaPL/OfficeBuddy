import {Injectable} from "@angular/core";
import {KeycloakService} from "keycloak-angular";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CreateDesk} from "./model/desk/create-desk.model";
import {firstValueFrom} from "rxjs";
import {Desk} from "./model/desk/desk.model";
import {DeskListView} from "./model/desk/desk-view.model";
import {UpdateDesk} from "./model/desk/update-desk.model";

@Injectable({
  providedIn: 'root'
})
export class DeskService {

  constructor(private http: HttpClient, private keycloakService: KeycloakService) {
  }

  private baseUrl = '/api/internal/desk';

  async getDeskListView(officeId: string, limit: number, offset: number): Promise<DeskListView> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    const url = `${this.baseUrl}/view/list?office_id=${officeId}&limit=${limit}&offset=${offset}`;
    return firstValueFrom(this.http.get<DeskListView>(url, {headers}));
  }

  async createDesk(desk: CreateDesk): Promise<Desk> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return firstValueFrom(this.http.post<Desk>(this.baseUrl, desk, {headers}));
  }

  async updateDesk(deskId: string, update: UpdateDesk): Promise<Desk> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return firstValueFrom(this.http.patch<Desk>(`${this.baseUrl}/${deskId}`, update, {headers}));
  }

  async updateAvailability(deskId: string, isAvailable: boolean): Promise<Desk> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    const update: UpdateDesk = {
      name: null,
      isAvailable: isAvailable,
      notes: null,
      isStanding: null,
      monitorsCount: null,
      hasPhone: null
    }

    return firstValueFrom(this.http.patch<Desk>(`${this.baseUrl}/${deskId}`, update, {headers}));
  }

  async archiveDesk(deskId: string): Promise<void> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return firstValueFrom(this.http.delete<void>(`${this.baseUrl}/${deskId}`, {headers}));
  }
}
