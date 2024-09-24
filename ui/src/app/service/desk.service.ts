import {Injectable} from "@angular/core";
import {KeycloakService} from "keycloak-angular";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CreateDesk} from "./model/desk/create-desk.model";
import {firstValueFrom} from "rxjs";
import {Desk} from "./model/desk/desk.model";
import {DeskListView} from "./model/desk/desk-view.model";
import {UpdateDesk} from "./model/desk/update-desk.model";
import {ReservableDeskView} from "./model/desk/reservable-desk-view.model";

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
      isAvailable: isAvailable,
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

  async getReservableDeskViewList(officeId: string, reservationFrom: Date, reservationTo: Date): Promise<ReservableDeskView[]> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    const reservationFromString = this.formatYYYYMMDD(reservationFrom)
    const reservationToString = this.formatYYYYMMDD(reservationTo)

    const url = `${this.baseUrl}/view/reservable?office_id=${officeId}&reservation_from=${reservationFromString}&reservation_to=${reservationToString}`;
    return firstValueFrom(this.http.get<ReservableDeskView[]>(url, {headers}));
  }

  // Removes time, including time zone, WITHOUT conversion to UTC
  formatYYYYMMDD(date: Date): string {
    return [
      date.getFullYear(),
      ('0' + (date.getMonth() + 1)).slice(-2),
      ('0' + date.getDate()).slice(-2)
    ].join('-');
  }
}
