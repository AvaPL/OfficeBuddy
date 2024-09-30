import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {CreateDesk} from "./model/desk/create-desk.model";
import {firstValueFrom} from "rxjs";
import {Desk} from "./model/desk/desk.model";
import {DeskListView} from "./model/desk/desk-view.model";
import {UpdateDesk} from "./model/desk/update-desk.model";
import {ReservableDeskView} from "./model/desk/reservable-desk-view.model";
import {LocalDate} from "./model/date/local-date.model";
import {AuthService} from "./auth.service";
import {requestHeaders} from "./util/header.util";

@Injectable({
  providedIn: 'root'
})
export class DeskService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  private baseUrl = '/api/internal/desk';

  async getDeskListView(officeId: string, limit: number, offset: number): Promise<DeskListView> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    const url = `${this.baseUrl}/view/list?office_id=${officeId}&limit=${limit}&offset=${offset}`;
    return firstValueFrom(this.http.get<DeskListView>(url, {headers}));
  }

  async createDesk(desk: CreateDesk): Promise<Desk> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.post<Desk>(this.baseUrl, desk, {headers}));
  }

  async updateDesk(deskId: string, update: UpdateDesk): Promise<Desk> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.patch<Desk>(`${this.baseUrl}/${deskId}`, update, {headers}));
  }

  async updateAvailability(deskId: string, isAvailable: boolean): Promise<Desk> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    const update: UpdateDesk = {
      isAvailable: isAvailable,
    }

    return firstValueFrom(this.http.patch<Desk>(`${this.baseUrl}/${deskId}`, update, {headers}));
  }

  async archiveDesk(deskId: string): Promise<void> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.delete<void>(`${this.baseUrl}/${deskId}`, {headers}));
  }

  async getReservableDeskViewList(officeId: string, reservationFrom: LocalDate, reservationTo: LocalDate): Promise<ReservableDeskView[]> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    const url = `${this.baseUrl}/view/reservable?office_id=${officeId}&reservation_from=${reservationFrom}&reservation_to=${reservationTo}`;
    return firstValueFrom(this.http.get<ReservableDeskView[]>(url, {headers}));
  }
}
