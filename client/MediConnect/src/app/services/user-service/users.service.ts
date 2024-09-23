import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AppConstants } from '../../config/app.constants';
import { User, UserSaveDTO, UserShowDTO } from '../../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = `${AppConstants.BASE_API_URL}/users`;

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

  // Get users publicly accessible, excluding patients
  getUsersPublic(page: number = 0, size: number = 20): Observable<any> {
    const headers = this.getHeaders();
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(this.apiUrl, { headers, params }).pipe(
      map((response) => {
        // Ensure response.content is an array of User objects
        response.content = response.content.filter(
          (user: User) => user.role !== 'PATIENT'
        );
        return response;
      })
    );
  }

  // Get users restricted (SUPERADMIN only), excluding patients
  getUsers(page: number = 0, size: number = 20): Observable<any> {
    const headers = this.getHeadersRestricted();
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(this.apiUrl, { headers, params }).pipe(
      map((response) => {
        // Ensure response.content is an array of User objects
        response.content = response.content.filter(
          (user: User) => user.role !== 'PATIENT'
        );
        return response;
      })
    );
  }

  // Get user by ID (SUPERADMIN only)
  getUserById(id: string): Observable<UserShowDTO> {
    const headers = this.getHeadersRestricted();
    return this.http.get<UserShowDTO>(`${this.apiUrl}/${id}`, { headers });
  }

  // Create a new user (SUPERADMIN only)
  createUser(userSaveDTO: UserSaveDTO): Observable<User> {
    const headers = this.getHeadersRestricted();
    return this.http.post<User>(this.apiUrl, userSaveDTO, { headers });
  }

  // Update an existing user (SUPERADMIN only)
  updateUserPatient(id: string, userSaveDTO: UserSaveDTO): Observable<User> {
    const headers = this.getHeadersRestricted();
    return this.http.put<User>(`${this.apiUrl}/patient/${id}`, userSaveDTO, {
      headers,
    });
  }

  // Update an existing user (SUPERADMIN only)
  updateUser(id: string, userSaveDTO: UserSaveDTO): Observable<User> {
    const headers = this.getHeadersRestricted();
    return this.http.put<User>(`${this.apiUrl}/${id}`, userSaveDTO, {
      headers,
    });
  }

  // Delete a user by ID (SUPERADMIN only)
  deleteUser(id: string): Observable<void> {
    const headers = this.getHeadersRestricted();
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers });
  }
}
