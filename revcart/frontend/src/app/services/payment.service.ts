import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { ApiConfigService } from './api-config.service';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private apiUrl: string;

  constructor(private http: HttpClient, private authService: AuthService, private apiConfig: ApiConfigService) {
    this.apiUrl = this.apiConfig.getApiUrl('payments');
  }

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  createPayment(orderId: number, method: string): Observable<any> {
    return this.http.post(`${this.apiUrl}?orderId=${orderId}&method=${method}`, 
      {}, { headers: this.getHeaders() });
  }

  processPayment(paymentId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${paymentId}/process`, 
      {}, { headers: this.getHeaders() });
  }

  failPayment(paymentId: number, reason: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/${paymentId}/fail?reason=${reason}`, 
      {}, { headers: this.getHeaders() });
  }

  refundPayment(paymentId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${paymentId}/refund`, 
      {}, { headers: this.getHeaders() });
  }
}
