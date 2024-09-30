import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {CreateDeskReservation} from "./model/reservation/create-desk-reservation.model";
import {firstValueFrom} from "rxjs";
import {DeskReservation} from "./model/reservation/desk-reservation.model";
import {LocalDate} from "./model/date/local-date.model";
import {ReservationState} from "./model/reservation/reservation-state.enum";
import {DeskReservationListView} from "./model/reservation/reservation-view.model";
import {AuthService} from "./auth.service";
import {requestHeaders} from "./util/header.util";

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  private baseUrl = '/api/internal/reservation';

  async getDeskReservationListView(
    officeId: string,
    reservationFrom: LocalDate,
    reservationStates: ReservationState[] | null,
    userId: string | null,
    limit: number,
    offset: number
  ): Promise<DeskReservationListView> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    let url = `${this.baseUrl}/desk/view/list?office_id=${officeId}&reservation_from=${reservationFrom}&limit=${limit}&offset=${offset}`;
    if (reservationStates) url += `&reservation_states=${reservationStates.join(',')}`;
    if (userId) url += `&user_id=${userId}`;

    return firstValueFrom(this.http.get<DeskReservationListView>(url, {headers}));
  }

  async createDeskReservation(reservation: CreateDeskReservation): Promise<DeskReservation> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.post<DeskReservation>(`${this.baseUrl}/desk`, reservation, {headers}));
  }

  async confirmDeskReservation(reservationId: string): Promise<void> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.put<void>(`${this.baseUrl}/${reservationId}/confirm`, null, {headers}));
  }

  async rejectDeskReservation(reservationId: string): Promise<void> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.put<void>(`${this.baseUrl}/${reservationId}/reject`, null, {headers}));
  }

  async cancelDeskReservation(reservationId: string): Promise<void> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.put<void>(`${this.baseUrl}/${reservationId}/cancel`, null, {headers}));
  }
}
