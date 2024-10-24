import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {CreateDeskReservation} from "./model/reservation/create-desk-reservation.model";
import {firstValueFrom} from "rxjs";
import {DeskReservation} from "./model/reservation/desk-reservation.model";
import {LocalDate} from "./model/date/local-date.model";
import {ReservationState} from "./model/reservation/reservation-state.enum";
import {DeskReservationListView} from "./model/reservation/desk-reservation-view.model";
import {AuthService} from "./auth.service";
import {requestHeaders} from "./util/header.util";
import {ParkingSpotReservationListView} from "./model/reservation/parking-spot-reservation-view.model";

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

  async confirmReservation(entityType: 'desk' | 'parking', reservationId: string): Promise<void> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.put<void>(`${this.baseUrl}/${entityType}/${reservationId}/confirm`, null, {headers}));
  }

  async rejectReservation(entityType: 'desk' | 'parking', reservationId: string): Promise<void> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.put<void>(`${this.baseUrl}/${entityType}/${reservationId}/reject`, null, {headers}));
  }

  async cancelReservation(entityType: 'desk' | 'parking', reservationId: string): Promise<void> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.put<void>(`${this.baseUrl}/${entityType}/${reservationId}/cancel`, null, {headers}));
  }

  async getParkingSpotReservationListView(
    officeId: string,
    reservationFrom: LocalDate,
    reservationStates: ReservationState[] | null,
    userId: string | null,
    plateNumber: string | null,
    limit: number,
    offset: number
  ): Promise<ParkingSpotReservationListView> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    let url = `${this.baseUrl}/parking/view/list?office_id=${officeId}&reservation_from=${reservationFrom}&limit=${limit}&offset=${offset}`;
    if (reservationStates) url += `&reservation_states=${reservationStates.join(',')}`;
    if (userId) url += `&user_id=${userId}`;
    if (plateNumber) url += `&plate_number=${plateNumber}`;

    return firstValueFrom(this.http.get<ParkingSpotReservationListView>(url, {headers}));
  }
}
