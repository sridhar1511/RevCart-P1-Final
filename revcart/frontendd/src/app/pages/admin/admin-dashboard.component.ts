import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { ProductService } from '../../services/product.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  orders: any[] = [];
  products: any[] = [];
  stats = {
    totalOrders: 0,
    totalProducts: 0,
    totalRevenue: 0,
    pendingOrders: 0
  };
  selectedTab = 'orders';
  isLoading = true;

  constructor(
    private orderService: OrderService,
    private productService: ProductService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    if (!this.authService.isAuthenticated() || !this.authService.isAdmin()) {
      this.router.navigate(['/login']);
      return;
    }
    this.loadDashboardData();
    
    // Listen for storage changes (when delivery agent updates orders)
    window.addEventListener('storage', (event) => {
      if (event.key === 'orders') {
        this.loadDashboardData();
      }
    });
    
    // Auto-refresh every 30 seconds
    setInterval(() => {
      this.loadDashboardData();
    }, 30000);
  }

  loadDashboardData() {
    this.isLoading = true;
    
    // Load all orders from backend API
    this.orderService.getAllOrders().subscribe({
      next: (allOrders: any[]) => {
        const sortedOrders = allOrders.sort((a: any, b: any) => {
          // Sort by date - latest first
          const dateA = new Date(a.orderDate).getTime();
          const dateB = new Date(b.orderDate).getTime();
          return dateB - dateA;
        });
        
        // Show only last 10 orders
        this.orders = sortedOrders.slice(0, 10);
        
        // Calculate stats from all orders, not just displayed ones
        this.calculateStats(sortedOrders);
      },
      error: () => {
        console.error('Error loading orders');
      }
    });
    
    this.productService.getAllProducts().subscribe({
      next: (products: any[]) => {
        this.products = products;
        this.stats.totalProducts = products.length;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  calculateStats(allOrders?: any[]) {
    const ordersToCalculate = allOrders || this.orders;
    this.stats.totalOrders = ordersToCalculate.length;
    this.stats.totalRevenue = ordersToCalculate
      .filter(order => order.status === 'DELIVERED')
      .reduce((sum, order) => sum + (order.totalAmount || 0), 0);
    this.stats.pendingOrders = ordersToCalculate.filter(o => o.status === 'PLACED' || o.status === 'CONFIRMED' || o.status === 'PROCESSING').length;
  }

  updateOrderStatus(orderId: number, event: Event) {
    const status = (event.target as HTMLSelectElement).value;
    if (!status) return;
    this.orderService.updateOrderStatus(orderId, status).subscribe({
      next: () => {
        alert('Order status updated');
        this.loadDashboardData();
      },
      error: () => alert('Error updating order status')
    });
  }

  deleteProduct(productId: number) {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(productId).subscribe({
        next: () => {
          alert('Product deleted');
          this.loadDashboardData();
        },
        error: () => alert('Error deleting product')
      });
    }
  }

  getStatusColor(status: string): string {
    const colors: { [key: string]: string } = {
      'PENDING': 'warning',
      'CONFIRMED': 'info',
      'PROCESSING': 'info',
      'SHIPPED': 'primary',
      'DELIVERED': 'success',
      'CANCELLED': 'danger'
    };
    return colors[status] || 'secondary';
  }
}
