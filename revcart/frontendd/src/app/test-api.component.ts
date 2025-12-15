import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-test-api',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container mt-4">
      <h3>API Test Results</h3>
      <div class="alert alert-info">
        <strong>Backend Status:</strong> {{ backendStatus }}
      </div>
      <div class="alert alert-success" *ngIf="productCount > 0">
        <strong>Products Found:</strong> {{ productCount }}
      </div>
      <div class="alert alert-warning" *ngIf="productCount === 0">
        <strong>No products found!</strong>
      </div>
      <div *ngIf="error" class="alert alert-danger">
        <strong>Error:</strong> {{ error }}
      </div>
      <button class="btn btn-primary" (click)="testAPI()">Test API Connection</button>
    </div>
  `
})
export class TestApiComponent implements OnInit {
  backendStatus = 'Testing...';
  productCount = 0;
  error = '';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.testAPI();
  }

  testAPI() {
    this.backendStatus = 'Testing...';
    this.error = '';
    
    // Test products endpoint
    this.http.get<any[]>('/api/products').subscribe({
      next: (products) => {
        this.backendStatus = 'Connected ✓';
        this.productCount = products?.length || 0;
        console.log('API Test - Products received:', products?.length);
      },
      error: (err) => {
        this.backendStatus = 'Failed ✗';
        this.error = err.message || 'Connection failed';
        console.error('API Test Error:', err);
      }
    });
  }
}