import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiConfigService } from './api-config.service';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {
  private apiUrl: string;

  constructor(private http: HttpClient, private apiConfig: ApiConfigService) {
    this.apiUrl = this.apiConfig.getApiUrl('analytics');
  }

  getDashboardStats(): Observable<any> {
    return this.http.get(`${this.apiUrl}/dashboard`);
  }

  getSalesAnalytics(): Observable<any> {
    return this.http.get(`${this.apiUrl}/sales`);
  }

  getUserAnalytics(): Observable<any> {
    return this.http.get(`${this.apiUrl}/users`);
  }
}
