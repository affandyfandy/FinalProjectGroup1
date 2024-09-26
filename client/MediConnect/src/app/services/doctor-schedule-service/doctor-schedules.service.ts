import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppConstants } from '../../config/app.constants';
import {
  DoctorScheduleDTO,
  DoctorScheduleList,
  ListDoctorSchedule,
} from '../../models/doctor-schedule.model';
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
    return this.http.get<any>(this.apiUrl + '/list', {
      headers: this.getHeadersRestricted(),
    });
  }

  // Get all schedules
  getSchedulesDoctor(): Observable<PaginatedResponse<DoctorScheduleList>> {
    return this.http.get<PaginatedResponse<DoctorScheduleList>>(this.apiUrl + '/doctor/list', {
      headers: this.getHeadersRestricted(),
    });
  }

  // Get all schedules
  getSchedulesDoctorByDay(id: string, day: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/doctor/${id}/day/${day}`, {
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
  createSchedule(
    doctorScheduleDTO: DoctorScheduleDTO
  ): Observable<DoctorScheduleDTO> {
    return this.http.post<DoctorScheduleDTO>(this.apiUrl, doctorScheduleDTO, {
      headers: this.getHeadersRestricted(),
    });
  }

  // Update existing schedule by id
  updateSchedule(
    id: string,
    doctorScheduleDTO: DoctorScheduleDTO
  ): Observable<DoctorScheduleDTO> {
    return this.http.put<DoctorScheduleDTO>(
      `${this.apiUrl}/${id}`,
      doctorScheduleDTO,
      {
        headers: this.getHeadersRestricted(),
      }
    );
  }

  // Fetch schedule by doctorId and startWorkingHour
  getScheduleTime(
    id: string,
    startWorkingHour: string
  ): Observable<ScheduleTimeDTO> {
    return this.http.get<ScheduleTimeDTO>(
      `${this.apiUrl}/${id}/${startWorkingHour}`,
      {
        headers: this.getHeadersRestricted(),
      }
    );
  }

  // Update schedule time by id and startWorkingHour
  updateScheduleTime(
    id: string,
    startWorkingHour: string,
    scheduleTimeDTO: ScheduleTimeDTO
  ): Observable<ScheduleTimeDTO> {
    return this.http.put<ScheduleTimeDTO>(
      `${this.apiUrl}/${id}/${startWorkingHour}`,
      scheduleTimeDTO,
      {
        headers: this.getHeadersRestricted(),
      }
    );
  }

  // Delete schedule by id
  deleteSchedule(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.getHeadersRestricted(),
    });
  }

  // Delete schedule time
  deleteScheduleTime(id: string, startWorkingHour: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}/${startWorkingHour}`, {
      headers: this.getHeadersRestricted(),
    });
  }

  // New method: Get schedules with filters (doctorName, date, specialization)
  getFilteredSchedules(
    doctorName?: string,
    date?: string,
    specialization?: string
  ): Observable<DoctorScheduleList[]> {
    let params = new HttpParams();

    if (doctorName) {
      params = params.set('doctorName', doctorName);
    }

    if (date) {
      params = params.set('date', date);
    }

    if (specialization) {
      params = params.set('specialization', specialization);
    }

    return this.http.get<DoctorScheduleList[]>(
      `${this.apiUrl}/doctor/list/filter`,
      {
        headers: this.getHeadersRestricted(),
        params: params,
      }
    );
  }

}

export interface PaginatedResponse<T> {
  content: T[];
  pageable: any;
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: any;
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}
