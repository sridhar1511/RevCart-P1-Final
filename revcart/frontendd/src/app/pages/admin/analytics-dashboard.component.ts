import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AnalyticsService } from '../../services/analytics.service';
import { forkJoin, of, timeout } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-analytics-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './analytics-dashboard.component.html',
  styleUrls: ['./analytics-dashboard.component.scss']
})
export class AnalyticsDashboardComponent implements OnInit, OnDestroy {
  totalOrders = 0;
  totalRevenue = 0;
  totalUsers = 0;
  totalProducts = 0;
  isLoading = true;
  private timeoutId: any;

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit() {
    this.loadAnalytics();
    this.timeoutId = setTimeout(() => {
      if (this.isLoading) this.isLoading = false;
    }, 10000);
  }

  ngOnDestroy() {
    if (this.timeoutId) clearTimeout(this.timeoutId);
  }

  loadAnalytics() {
    this.isLoading = true;
    forkJoin([
      this.analyticsService.getDashboardStats().pipe(
        timeout(5000),
        catchError(() => of({ data: {} }))
      ),
      this.analyticsService.getSalesAnalytics().pipe(
        timeout(5000),
        catchError(() => of({ data: {} }))
      ),
      this.analyticsService.getUserAnalytics().pipe(
        timeout(5000),
        catchError(() => of({ data: {} }))
      )
    ]).subscribe(([dashStats, salesData, userData]) => {
      const dash = dashStats?.data || {};
      this.totalOrders = dash.totalOrders || 0;
      this.totalRevenue = dash.totalRevenue || 0;
      this.totalUsers = dash.totalUsers || 0;
      this.totalProducts = dash.totalProducts || 0;
      this.isLoading = false;
      if (this.timeoutId) clearTimeout(this.timeoutId);
    });
  }
}
