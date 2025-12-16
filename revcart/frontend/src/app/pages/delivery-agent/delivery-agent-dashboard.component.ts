import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DeliveryService } from '../../services/delivery.service';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-delivery-agent-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './delivery-agent-dashboard.component.html',
  styleUrls: ['./delivery-agent-dashboard.component.scss']
})
export class DeliveryAgentDashboardComponent implements OnInit {
  agent: any = null;
  orders: any[] = [];
  filteredOrders: any[] = [];
  statusFilter = '';
  isLoading = true;

  constructor(
    private deliveryService: DeliveryService,
    private orderService: OrderService,
    private router: Router
  ) {}

  ngOnInit() {
    const agentData = localStorage.getItem('deliveryAgent');
    if (!agentData) {
      this.router.navigate(['/delivery-agent/login']);
      return;
    }

    this.agent = JSON.parse(agentData);
    this.loadOrders();
    
    // Create test order if none exist
    if (this.orders.length === 0) {
      this.createTestOrder();
    }
  }

  createTestOrder() {
    const testOrder = {
      id: Date.now(),
      orderDate: new Date().toISOString(),
      status: 'CONFIRMED',
      totalAmount: 299,
      deliveryAddress: '123 Test Street, Test City',
      phoneNumber: '9876543210',
      items: [{ name: 'Test Product', quantity: 1, price: 299 }]
    };
    
    const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]');
    savedOrders.push(testOrder);
    localStorage.setItem('orders', JSON.stringify(savedOrders));
    this.loadOrders();
  }

  loadOrders() {
    this.isLoading = true;
    
    // Load orders from localStorage (same as user orders)
    const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]');
    
    this.orders = savedOrders.map((order: any) => ({
      id: order.id,
      orderDate: order.orderDate,
      status: order.status,
      totalAmount: order.totalAmount,
      deliveryAddress: order.deliveryAddress,
      phoneNumber: order.phoneNumber,
      customerName: order.customerName || order.user?.name || 'Customer',
      items: order.items || []
    })).sort((a: any, b: any) => {
      // Sort by date - latest first
      const dateA = new Date(a.orderDate).getTime();
      const dateB = new Date(b.orderDate).getTime();
      return dateB - dateA;
    });
    
    this.filterOrders();
    this.isLoading = false;
  }

  filterOrders() {
    if (this.statusFilter) {
      this.filteredOrders = this.orders.filter(o => o.status === this.statusFilter);
    } else {
      this.filteredOrders = this.orders;
    }
  }

  updateOrderStatus(orderId: number, newStatus: string) {
    console.log(`Updating order ${orderId} to status ${newStatus}`);
    
    const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]');
    console.log('Current orders:', savedOrders);
    
    const orderIndex = savedOrders.findIndex((order: any) => order.id === orderId);
    console.log('Order index:', orderIndex);
    
    if (orderIndex !== -1) {
      const currentStatus = savedOrders[orderIndex].status;
      console.log(`Current status: ${currentStatus}, New status: ${newStatus}`);
      
      // Validate status transition
      const validTransitions: { [key: string]: string[] } = {
        'PLACED': ['PROCESSING'],
        'CONFIRMED': ['PROCESSING'],
        'PROCESSING': ['SHIPPED'],
        'SHIPPED': ['DELIVERED']
      };
      
      if (validTransitions[currentStatus]?.includes(newStatus) || currentStatus === newStatus) {
        savedOrders[orderIndex].status = newStatus;
        savedOrders[orderIndex].lastUpdated = new Date().toISOString();
        localStorage.setItem('orders', JSON.stringify(savedOrders));
        
        // Trigger storage event for other tabs/components
        window.dispatchEvent(new StorageEvent('storage', {
          key: 'orders',
          newValue: JSON.stringify(savedOrders)
        }));
        
        // If order is delivered, update agent stats
        if (newStatus === 'DELIVERED') {
          this.updateAgentStats();
        }
        
        alert(`Order #${orderId} status updated to ${this.getStatusLabel(newStatus)}`);
        this.loadOrders();
      } else {
        alert(`Cannot update order from ${currentStatus} to ${newStatus}`);
      }
    } else {
      alert('Order not found');
    }
  }

  updateAgentStatus(newStatus: string) {
    if (!this.agent) {
      alert('Agent not found. Please login again.');
      return;
    }
    
    this.agent.status = newStatus;
    localStorage.setItem('deliveryAgent', JSON.stringify(this.agent));
    alert('Status updated');
  }

  logout() {
    localStorage.removeItem('deliveryAgent');
    this.router.navigate(['/home']).then(() => {
      window.location.reload();
    });
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'DELIVERED': return 'success';
      case 'SHIPPED': return 'warning';
      case 'CONFIRMED':
      case 'PROCESSING': return 'info';
      default: return 'secondary';
    }
  }

  isAgentAvailable(): boolean {
    return this.agent?.status === 'AVAILABLE';
  }

  updateAgentStats(): void {
    const orders = JSON.parse(localStorage.getItem('orders') || '[]');
    const deliveredOrders = orders.filter((o: any) => o.status === 'DELIVERED');
    
    // Calculate total products delivered
    const totalProducts = deliveredOrders.reduce((total: number, order: any) => {
      return total + (order.items?.reduce((sum: number, item: any) => sum + (item.quantity || 1), 0) || 1);
    }, 0);
    
    // Update agent stats
    const agentStats = {
      delivered: deliveredOrders.length,
      totalProducts: totalProducts,
      rating: 4.5,
      ratingBreakdown: {
        five: 65,
        four: 20,
        three: 10,
        two: 3,
        one: 2
      }
    };
    
    localStorage.setItem('agentStats', JSON.stringify(agentStats));
  }

  getStatusLabel(status: string): string {
    const labels: { [key: string]: string } = {
      'CONFIRMED': 'Confirmed',
      'PROCESSING': 'Processing',
      'SHIPPED': 'Out for Delivery',
      'DELIVERED': 'Delivered'
    };
    return labels[status] || status;
  }
}
