import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {firstValueFrom} from 'rxjs';
import {OfficeListView} from "./model/office/office-view.model";
import {CreateOffice} from "./model/office/create-office.model";
import {UpdateOffice} from "./model/office/update-office.model";
import {Office} from "./model/office/office.model";
import {OfficeCompact} from "./model/office/office-compact.model";
import {AuthService} from "./auth.service";
import {requestHeaders} from "./util/header.util";

@Injectable({
  providedIn: 'root'
})
export class OfficeService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  private baseUrl = '/api/internal/office';

  async getOfficeListView(limit: number, offset: number): Promise<OfficeListView> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token)

    return firstValueFrom(this.http.get<OfficeListView>(`${this.baseUrl}/view/list?limit=${limit}&offset=${offset}`, {headers}));
  }

  async getCompactOffices(): Promise<OfficeCompact[]> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token)

    // TODO: Introduce a dedicated endpoint that'll return only the id and name of the offices
    // return firstValueFrom(this.http.get<OfficeCompact[]>(`${this.baseUrl}/view/compact`, {headers}));

    const officeListView = await firstValueFrom(
      this.http.get<OfficeListView>(`${this.baseUrl}/view/list?limit=1000&offset=0`, {headers})
    );
    return this.officeListViewToCompactOffices(officeListView);
  }

  officeListViewToCompactOffices(officeListView: OfficeListView): OfficeCompact[] {
    return officeListView.offices.map(office => {
      return {
        id: office.id,
        name: office.name
      };
    });
  }

  async createOffice(office: CreateOffice): Promise<Office> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token)

    return firstValueFrom(this.http.post<Office>(this.baseUrl, office, {headers}));
  }

  async updateOffice(officeId: string, update: UpdateOffice): Promise<Office> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token)

    return firstValueFrom(this.http.patch<Office>(`${this.baseUrl}/${officeId}`, update, {headers}));
  }

  async updateOfficeManagers(officeId: string, officeManagerIds: string[]): Promise<Office> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token)

    return firstValueFrom(this.http.put<Office>(`${this.baseUrl}/${officeId}/office-manager-ids`, officeManagerIds, {headers}));
  }

  async archiveOffice(officeId: string): Promise<void> {
    const token = await this.authService.getToken();
    const headers = requestHeaders(token)

    return firstValueFrom(this.http.delete<void>(`${this.baseUrl}/${officeId}`, {headers}));
  }
}
