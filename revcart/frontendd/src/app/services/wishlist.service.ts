import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { ApiConfigService } from './api-config.service';

@Injectable({
  providedIn: 'root'
})
export class WishlistService {
  private apiUrl: string;
  private wishlistItems = new BehaviorSubject<any[]>([]);
  public wishlistItems$ = this.wishlistItems.asObservable();

  constructor(private http: HttpClient, private authService: AuthService, private apiConfig: ApiConfigService) {
    this.apiUrl = this.apiConfig.getApiUrl('wishlist');
    this.loadWishlist();
  }

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  loadWishlist(): void {
    // Load from localStorage for now
    const saved = localStorage.getItem('wishlist');
    if (saved) {
      try {
        this.wishlistItems.next(JSON.parse(saved));
      } catch (e) {
        this.wishlistItems.next([]);
      }
    }
  }

  addToWishlist(productId: number, product?: any): Observable<any> {
    return new Observable(observer => {
      const current = this.wishlistItems.value;
      if (!current.some(item => item.id === productId)) {
        const productData = product || { id: productId };
        const updated = [...current, productData];
        this.wishlistItems.next(updated);
        localStorage.setItem('wishlist', JSON.stringify(updated));
        this.showWishlistNotification(productData.name || productData.title || 'Product', 'added');
      }
      observer.next({ message: 'Added to wishlist' });
      observer.complete();
    });
  }

  removeFromWishlist(productId: number): Observable<any> {
    return new Observable(observer => {
      const current = this.wishlistItems.value;
      const removedItem = current.find(item => item.id === productId);
      const updated = current.filter(item => item.id !== productId);
      this.wishlistItems.next(updated);
      localStorage.setItem('wishlist', JSON.stringify(updated));
      if (removedItem) {
        this.showWishlistNotification(removedItem.name || removedItem.title || 'Product', 'removed');
      }
      observer.next({ message: 'Removed from wishlist' });
      observer.complete();
    });
  }

  isInWishlist(productId: number): boolean {
    return this.wishlistItems.value.some(item => item.id === productId);
  }

  getWishlistCount(): number {
    return this.wishlistItems.value.length;
  }

  private showWishlistNotification(productName: string, action: 'added' | 'removed'): void {
    // Remove any existing wishlist notifications to prevent overlap
    const existingNotifications = document.querySelectorAll('.wishlist-notification');
    existingNotifications.forEach(notification => {
      if (notification.parentNode) {
        notification.parentNode.removeChild(notification);
      }
    });

    // Create notification element
    const notification = document.createElement('div');
    notification.className = 'wishlist-notification';
    const icon = action === 'added' ? 'bi-heart-fill text-danger' : 'bi-heart text-muted';
    const message = action === 'added' ? 'added to wishlist!' : 'removed from wishlist!';
    const bgColor = action === 'added' ? 'linear-gradient(135deg, #FFB6C1, #FFF0F5)' : 'linear-gradient(135deg, #E8E8E8, #F5F5F5)';
    const borderColor = action === 'added' ? '#FF69B4' : '#999999';
    
    notification.innerHTML = `
      <div class="notification-content">
        <i class="bi ${icon} me-2"></i>
        <span><strong>${productName}</strong> ${message}</span>
        <i class="bi bi-heart-fill ms-2"></i>
      </div>
    `;
    
    // Add styles
    notification.style.cssText = `
      position: fixed;
      top: 80px;
      right: 20px;
      background: ${bgColor};
      color: #2c3e50;
      padding: 15px 20px;
      border-radius: 12px;
      box-shadow: 0 8px 25px rgba(0,0,0,0.15);
      z-index: 10000;
      font-weight: 600;
      border: 2px solid ${borderColor};
      backdrop-filter: blur(10px);
      animation: slideInRight 0.4s ease-out;
      max-width: 300px;
    `;
    
    // Add animation keyframes if not already added
    if (!document.querySelector('#wishlist-notification-styles')) {
      const style = document.createElement('style');
      style.id = 'wishlist-notification-styles';
      style.textContent = `
        @keyframes slideInRight {
          from { transform: translateX(100%); opacity: 0; }
          to { transform: translateX(0); opacity: 1; }
        }
        @keyframes slideOutRight {
          from { transform: translateX(0); opacity: 1; }
          to { transform: translateX(100%); opacity: 0; }
        }
        .wishlist-notification .notification-content {
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
