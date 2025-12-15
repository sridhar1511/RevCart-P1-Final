import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CouponService } from '../../services/coupon.service';

@Component({
  selector: 'app-coupons',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './coupons.component.html',
  styleUrls: ['./coupons.component.scss']
})
export class CouponsComponent implements OnInit {
  coupons: any[] = [];
  loading = true;

  constructor(private couponService: CouponService) {}

  ngOnInit(): void {
    this.loadCoupons();
  }

  loadCoupons(): void {
    this.couponService.getAllCoupons().subscribe({
      next: (response: any) => {
        this.coupons = response;
        this.loading = false;
      },
      error: (error: any) => {
        console.error('Error loading coupons:', error);
        this.loading = false;
      }
    });
  }

  copyCouponCode(code: string): void {
    navigator.clipboard.writeText(code);
    alert('Coupon code copied: ' + code);
  }

  isExpired(expiryDate: string): boolean {
    return new Date(expiryDate) < new Date();
  }
}
