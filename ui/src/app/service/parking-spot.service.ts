import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {firstValueFrom} from "rxjs";
import {AuthService} from "./auth.service";
import {requestHeaders} from "./util/header.util";
import {ParkingSpotListView, ParkingSpotView} from "./model/parking/parking-spot-view.model";
import {CreateParkingSpot} from "./model/parking/create-parking-spot.model";
import {UpdateParkingSpot} from "./model/parking/update-parking-spot.model";
import {Pagination} from "./model/pagination/pagination.model";
import {LocalDate} from "./model/date/local-date.model";
import {ParkingSpot} from "./model/parking/parking-spot.model";

@Injectable({
  providedIn: 'root'
})
export class ParkingSpotService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  private baseUrl = '/api/internal/parking';

  async getParkingSpotListView(officeId: string, limit: number, offset: number): Promise<ParkingSpotListView> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    const url = `${this.baseUrl}/view/list?office_id=${officeId}&limit=${limit}&offset=${offset}`;
    return firstValueFrom(this.http.get<ParkingSpotListView>(url, {headers}));
  }

  async createParkingSpot(parkingSpot: CreateParkingSpot): Promise<ParkingSpot> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.post<ParkingSpot>(this.baseUrl, parkingSpot, {headers}));
  }

  // async updateParkingSpot(parkingSpotId: string, update: UpdateParkingSpot): Promise<ParkingSpot> {
  //   const token = await this.authService.getToken();
  //   const headers = requestHeaders(token);
  //
  //   return firstValueFrom(this.http.patch<ParkingSpot>(`${this.baseUrl}/${parkingSpotId}`, update, {headers}));
  // }

  async updateAvailability(parkingSpotId: string, isAvailable: boolean): Promise<ParkingSpot> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    const update: UpdateParkingSpot = {
      isAvailable: isAvailable,
    }

    return firstValueFrom(this.http.patch<ParkingSpot>(`${this.baseUrl}/${parkingSpotId}`, update, {headers}));
  }

  async archiveParkingSpot(parkingSpotId: string): Promise<void> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token);

    return firstValueFrom(this.http.delete<void>(`${this.baseUrl}/${parkingSpotId}`, {headers}));
  }

  // async getReservableParkingSpotListView(officeId: string, reservationFrom: LocalDate, reservationTo: LocalDate): Promise<ReservableParkingSpotView[]> {
  //   const token = await this.authService.getToken();
  //   const headers = requestHeaders(token);
  //
  //   const url = `${this.baseUrl}/view/reservable/list?office_id=${officeId}&reservation_from=${reservationFrom}&reservation_to=${reservationTo}`;
  //   return firstValueFrom(this.http.get<ReservableParkingSpotView[]>(url, {headers}));
  // }
}
