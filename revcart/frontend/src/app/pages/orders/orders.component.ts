import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss']
})
export class OrdersComponent implements OnInit {
  orders: any[] = [];
  filteredOrders: any[] = [];
  statusFilter = '';
  isLoading = true;

  constructor(private orderService: OrderService) {}

  ngOnInit() {
    this.loadOrders();
    
    // Listen for storage changes (when delivery agent updates order status)
    window.addEventListener('storage', (event) => {
      if (event.key === 'orders') {
        this.loadOrders();
      }
    });
    
    // Also listen for custom storage events from same tab
    window.addEventListener('storage', this.handleStorageChange.bind(this));
    
    // Auto-refresh every 30 seconds to check for updates
    setInterval(() => {
      this.loadOrders();
    }, 30000);
  }
  
  handleStorageChange(event: StorageEvent) {
    if (event.key === 'orders') {
      this.loadOrders();
    }
  }

  loadOrders() {
    this.isLoading = true;
    
    // Load orders from localStorage
    const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]');
    
    this.orders = savedOrders.map((order: any) => ({
      id: order.id,
      date: order.orderDate,
      status: order.status,
      total: order.totalAmount,
      lastUpdated: order.lastUpdated,
      items: order.items?.map((item: any) => ({
        name: item.name || 'Product',
        quantity: item.quantity,
        price: item.price,
        image: item.image || ''
      })) || [],
      timeline: {
        confirmed: ['CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED'].includes(order.status),
        packed: ['PROCESSING', 'SHIPPED', 'DELIVERED'].includes(order.status),
        shipped: ['SHIPPED', 'DELIVERED'].includes(order.status),
        delivered: order.status === 'DELIVERED'
      }
    }));
    
    // Sort orders by last updated (most recent first)
    this.orders.sort((a, b) => {
      const dateA = new Date(a.lastUpdated || a.date).getTime();
      const dateB = new Date(b.lastUpdated || b.date).getTime();
      return dateB - dateA;
    });
    
    this.filterOrders();
    this.isLoading = false;
  }

  filterOrders() {
    if (this.statusFilter) {
      this.filteredOrders = this.orders.filter(order => order.status === this.statusFilter);
    } else {
      this.filteredOrders = this.orders;
    }
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'DELIVERED': return 'success';
      case 'SHIPPED': return 'warning';
      case 'CONFIRMED':
      case 'PROCESSING': return 'info';
      case 'CANCELLED': return 'danger';
      case 'PENDING': return 'secondary';
      default: return 'secondary';
    }
  }

  getStatusLabel(status: string): string {
    const labels: { [key: string]: string } = {
      'PENDING': 'Pending',
      'CONFIRMED': 'Confirmed',
      'PROCESSING': 'Processing',
      'SHIPPED': 'Out for Delivery',
      'DELIVERED': 'Delivered',
      'CANCELLED': 'Cancelled'
    };
    return labels[status] || status;
  }

  canCancel(status: string): boolean {
    return ['PENDING', 'CONFIRMED', 'PROCESSING', 'PLACED'].includes(status);
  }

  cancelOrder(orderId: number) {
    if (confirm('Are you sure you want to cancel this order?')) {
      const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]');
      const orderIndex = savedOrders.findIndex((order: any) => order.id === orderId);
      
      if (orderIndex !== -1) {
        savedOrders[orderIndex].status = 'CANCELLED';
        savedOrders[orderIndex].lastUpdated = new Date().toISOString();
        localStorage.setItem('orders', JSON.stringify(savedOrders));
        alert('Order cancelled successfully');
        this.loadOrders();
      }
    }
  }
  
  refreshOrders() {
    this.loadOrders();
  }
}
