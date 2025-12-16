import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';
import { WishlistService } from '../../services/wishlist.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule, FormsModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  isLoggedIn = false;
  currentUser: any = null;
  cartCount = 0;
  wishlistCount = 0;
  isAdmin = false;
  searchTerm = '';
  showDropdown = false;

  constructor(
    private authService: AuthService,
    private cartService: CartService,
    private wishlistService: WishlistService,
    private router: Router
  ) {}

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      this.isLoggedIn = !!user;
      this.isAdmin = user ? this.authService.isAdmin() : false;
      
      // If no regular user, check for delivery agent
      if (!user) {
        this.checkDeliveryAgent();
      }
    });
    
    // Initial check
    this.checkDeliveryAgent();
    
    // Listen for storage changes
    window.addEventListener('storage', () => {
      this.checkDeliveryAgent();
    });
  }
  
  private checkDeliveryAgent() {
    const deliveryAgent = localStorage.getItem('deliveryAgent');
    const currentUser = this.authService.getCurrentUser();
    
    if (deliveryAgent && !currentUser) {
      this.currentUser = JSON.parse(deliveryAgent);
      this.isLoggedIn = true;
      this.isAdmin = false;
    } else if (!deliveryAgent && !currentUser) {
      this.currentUser = null;
      this.isLoggedIn = false;
      this.isAdmin = false;
    } else if (currentUser) {
      this.currentUser = currentUser;
      this.isLoggedIn = true;
      this.isAdmin = this.authService.isAdmin();
    }

    // Update cart and wishlist counts
    this.cartService.cartItems$.subscribe(items => {
      this.cartCount = items.reduce((count, item) => count + item.quantity, 0);
    });

    this.wishlistService.wishlistItems$.subscribe(items => {
      this.wishlistCount = items.length;
    });
  }

  toggleDropdown() {
    this.showDropdown = !this.showDropdown;
  }

  isDeliveryAgent(): boolean {
    return !!localStorage.getItem('deliveryAgent') && !this.authService.getCurrentUser();
  }

  getDisplayName(): string {
    if (this.isDeliveryAgent()) {
      const agent = JSON.parse(localStorage.getItem('deliveryAgent') || '{}');
      return agent.name || 'Agent';
    }
    return this.currentUser?.firstName || 'User';
  }

  logout() {
    this.authService.logout();
    localStorage.removeItem('deliveryAgent');
    
    // Force immediate state reset
    this.isAdmin = false;
    this.showDropdown = false;
    this.currentUser = null;
    this.isLoggedIn = false;
    
    // Force component refresh
    this.checkDeliveryAgent();
    
    // Trigger storage event
    window.dispatchEvent(new Event('storage'));
    
    // Force change detection
    setTimeout(() => {
      this.checkDeliveryAgent();
    }, 0);
    
    this.router.navigate(['/home']);
  }
}
