import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiConfigService } from './api-config.service';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private apiUrl: string;

  constructor(private http: HttpClient, private apiConfig: ApiConfigService) {
    this.apiUrl = this.apiConfig.getApiUrl('search');
  }

  search(query: string): Observable<any> {
    return this.http.get(`${this.apiUrl}?query=${query}`);
  }

  advancedSearch(query?: string, minPrice?: number, maxPrice?: number, category?: string): Observable<any> {
    let url = `${this.apiUrl}/filter?`;
    if (query) url += `query=${query}&`;
    if (minPrice) url += `minPrice=${minPrice}&`;
    if (maxPrice) url += `maxPrice=${maxPrice}&`;
    if (category) url += `category=${category}&`;
    return this.http.get(url);
  }

  getTrendingProducts(): Observable<any> {
    return this.http.get(`${this.apiUrl}/trending`);
  }

  getRecommendations(category: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/recommendations?category=${category}`);
  }

  getByCategory(category: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/category/${category}`);
  }
}
