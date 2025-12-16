import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiConfigService } from './api-config.service';

@Injectable({
  providedIn: 'root'
})
export class RazorpayService {
  private apiUrl: string;

  constructor(private http: HttpClient, private apiConfig: ApiConfigService) {
    this.apiUrl = this.apiConfig.getApiUrl('razorpay');
  }

  createOrder(orderId: number, amount: number, currency: string = 'INR'): Observable<any> {
    return this.http.post(`${this.apiUrl}/create-order`, null, {
      params: { orderId, amount, currency }
    });
  }

  verifyPayment(razorpayOrderId: string, razorpayPaymentId: string, razorpaySignature: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/verify-payment`, null, {
      params: { razorpayOrderId, razorpayPaymentId, razorpaySignature }
    });
  }

  capturePayment(razorpayPaymentId: string, amount: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/capture-payment`, null, {
      params: { razorpayPaymentId, amount }
    });
  }

  refundPayment(razorpayPaymentId: string, amount: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/refund-payment`, null, {
      params: { razorpayPaymentId, amount }
    });
  }

  loadRazorpayScript(): Promise<boolean> {
    return new Promise((resolve) => {
      const script = document.createElement('script');
      script.src = 'https://checkout.razorpay.com/v1/checkout.js';
      script.async = true;
      script.onload = () => resolve(true);
      script.onerror = () => resolve(false);
      document.body.appendChild(script);
    });
  }

  openRazorpayCheckout(options: any): Promise<any> {
    return new Promise((resolve, reject) => {
      const razorpay = (window as any).Razorpay;
      if (!razorpay) {
        reject('Razorpay script not loaded');
        return;
      }

      const checkout = new razorpay(options);
      checkout.open();
      
      checkout.on('payment.failed', (response: any) => {
        reject(response.error);
      });
    });
  }
}
