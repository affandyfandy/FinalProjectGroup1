import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppConstants } from '../../config/app.constants';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${AppConstants.BASE_API_URL}/authentication`;

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

  login(user: any): Observable<any> {
    const headers = this.getHeaders();
    return this.http.post<any>(this.apiUrl, user, { headers });
  }

  register(user: any): Observable<any> {
    const headers = this.getHeaders();
    return this.http.post<any>(`${this.apiUrl}/register`, user, { headers });
  }

  profile(): Observable<any> {
    const headers = this.getHeadersRestricted();
    return this.http.get<any>(`${this.apiUrl}/profile`, { headers });
  }

  setToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  setRole(role: string): void {
    localStorage.setItem('userRole', role);
  }

  getRole(): string | null {
    return localStorage.getItem('userRole');
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
  }

  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }
}
