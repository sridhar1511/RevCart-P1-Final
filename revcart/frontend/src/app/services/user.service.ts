import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { ApiConfigService } from './api-config.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl: string;

  constructor(private http: HttpClient, private authService: AuthService, private apiConfig: ApiConfigService) {
    this.apiUrl = this.apiConfig.getApiUrl('user');
  }

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getUserProfile(): Observable<any> {
    return this.http.get(`${this.apiUrl}/profile`, { headers: this.getHeaders() });
  }

  updateUserProfile(profileData: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/profile`, profileData, { headers: this.getHeaders() });
  }

  changePassword(passwordData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/change-password`, passwordData, { headers: this.getHeaders() });
  }

  getUserAddresses(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/addresses`, { headers: this.getHeaders() });
  }

  addAddress(addressData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/addresses`, addressData, { headers: this.getHeaders() });
  }

  updateAddress(addressId: number, addressData: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/addresses/${addressId}`, addressData, { headers: this.getHeaders() });
  }

  deleteAddress(addressId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/addresses/${addressId}`, { headers: this.getHeaders() });
  }
}
