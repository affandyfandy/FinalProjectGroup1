import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpHeaders,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { AppConstants } from '../../config/app.constants';
import { Router } from '@angular/router';
import { HttpParams } from '@angular/common/http';
import { AppointmentSaveDTO } from '../../models/appointment.model';
import { AppointmentShowDTO } from '../../models/appointment.model';

@Injectable({
  providedIn: 'root',
})
export class AppointmentService {
  private apiUrl = `${AppConstants.BASE_API_URL}/appointments`;

  constructor(private http: HttpClient, private router: Router) {}

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

  // Get all appointments
  getAppointments(): Observable<AppointmentShowDTO[]> {
    return this.http.get<any>(this.apiUrl + "/list", {
      headers: this.getHeadersRestricted(),
    });
  }

  // Get appointment by ID
  getAppointmentByPatientId(id: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/patient/${id}`, {
      headers: this.getHeadersRestricted(),
    });
  }

  // Create a new appointment
  createAppointment(appointmentSave: AppointmentSaveDTO): Observable<any> {
    return this.http.post<any>(this.apiUrl, appointmentSave, {
      headers: this.getHeadersRestricted(),
    });
  }

  // Delete appointment by id
  deleteAppointment(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.getHeadersRestricted(),
    });
  }
}
