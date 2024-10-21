import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {firstValueFrom} from "rxjs";
import {AuthService} from "./auth.service";
import {requestHeaders} from "./util/header.util";
import {ParkingSpotView} from "./model/parking/parking-spot-view.model";
import {CreateParkingSpot} from "./model/parking/create-parking-spot.model";
import {UpdateParkingSpot} from "./model/parking/update-parking-spot.model";
import {Pagination} from "./model/pagination/pagination.model";
import {LocalDate} from "./model/date/local-date.model";

@Injectable({
  providedIn: 'root'
})
export class ParkingSpotService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  private baseUrl = '/api/internal/parking';

  async getParkingSpotListView(officeId: string, limit: number, offset: number): Promise<{
    parkingSpots: ParkingSpotView[],
    pagination: Pagination
  }> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    const url = `${this.baseUrl}/view/list?office_id=${officeId}&limit=${limit}&offset=${offset}`;
    return firstValueFrom(this.http.get<{ parkingSpots: ParkingSpotView[], pagination: Pagination }>(url, {headers}));
  }

  async createParkingSpot(parkingSpot: CreateParkingSpot): Promise<ParkingSpotView> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.post<ParkingSpotView>(this.baseUrl, parkingSpot, {headers}));
  }

  async updateParkingSpot(parkingSpotId: string, update: UpdateParkingSpot): Promise<ParkingSpotView> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.patch<ParkingSpotView>(`${this.baseUrl}/${parkingSpotId}`, update, {headers}));
  }

  async updateAvailability(parkingSpotId: string, isAvailable: boolean): Promise<ParkingSpotView> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    const update: UpdateParkingSpot = {
      isAvailable: isAvailable,
    }

    return firstValueFrom(this.http.patch<ParkingSpotView>(`${this.baseUrl}/${parkingSpotId}`, update, {headers}));
  }

  async archiveParkingSpot(parkingSpotId: string): Promise<void> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.delete<void>(`${this.baseUrl}/${parkingSpotId}`, {headers}));
  }

  async getReservableParkingSpotListView(officeId: string, reservationFrom: LocalDate, reservationTo: LocalDate): Promise<{
    parkingSpots: ParkingSpotView[],
    pagination: Pagination
  }> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    const url = `${this.baseUrl}/view/reservable/list?office_id=${officeId}&reservation_from=${reservationFrom}&reservation_to=${reservationTo}`;
    return firstValueFrom(this.http.get<{ parkingSpots: ParkingSpotView[], pagination: Pagination }>(url, {headers}));
  }
}
