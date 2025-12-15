import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  cartItems: any[] = [];
  deliveryFee = 30;
  taxRate = 0.05;

  constructor(private cartService: CartService) {}

  ngOnInit() {
    this.cartService.cartItems$.subscribe(items => {
      this.cartItems = [...items];
    });
  }

  updateQuantity(item: any, change: number) {
    const newQuantity = item.quantity + change;
    if (newQuantity > 0) {
      this.cartService.updateQuantity(item.id, newQuantity, item.size);
    } else if (newQuantity <= 0) {
      this.removeItem(item);
    }
  }

  removeItem(item: any) {
    this.cartService.removeFromCart(item.id, item.size);
  }

  getTotalItems() {
    return this.cartItems.reduce((count, item) => count + item.quantity, 0);
  }

  getSubtotal() {
    return this.cartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
  }

  getTax() {
    return Math.round(this.getSubtotal() * this.taxRate);
  }

  getTotal() {
    return this.getSubtotal() + this.deliveryFee + this.getTax();
  }
}