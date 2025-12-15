import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { ApiConfigService } from './api-config.service';

@Injectable({
  providedIn: 'root'
})
export class RecommendationService {
  private apiUrl: string;

  constructor(private http: HttpClient, private authService: AuthService, private apiConfig: ApiConfigService) {
    this.apiUrl = this.apiConfig.getApiUrl('recommendations');
  }

  getRecommendationsForUser(): Observable<any> {
    return this.http.get(`${this.apiUrl}/for-user`);
  }

  getPopularProducts(): Observable<any> {
    return this.http.get(`${this.apiUrl}/popular`);
  }

  getSimilarProducts(productId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/similar/${productId}`);
  }
}
