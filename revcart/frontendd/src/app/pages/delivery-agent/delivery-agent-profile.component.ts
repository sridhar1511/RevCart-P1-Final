import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-delivery-agent-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container my-4">
      <div class="row">
        <div class="col-md-4">
          <div class="card">
            <div class="card-body text-center">
              <i class="bi bi-person-circle" style="font-size: 5rem; color: #007bff;"></i>
              <h4 class="mt-3">{{agent?.name || 'Agent'}}</h4>
              <p class="text-muted">{{agent?.email}}</p>
              <span class="badge" [class]="'bg-' + getStatusColor(agent?.status)">{{agent?.status || 'OFFLINE'}}</span>
            </div>
          </div>
          
          <div class="card mt-3">
            <div class="card-body">
              <h6>Statistics</h6>
              <div class="row text-center mb-3">
                <div class="col-4">
                  <h4 class="text-success">{{stats.delivered}}</h4>
                  <small class="text-muted">Orders</small>
                </div>
                <div class="col-4">
                  <h4 class="text-info">{{stats.totalProducts}}</h4>
                  <small class="text-muted">Products</small>
                </div>
                <div class="col-4">
                  <h4 class="text-warning">{{stats.rating}}</h4>
                  <small class="text-muted">Rating</small>
                </div>
              </div>
              
              <div class="rating-details">
                <h6>Rating Breakdown</h6>
                <div class="mb-2">
                  <div class="d-flex justify-content-between align-items-center">
                    <span>5 ⭐</span>
                    <div class="progress flex-grow-1 mx-2" style="height: 8px;">
                      <div class="progress-bar bg-success" [style.width.%]="ratingBreakdown.five"></div>
                    </div>
                    <small>{{ratingBreakdown.five}}%</small>
                  </div>
                </div>
                <div class="mb-2">
                  <div class="d-flex justify-content-between align-items-center">
                    <span>4 ⭐</span>
                    <div class="progress flex-grow-1 mx-2" style="height: 8px;">
                      <div class="progress-bar bg-info" [style.width.%]="ratingBreakdown.four"></div>
                    </div>
                    <small>{{ratingBreakdown.four}}%</small>
                  </div>
                </div>
                <div class="mb-2">
                  <div class="d-flex justify-content-between align-items-center">
                    <span>3 ⭐</span>
                    <div class="progress flex-grow-1 mx-2" style="height: 8px;">
                      <div class="progress-bar bg-warning" [style.width.%]="ratingBreakdown.three"></div>
                    </div>
                    <small>{{ratingBreakdown.three}}%</small>
                  </div>
                </div>
                <div class="mb-2">
                  <div class="d-flex justify-content-between align-items-center">
                    <span>2 ⭐</span>
                    <div class="progress flex-grow-1 mx-2" style="height: 8px;">
                      <div class="progress-bar bg-orange" [style.width.%]="ratingBreakdown.two"></div>
                    </div>
                    <small>{{ratingBreakdown.two}}%</small>
                  </div>
                </div>
                <div class="mb-2">
                  <div class="d-flex justify-content-between align-items-center">
                    <span>1 ⭐</span>
                    <div class="progress flex-grow-1 mx-2" style="height: 8px;">
                      <div class="progress-bar bg-danger" [style.width.%]="ratingBreakdown.one"></div>
                    </div>
                    <small>{{ratingBreakdown.one}}%</small>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div class="col-md-8">
          <div class="card">
            <div class="card-header">
              <h5>Profile Information</h5>
            </div>
            <div class="card-body">
              <form (ngSubmit)="updateProfile()">
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label class="form-label">Name</label>
                    <input type="text" class="form-control" [(ngModel)]="agent.name" name="name" required>
                  </div>
                  <div class="col-md-6 mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" class="form-control" [(ngModel)]="agent.email" name="email" disabled>
                  </div>
                </div>
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label class="form-label">Phone</label>
                    <input type="tel" class="form-control" [(ngModel)]="agent.phone" name="phone" required>
                  </div>
                  <div class="col-md-6 mb-3">
                    <label class="form-label">Vehicle Type</label>
                    <select class="form-control" [(ngModel)]="agent.vehicleType" name="vehicleType">
                      <option value="BIKE">Bike</option>
                      <option value="CAR">Car</option>
                      <option value="VAN">Van</option>
                    </select>
                  </div>
                </div>
                <div class="mb-3">
                  <label class="form-label">Address</label>
                  <textarea class="form-control" [(ngModel)]="agent.address" name="address" rows="3"></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Update Profile</button>
              </form>
            </div>
          </div>
          
          <div class="card mt-3">
            <div class="card-header">
              <h5>Recent Deliveries</h5>
            </div>
            <div class="card-body">
              <div *ngFor="let delivery of recentDeliveries" class="border-bottom pb-2 mb-2">
                <div class="d-flex justify-content-between">
                  <div>
                    <strong>Order #{{delivery.orderId}}</strong>
                    <p class="mb-0 text-muted">{{delivery.date}}</p>
                  </div>
                  <div class="text-end">
                    <span class="badge bg-success">Delivered</span>
                    <p class="mb-0">₹{{delivery.amount}}</p>
                  </div>
                </div>
              </div>
              <div *ngIf="recentDeliveries.length === 0" class="text-center text-muted">
                No recent deliveries
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class DeliveryAgentProfileComponent implements OnInit {
  agent: any = {};
  stats = {
    delivered: 0,
    totalProducts: 0,
    rating: 4.5
  };
  
  ratingBreakdown = {
    five: 65,
    four: 20,
    three: 10,
    two: 3,
    one: 2
  };
  recentDeliveries: any[] = [];

  constructor(private router: Router) {}

  ngOnInit() {
    const agentData = localStorage.getItem('deliveryAgent');
    if (!agentData) {
      this.router.navigate(['/delivery-agent/login']);
      return;
    }

    this.agent = JSON.parse(agentData);
    
    // Set default values if missing
    if (!this.agent.phone) this.agent.phone = '';
    if (!this.agent.address) this.agent.address = '';
    if (!this.agent.vehicleType) this.agent.vehicleType = 'BIKE';
    if (!this.agent.status) this.agent.status = 'OFFLINE';
    
    this.loadStats();
    this.loadRecentDeliveries();
  }

  loadStats() {
    // Load from localStorage or calculate from orders
    const orders = JSON.parse(localStorage.getItem('orders') || '[]');
    const deliveredOrders = orders.filter((o: any) => o.status === 'DELIVERED');
    
    this.stats.delivered = deliveredOrders.length;
    
    // Calculate total products delivered
    this.stats.totalProducts = deliveredOrders.reduce((total: number, order: any) => {
      return total + (order.items?.reduce((sum: number, item: any) => sum + (item.quantity || 1), 0) || 1);
    }, 0);
    
    // Load rating from localStorage or default
    const savedStats = localStorage.getItem('agentStats');
    if (savedStats) {
      const stats = JSON.parse(savedStats);
      this.stats.rating = stats.rating || 4.5;
      this.ratingBreakdown = stats.ratingBreakdown || this.ratingBreakdown;
    }
  }

  loadRecentDeliveries() {
    const orders = JSON.parse(localStorage.getItem('orders') || '[]');
    this.recentDeliveries = orders
      .filter((o: any) => o.status === 'DELIVERED')
      .slice(-5)
      .map((o: any) => ({
        orderId: o.id,
        date: new Date(o.orderDate).toLocaleDateString(),
        amount: o.totalAmount
      }));
  }

  updateProfile() {
    // Update localStorage
    localStorage.setItem('deliveryAgent', JSON.stringify(this.agent));
    
    // Trigger storage event to update header
    window.dispatchEvent(new StorageEvent('storage', {
      key: 'deliveryAgent',
      newValue: JSON.stringify(this.agent)
    }));
    
    alert('Profile updated successfully!');
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'AVAILABLE': return 'success';
      case 'BUSY': return 'warning';
      case 'OFFLINE': return 'secondary';
      default: return 'secondary';
    }
  }
}