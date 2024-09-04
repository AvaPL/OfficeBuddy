import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {KeycloakService} from 'keycloak-angular';
import {firstValueFrom} from 'rxjs';
import {OfficeListView} from "./model/office/office-view.model";
import {CreateOffice} from "./model/office/create-office.model";
import {UpdateOffice} from "./model/office/update-office.model";
import {Office} from "./model/office/office.model";

@Injectable({
  providedIn: 'root'
})
export class OfficeService {

  constructor(private http: HttpClient, private keycloakService: KeycloakService) {
  }

  private baseUrl = '/api/internal/office';

  async getOfficeListView(limit: number, offset: number): Promise<OfficeListView> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return firstValueFrom(this.http.get<OfficeListView>(`${this.baseUrl}/view/list?limit=${limit}&offset=${offset}`, {headers}));
  }

  async createOffice(office: CreateOffice): Promise<Office> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return firstValueFrom(this.http.post<Office>(this.baseUrl, office, {headers}));
  }

  async updateOffice(officeId: string, update: UpdateOffice): Promise<Office> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return firstValueFrom(this.http.patch<Office>(`${this.baseUrl}/${officeId}`, update, {headers}));
  }

  async archiveOffice(officeId: string): Promise<void> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return firstValueFrom(this.http.delete<void>(`${this.baseUrl}/${officeId}`, {headers}));
  }
}
