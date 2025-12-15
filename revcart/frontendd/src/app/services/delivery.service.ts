import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiConfigService } from './api-config.service';

@Injectable({
  providedIn: 'root'
})
export class DeliveryService {
  private apiUrl: string;

  constructor(private http: HttpClient, private apiConfig: ApiConfigService) {
    this.apiUrl = this.apiConfig.getApiUrl('delivery');
  }

  agentRegister(email: string, password: string, name: string, phone: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/agent/register?email=${email}&password=${password}&name=${name}&phone=${phone}`, {});
  }

  agentLogin(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/agent/login?email=${email}&password=${password}`, {});
  }

  getAvailableAgents(): Observable<any> {
    return this.http.get(`${this.apiUrl}/available-agents`);
  }

  assignDeliveryAgent(orderId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/assign/${orderId}`, {});
  }

  updateDeliveryStatus(orderId: number, status: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/status/${orderId}?status=${status}`, {});
  }

  updateAgentStatus(agentId: number, status: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/agent/${agentId}/status?status=${status}`, {});
  }

  getAgent(agentId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/agent/${agentId}`);
  }

  updateDeliveryLocation(latitude: number, longitude: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/location/update?latitude=${latitude}&longitude=${longitude}`, {});
  }

  getDeliveryLocation(orderId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/location/${orderId}`);
  }

  getDeliveryOrders(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/orders`);
  }

  verifyAgentOtp(email: string, otp: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/agent/verify-otp?email=${email}&otp=${otp}`, {});
  }

  resendAgentOtp(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/agent/resend-otp?email=${email}`, {});
  }
}
