import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { ApiConfigService } from './api-config.service';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl: string;

  constructor(private http: HttpClient, private authService: AuthService, private apiConfig: ApiConfigService) {
    this.apiUrl = this.apiConfig.getApiUrl('orders');
  }

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  createOrder(deliveryAddress: string, phoneNumber: string): Observable<any> {
    const params = new HttpParams()
      .set('deliveryAddress', deliveryAddress)
      .set('phoneNumber', phoneNumber);
    return this.http.post(this.apiUrl, {}, { 
      headers: this.getHeaders(),
      params: params
    });
  }

  createOrderWithItems(deliveryAddress: string, phoneNumber: string, items: any[]): Observable<any> {
    return this.http.post(`${this.apiUrl}/with-items`, {
      deliveryAddress,
      phoneNumber,
      items
    }, { headers: this.getHeaders() });
  }

  getUserOrders(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, { headers: this.getHeaders() });
  }

  getAllOrders(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/admin/all`, { headers: this.getHeaders() });
  }

  getOrderById(orderId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${orderId}`, { headers: this.getHeaders() });
  }

  cancelOrder(orderId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${orderId}`, { headers: this.getHeaders() });
  }

  updateOrderStatus(orderId: number, status: string): Observable<any> {
    const params = new HttpParams().set('status', status);
    return this.http.put(`${this.apiUrl}/${orderId}/status`, {}, { 
      headers: this.getHeaders(),
      params: params
    });
  }
}
