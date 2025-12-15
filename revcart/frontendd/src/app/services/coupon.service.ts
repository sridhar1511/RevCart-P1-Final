import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiConfigService } from './api-config.service';

@Injectable({
  providedIn: 'root'
})
export class CouponService {
  private apiUrl: string;

  constructor(private http: HttpClient, private apiConfig: ApiConfigService) {
    this.apiUrl = this.apiConfig.getApiUrl('coupons');
  }

  validateCoupon(code: string, orderAmount: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/validate`, { code, orderAmount });
  }

  getAllCoupons(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  createCoupon(coupon: any): Observable<any> {
    return this.http.post(this.apiUrl, coupon);
  }
}
