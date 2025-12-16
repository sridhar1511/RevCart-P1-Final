import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { ApiConfigService } from './api-config.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl: string;

  private currentUserSubject = new BehaviorSubject<any>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private showWelcomeSubject = new BehaviorSubject<boolean>(false);
  public showWelcome$ = this.showWelcomeSubject.asObservable();

  constructor(private http: HttpClient, private apiConfig: ApiConfigService) {
    this.apiUrl = this.apiConfig.getApiUrl('auth');
    const user = localStorage.getItem('currentUser');
    if (user) {
      this.currentUserSubject.next(JSON.parse(user));
    }
  }



  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/signin`, { email, password }).pipe(
      tap((response: any) => {
        if (response.token) {
          this.setCurrentUser(response, response.token);
        }
      })
    );
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/signup`, userData);
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
    localStorage.removeItem('cart');
    
    // Clear user-specific data
    const keys = Object.keys(localStorage);
    keys.forEach(key => {
      if (key.startsWith('revcart_addresses_') || key.startsWith('revcart_orders_')) {
        // Don't remove, just let the service handle user-specific loading
      }
    });
    
    this.currentUserSubject.next(null);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getCurrentUser(): any {
    return this.currentUserSubject.value;
  }

  setCurrentUser(user: any, token: string): void {
    localStorage.setItem('currentUser', JSON.stringify(user));
    localStorage.setItem('token', token);
    this.currentUserSubject.next(user);
    this.showWelcomeSubject.next(true);
  }

  hideWelcome(): void {
    this.showWelcomeSubject.next(false);
  }

  isAdmin(): boolean {
    const user = this.getCurrentUser();
    if (!user) return false;
    const role = user.role ? user.role.toString().toUpperCase() : '';
    return role === 'ADMIN' || role.includes('ADMIN');
  }

  getUserRole(): string {
    const user = this.getCurrentUser();
    return user ? user.role : 'USER';
  }

  getFirstName(): string {
    const user = this.getCurrentUser();
    return user ? user.firstName : '';
  }

  getLastName(): string {
    const user = this.getCurrentUser();
    return user ? user.lastName : '';
  }

  getProfilePicture(): string | null {
    const user = this.getCurrentUser();
    return user ? user.profilePicture : null;
  }

  forgotPassword(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/forgot-password?email=${email}`, {});
  }

  resetPassword(token: string, newPassword: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/reset-password`, { token, newPassword });
  }

  sendOtp(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/send-otp?email=${email}`, {});
  }

  verifyOtp(email: string, otp: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/verify-otp?email=${email}&otp=${otp}`, {});
  }

  googleLogin(token: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/oauth2/google`, { token }).pipe(
      tap((response: any) => {
        if (response.token) {
          this.setCurrentUser(response, response.token);
        }
      })
    );
  }

  googleCallback(code: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/google/callback`, { code }).pipe(
      tap((response: any) => {
        if (response.token) {
          this.setCurrentUser(response, response.token);
        }
      })
    );
  }
}
