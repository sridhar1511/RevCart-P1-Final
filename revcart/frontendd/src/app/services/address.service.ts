import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { AuthService } from './auth.service';

export interface Address {
  id?: number;
  fullName: string;
  phone: string;
  line1: string;
  line2?: string;
  city: string;
  state: string;
  pincode: string;
  isDefault?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class AddressService {
  private addressesSubject = new BehaviorSubject<Address[]>([]);
  public addresses$ = this.addressesSubject.asObservable();
  private nextId = 1;

  constructor(private authService: AuthService) {
    this.loadAddresses();
  }

  private getStorageKey(): string {
    const user = this.authService.getCurrentUser();
    const userId = user ? user.id || user.email : 'guest';
    return `revcart_addresses_${userId}`;
  }

  loadAddresses(): void {
    const stored = localStorage.getItem(this.getStorageKey());
    const addresses = stored ? JSON.parse(stored) : [];
    this.addressesSubject.next(addresses);
    this.nextId = addresses.length > 0 ? Math.max(...addresses.map((a: Address) => a.id || 0)) + 1 : 1;
  }

  private saveToStorage(addresses: Address[]): void {
    localStorage.setItem(this.getStorageKey(), JSON.stringify(addresses));
    this.addressesSubject.next(addresses);
  }

  saveAddress(address: Address): Observable<Address> {
    const addresses = [...this.addressesSubject.value];
    
    if (address.id) {
      const index = addresses.findIndex(a => a.id === address.id);
      if (index !== -1) {
        addresses[index] = { ...address };
      }
    } else {
      address.id = this.nextId++;
      if (addresses.length === 0) {
        address.isDefault = true;
      }
      addresses.push({ ...address });
    }
    
    this.saveToStorage(addresses);
    return of({ ...address });
  }

  deleteAddress(id: number): Observable<void> {
    const addresses = this.addressesSubject.value.filter(a => a.id !== id);
    this.saveToStorage(addresses);
    return of(void 0);
  }

  setDefaultAddress(id: number): Observable<void> {
    const addresses = this.addressesSubject.value.map(a => ({
      ...a,
      isDefault: a.id === id
    }));
    this.saveToStorage(addresses);
    return of(void 0);
  }

  getDefaultAddress(): Address | null {
    const addresses = this.addressesSubject.value;
    return addresses.find(addr => addr.isDefault) || addresses[0] || null;
  }
}