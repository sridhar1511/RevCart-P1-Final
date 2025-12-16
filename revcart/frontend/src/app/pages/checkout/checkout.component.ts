import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';
import { PaymentService } from '../../services/payment.service';
import { CouponService } from '../../services/coupon.service';
import { AddressService, Address } from '../../services/address.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit {
  address: Address = {
    fullName: '',
    phone: '',
    line1: '',
    line2: '',
    city: '',
    state: '',
    pincode: ''
  };
  
  savedAddresses: Address[] = [];
  selectedAddressId: number | null = null;
  showAddressForm = false;
  saveThisAddress = false;
  
  paymentMethod = '';
  upiId = '';
  orderItems: any[] = [];
  subtotal = 0;
  deliveryFee = 30;
  tax = 0;
  total = 0;
  isProcessing = false;
  order: any = null;
  couponCode = '';
  couponDiscount = 0;
  couponApplied = false;
  couponMessage = '';
  availableCoupons: any[] = [];
  showCoupons = false;

  constructor(
    private cartService: CartService,
    private orderService: OrderService,
    private paymentService: PaymentService,
    private couponService: CouponService,
    private addressService: AddressService,
    private router: Router
  ) {}

  ngOnInit() {
    this.cartService.cartItems$.subscribe(items => {
      this.orderItems = items;
      this.calculateTotals();
    });
    
    // Load saved addresses from address service
    this.addressService.addresses$.subscribe(addresses => {
      this.savedAddresses = addresses;
      // Auto-select default address if available
      const defaultAddress = addresses.find(addr => addr.isDefault);
      if (defaultAddress && !this.selectedAddressId) {
        this.selectAddress(defaultAddress.id!);
      }
    });
    
    // Load available coupons
    this.loadAvailableCoupons();
  }

  calculateTotals() {
    this.subtotal = this.orderItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    this.tax = Math.round(this.subtotal * 0.05);
    this.total = this.subtotal + this.deliveryFee + this.tax - this.couponDiscount;
    
    // Refresh available coupons when subtotal changes
    if (this.subtotal > 0) {
      this.loadAvailableCoupons();
    }
  }

  applyCoupon() {
    if (!this.couponCode.trim()) {
      this.couponMessage = 'Please enter a coupon code';
      return;
    }

    console.log('Applying coupon:', this.couponCode, 'Amount:', this.subtotal);
    
    this.couponService.validateCoupon(this.couponCode, this.subtotal).subscribe({
      next: (response) => {
        console.log('Coupon response:', response);
        if (response.valid) {
          this.couponDiscount = response.discount;
          this.couponApplied = true;
          this.couponMessage = `Coupon applied! Discount: ₹${response.discount}`;
          this.calculateTotals();
        } else {
          this.couponMessage = response.message || 'Invalid coupon code';
          this.couponApplied = false;
          this.couponDiscount = 0;
        }
      },
      error: (error) => {
        console.error('Coupon error:', error);
        this.couponMessage = error.error?.message || 'Error validating coupon';
        this.couponApplied = false;
        this.couponDiscount = 0;
      }
    });
  }

  removeCoupon() {
    this.couponCode = '';
    this.couponDiscount = 0;
    this.couponApplied = false;
    this.couponMessage = '';
    this.calculateTotals();
  }
  
  loadAvailableCoupons() {
    this.couponService.getAllCoupons().subscribe({
      next: (coupons) => {
        this.availableCoupons = coupons.filter(coupon => coupon.active && coupon.minOrderAmount <= this.subtotal);
      },
      error: (error) => {
        console.error('Error loading coupons:', error);
        // Fallback to static coupons if API fails
        this.availableCoupons = [
          { code: 'SAVE10', discount: 10, discountType: 'PERCENTAGE', minOrderAmount: 500, description: '10% off on orders above ₹500' },
          { code: 'WELCOME20', discount: 20, discountType: 'PERCENTAGE', minOrderAmount: 1000, description: '20% off on orders above ₹1000' },
          { code: 'BIGDEAL', discount: 100, discountType: 'FIXED', minOrderAmount: 2000, description: '₹100 off on orders above ₹2000' }
        ].filter(coupon => coupon.minOrderAmount <= this.subtotal);
      }
    });
  }
  
  toggleCoupons() {
    this.showCoupons = !this.showCoupons;
    if (this.showCoupons) {
      this.loadAvailableCoupons();
    }
  }
  
  selectCoupon(coupon: any) {
    this.couponCode = coupon.code;
    this.applyCoupon();
    this.showCoupons = false;
  }
  
  selectAddress(addressId: number) {
    this.selectedAddressId = addressId;
    const selected = this.savedAddresses.find(addr => addr.id === addressId);
    if (selected) {
      this.address = { ...selected };
      this.showAddressForm = false;
    }
  }
  
  useNewAddress() {
    this.selectedAddressId = null;
    this.address = {
      fullName: '',
      phone: '',
      line1: '',
      line2: '',
      city: '',
      state: '',
      pincode: ''
    };
    this.showAddressForm = true;
  }
  
  saveAddress() {
    if (this.saveThisAddress && this.address.fullName) {
      this.addressService.saveAddress(this.address).subscribe({
        next: () => {
          console.log('Address saved successfully');
        },
        error: (error) => {
          console.error('Error saving address:', error);
        }
      });
    }
  }

  private saveOrderLocally(order: any): void {
    const existingOrders = JSON.parse(localStorage.getItem('orders') || '[]');
    existingOrders.push(order);
    localStorage.setItem('orders', JSON.stringify(existingOrders));
  }

  placeOrder() {
    if (!this.paymentMethod || this.paymentMethod.trim() === '') {
      alert('Please select a payment method');
      return;
    }

    if (!this.address.fullName || !this.address.phone || !this.address.line1) {
      alert('Please fill in all required address fields');
      return;
    }

    if (this.paymentMethod === 'upi' && !this.upiId) {
      alert('Please enter your UPI ID');
      return;
    }

    if (this.orderItems.length === 0) {
      alert('Your cart is empty');
      return;
    }

    this.isProcessing = true;
    
    // Save address if requested
    this.saveAddress();
    
    const fullAddress = `${this.address.line1}, ${this.address.line2}, ${this.address.city}, ${this.address.state} ${this.address.pincode}`;
    
    // Create local order without backend dependency
    this.order = {
      id: Date.now(),
      items: [...this.orderItems],
      totalAmount: this.total,
      deliveryAddress: fullAddress,
      phoneNumber: this.address.phone,
      customerName: this.address.fullName,
      status: 'CONFIRMED',
      orderDate: new Date().toISOString()
    };
    
    // Save order locally
    this.saveOrderLocally(this.order);
    this.processPayment();
  }

  processPayment() {
    if (!this.order) return;
    this.completeOrder(0);
  }

  completeOrder(paymentId: number) {
    // Clear cart thoroughly
    this.cartService.clearCart();
    localStorage.removeItem('cart');
    this.orderItems = [];
    
    alert(`Order placed successfully! Order ID: ${this.order.id}`);
    this.router.navigate(['/orders']);
    this.isProcessing = false;
  }
}
