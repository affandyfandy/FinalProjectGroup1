import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppConstants } from '../../config/app.constants';
import { DoctorDTO } from '../../models/doctor.model';

@Injectable({
  providedIn: 'root',
})
export class DoctorsService {
  private apiUrl = `${AppConstants.BASE_API_URL}/doctors`;

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

  // Get all doctors
  getDoctors(): Observable<DoctorDTO[]> {
    return this.http.get<any>(this.apiUrl + "/list", {
      headers: this.getHeadersRestricted(),
    });
  }

  // Get doctor by id
  getDoctorById(id: string): Observable<DoctorDTO> {
    return this.http.get<DoctorDTO>(`${this.apiUrl}/${id}`, {
      headers: this.getHeadersRestricted(),
    });
  }

  // Create a new doctor
  createDoctor(doctorDTO: DoctorDTO): Observable<DoctorDTO> {
    return this.http.post<DoctorDTO>(this.apiUrl, doctorDTO, {
      headers: this.getHeadersRestricted(),
    });
  }

  // Update existing doctor by id
  updateDoctor(
    id: string,
    doctorDTO: DoctorDTO
  ): Observable<DoctorDTO> {
    return this.http.put<DoctorDTO>(`${this.apiUrl}/${id}`, doctorDTO, {
      headers: this.getHeadersRestricted(),
    });
  }

  // Delete doctor by id
  deleteDoctor(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.getHeadersRestricted(),
    });
  }
}
