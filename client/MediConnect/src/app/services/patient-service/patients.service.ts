import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppConstants } from '../../config/app.constants';
import {
  PatientDTO,
  PatientSaveDTO,
  PatientShowDTO,
} from '../../models/patient.model'; // Assuming you have model interfaces

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
  getPatientsPublic(page: number = 0, size: number = 20): Observable<any> {
    const headers = this.getHeadersRestricted();

    let params = new HttpParams();

    // Conditionally add 'page' and 'size' parameters only if they are provided
    if (page !== undefined && size !== undefined) {
      params = params.set('page', page.toString()).set('size', size.toString());
    }

    return this.http.get<any>(`${this.apiUrl}/public`, { headers, params });
  }

  getPatients(page: number = 0, size: number = 20): Observable<any> {
    const headers = this.getHeadersRestricted();

    let params = new HttpParams();

    // Conditionally add 'page' and 'size' parameters only if they are provided
    if (page !== undefined && size !== undefined) {
      params = params.set('page', page.toString()).set('size', size.toString());
    }

    return this.http.get<any>(this.apiUrl, { headers, params });
  }

  // Get patient by ID (authentication required)
  getPatientById(id: string): Observable<PatientShowDTO> {
    return this.http.get<PatientShowDTO>(`${this.apiUrl}/${id}`, {
      headers: this.getHeadersRestricted(),
    });
  }

  // Create a new patient (authentication required)
  createPatient(patientSaveDTO: PatientSaveDTO): Observable<PatientDTO> {
    return this.http.post<PatientDTO>(this.apiUrl, patientSaveDTO, {
      headers: this.getHeadersRestricted(),
    });
  }

  // Update existing patient by ID (authentication required)
  updatePatient(
    id: string,
    patientSaveDTO: PatientSaveDTO
  ): Observable<PatientDTO> {
    return this.http.put<PatientDTO>(`${this.apiUrl}/${id}`, patientSaveDTO, {
      headers: this.getHeadersRestricted(),
    });
  }

  // Delete patient by ID (authentication required)
  deletePatient(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.getHeadersRestricted(),
    });
  }
}
