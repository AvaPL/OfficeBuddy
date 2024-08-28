import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {KeycloakService} from 'keycloak-angular';
import {firstValueFrom} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OfficeService {

  constructor(private http: HttpClient, private keycloakService: KeycloakService) {
  }

  private baseUrl = '/api/internal/office';

  // TODO: Use concrete types instead of any
  async createOffice(officeData: any): Promise<any> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    // TODO: Extract /api/internal as common base URL
    return firstValueFrom(this.http.post(this.baseUrl, {...officeData, notes: []}, {headers}));
  }

  // TODO: Use concrete types instead of any
  async updateOffice(officeId: string, officeData: any): Promise<any> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    // TODO: Backend expects all properties to be present, has to be fixed
    return firstValueFrom(this.http.patch(`${this.baseUrl}/${officeId}`, officeData, {headers}));
  }

  // TODO: Use concrete types instead of any
  async archiveOffice(officeId: string): Promise<any> {
    const token = await this.keycloakService.getToken();
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return firstValueFrom(this.http.delete(`${this.baseUrl}/${officeId}`, {headers}));
  }
}
