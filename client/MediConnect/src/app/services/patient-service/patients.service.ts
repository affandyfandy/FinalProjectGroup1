import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppConstants } from '../../config/app.constants';

@Injectable({
  providedIn: 'root',
})
export class PatientsService {
  private apiUrl = `${AppConstants.BASE_API_URL}/all/patients`;

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
    });
  }

  private getHeadersRestricted(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    });
  }

  // Get products by criteria with pagination
  getPatients(page: number = 0, size: number = 20): Observable<any> {
    const headers = this.getHeadersRestricted();

    let params = new HttpParams();

    // Conditionally add 'page' and 'size' parameters only if they are provided
    if (page !== undefined && size !== undefined) {
      params = params.set('page', page.toString()).set('size', size.toString());
    }

    return this.http.get<any>(`${this.apiUrl}/public`, { headers, params });
  }

  getPatientsRestricted(page: number = 0, size: number = 20): Observable<any> {
    const headers = this.getHeadersRestricted();

    let params = new HttpParams();

    // Conditionally add 'page' and 'size' parameters only if they are provided
    if (page !== undefined && size !== undefined) {
      params = params.set('page', page.toString()).set('size', size.toString());
    }

    return this.http.get<any>(this.apiUrl, { headers, params });
  }
}
