import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppConstants } from '../../config/app.constants';
import { DoctorScheduleDTO } from '../../models/doctor-schedule.model';
import { ScheduleTimeDTO } from '../../models/doctor-schedule.model';

@Injectable({
  providedIn: 'root',
})
export class DoctorSchedulesService {
  private apiUrl = `${AppConstants.BASE_API_URL}/schedules`;

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

  // Get all schedules
  getSchedules(): Observable<DoctorScheduleDTO[]> {
    return this.http.get<any>(this.apiUrl + "/list", {
      headers: this.getHeadersRestricted(),
    });
  }

  // Get schedule by id
  getScheduleById(id: string): Observable<DoctorScheduleDTO> {
    return this.http.get<DoctorScheduleDTO>(`${this.apiUrl}/${id}`, {
      headers: this.getHeadersRestricted(),
    });
  }

  // Create a new schedule
  createSchedule(doctorScheduleDTO: DoctorScheduleDTO): Observable<DoctorScheduleDTO> {
    return this.http.post<DoctorScheduleDTO>(this.apiUrl, doctorScheduleDTO, {
      headers: this.getHeadersRestricted(),
    });
  }

  // Update existing schedule by id
  updateSchedule(
    id: string,
    doctorScheduleDTO: DoctorScheduleDTO
  ): Observable<DoctorScheduleDTO> {
    return this.http.put<DoctorScheduleDTO>(`${this.apiUrl}/${id}`, doctorScheduleDTO, {
      headers: this.getHeadersRestricted(),
    });
  }

  // Delete schedule by id
  deleteSchedule(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.getHeadersRestricted(),
    });
  }
}
