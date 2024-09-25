import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {KeycloakService} from "keycloak-angular";
import {CreateDeskReservation} from "./model/reservation/create-desk-reservation.model";
import {firstValueFrom} from "rxjs";
import {DeskReservation} from "./model/reservation/desk-reservation.model";

@Injectable({
    providedIn: 'root'
  })
export class ReservationService {

  constructor(private http: HttpClient, private keycloakService: KeycloakService) {
  }

  private baseUrl = '/api/internal/reservation';

  async createDeskReservation(reservation: CreateDeskReservation): Promise<DeskReservation> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return firstValueFrom(this.http.post<DeskReservation>(`${this.baseUrl}/desk`, reservation, {headers}));
  }
}
