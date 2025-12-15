import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { ApiConfigService } from './api-config.service';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private apiUrl: string;
  private cartItems = new BehaviorSubject<any[]>([]);
  public cartItems$ = this.cartItems.asObservable();

  constructor(private http: HttpClient, private authService: AuthService, private apiConfig: ApiConfigService) {
    this.apiUrl = this.apiConfig.getApiUrl('cart');
    // Only load cart from localStorage for offline support
    const savedCart = localStorage.getItem('cart');
    if (savedCart) {
      try {
        const parsed = JSON.parse(savedCart);
        this.cartItems.next([...parsed]);
      } catch (e) {
        localStorage.removeItem('cart');
        this.cartItems.next([]);
      }
    }
  }

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getCart(): Observable<any> {
    if (this.authService.isAuthenticated()) {
      return this.http.get(this.apiUrl, { headers: this.getHeaders() });
    }
    return new Observable(observer => {
      observer.next({ cartItems: this.cartItems.value });
    });
  }

  addToCart(product: any, quantity: number = 1): void {
    if (this.authService.isAdmin()) {
      console.warn('Admin users cannot add items to cart');
      return;
    }
    
    // Always add to local cart first for immediate UI feedback
    this.addToLocalCart(product, quantity);
    
    // If authenticated, also add to server
    if (this.authService.isAuthenticated()) {
      const cartRequest = { productId: product.id, quantity };
      this.http.post(`${this.apiUrl}/add`, cartRequest, { headers: this.getHeaders() }).subscribe({
        next: (response) => {
          // Server add successful, no need to reload
        },
        error: (error) => {
          console.error('Error adding to server cart:', error);
        }
      });
    }
  }

  private addToLocalCart(product: any, quantity: number): void {
    const currentItems = [...this.cartItems.value];
    const existingItem = currentItems.find(item => item.id === product.id && item.size === product.size);

    if (existingItem) {
      existingItem.quantity += quantity;
    } else {
      currentItems.push({ ...product, quantity });
    }

    this.updateLocalCart(currentItems);
    this.showAddToCartNotification(product.name, quantity);
  }

  private loadCartFromServer(): void {
    this.http.get(`${this.apiUrl}`, { headers: this.getHeaders() }).subscribe({
      next: (response: any) => {
        this.cartItems.next(response.cartItems || []);
      },
      error: (error) => {
        console.error('Error loading cart:', error);
      }
    });
  }

  updateCartItem(productId: number, quantity: number): Observable<any> | void {
    if (this.authService.isAuthenticated()) {
      return this.http.put(`${this.apiUrl}/update`, 
        { productId, quantity }, 
        { headers: this.getHeaders() }
      );
    } else {
      const currentItems = this.cartItems.value;
      const item = currentItems.find(item => item.id === productId);
      
      if (item) {
        if (quantity <= 0) {
          this.removeFromCart(productId);
        } else {
          item.quantity = quantity;
          this.updateLocalCart(currentItems);
        }
      }
    }
  }

  removeFromCart(productId: number, size?: string): void {
    // Always remove from local cart first for immediate UI feedback
    this.removeFromLocalCart(productId, size);
    
    // If authenticated, also remove from server
    if (this.authService.isAuthenticated()) {
      this.http.delete(`${this.apiUrl}/remove/${productId}`, { headers: this.getHeaders() }).subscribe({
        next: () => {
          // Server removal successful
        },
        error: (error) => {
          console.error('Error removing from server cart:', error);
          // Item already removed from local cart, so no need to revert
        }
      });
    }
  }

  private removeFromLocalCart(productId: number, size?: string): void {
    const currentItems = [...this.cartItems.value].filter(item => {
      if (size) {
        return !(item.id === productId && item.size === size);
      }
      return item.id !== productId;
    });
    this.updateLocalCart(currentItems);
  }

  clearCart(): void {
    if (this.authService.isAuthenticated()) {
      this.http.delete(`${this.apiUrl}/clear`, 
        { headers: this.getHeaders() }
      ).subscribe({
        next: () => {
          this.updateLocalCart([]);
        },
        error: (error) => {
          console.error('Error clearing cart:', error);
          this.updateLocalCart([]);
        }
      });
    } else {
      this.updateLocalCart([]);
    }
  }

  getCartTotal(): number {
    return this.cartItems.value.reduce((total, item) => total + (item.price * item.quantity), 0);
  }

  getCartItemCount(): number {
    return this.cartItems.value.reduce((count, item) => count + item.quantity, 0);
  }

  private updateLocalCart(items: any[]): void {
    const newItems = [...items];
    this.cartItems.next(newItems);
    if (newItems.length === 0) {
      localStorage.removeItem('cart');
    } else {
      localStorage.setItem('cart', JSON.stringify(newItems));
    }
  }

  updateQuantity(productId: number, quantity: number, size?: string): void {
    if (quantity <= 0) {
      this.removeFromCart(productId, size);
      return;
    }

    // Always update local cart first for immediate UI feedback
    this.updateLocalQuantity(productId, quantity, size);
    
    // If authenticated, also update server
    if (this.authService.isAuthenticated()) {
      this.http.put(`${this.apiUrl}/update`, 
        { productId, quantity }, 
        { headers: this.getHeaders() }
      ).subscribe({
        next: () => {
          // Server update successful
        },
        error: (error) => {
          console.error('Error updating server cart:', error);
        }
      });
    }
  }

  private updateLocalQuantity(productId: number, quantity: number, size?: string): void {
    const currentItems = [...this.cartItems.value];
    const item = currentItems.find(item => {
      if (size) {
        return item.id === productId && item.size === size;
      }
      return item.id === productId;
    });
    
    if (item) {
      item.quantity = quantity;
      this.updateLocalCart(currentItems);
    }
  }

  // Sync local cart with server when user logs in
  syncCartWithServer(): void {
    if (this.authService.isAuthenticated() && this.cartItems.value.length > 0) {
      this.cartItems.value.forEach(item => {
        this.addToCart(item, item.quantity);
      });
      this.updateLocalCart([]);
    }
  }

  private showAddToCartNotification(productName: string, quantity: number): void {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = 'cart-notification';
    notification.innerHTML = `
      <div class="notification-content">
        <i class="bi bi-check-circle-fill text-success me-2"></i>
        <span><strong>${productName}</strong> ${quantity > 1 ? `(${quantity})` : ''} added to cart!</span>
        <i class="bi bi-cart-fill ms-2"></i>
      </div>
    `;
    
    // Add styles
    notification.style.cssText = `
      position: fixed;
      top: 20px;
      right: 20px;
      background: linear-gradient(135deg, #D6A99D, #FBF3D5);
      color: #2c3e50;
      padding: 15px 20px;
      border-radius: 12px;
      box-shadow: 0 8px 25px rgba(0,0,0,0.15);
      z-index: 9999;
      font-weight: 600;
      border: 2px solid #9CAFAA;
      backdrop-filter: blur(10px);
      animation: slideInRight 0.4s ease-out;
      max-width: 300px;
    `;
    
    // Add animation keyframes if not already added
    if (!document.querySelector('#cart-notification-styles')) {
      const style = document.createElement('style');
      style.id = 'cart-notification-styles';
      style.textContent = `
        @keyframes slideInRight {
          from { transform: translateX(100%); opacity: 0; }
          to { transform: translateX(0); opacity: 1; }
        }
        @keyframes slideOutRight {
          from { transform: translateX(0); opacity: 1; }
          to { transform: translateX(100%); opacity: 0; }
        }
        .cart-notification .notification-content {
          display: flex;
          align-items: center;
          font-size: 14px;
        }
      `;
      document.head.appendChild(style);
    }
    
    // Add to DOM
    document.body.appendChild(notification);
    
    // Remove after 3 seconds
    setTimeout(() => {
      notification.style.animation = 'slideOutRight 0.4s ease-in';
      setTimeout(() => {
        if (notification.parentNode) {
          notification.parentNode.removeChild(notification);
        }
      }, 400);
    }, 3000);
  }
}