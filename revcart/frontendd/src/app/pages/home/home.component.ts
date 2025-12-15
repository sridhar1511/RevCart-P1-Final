import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SubscriptionService } from '../../services/subscription.service';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, CommonModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  email = '';
  subscribeLoading = false;
  subscribeMessage = '';
  subscribeError = false;
  trendingProducts: any[] = [];
  recommendedProducts: any[] = [];

  categories = [
    { name: 'Fruits', emoji: '🍎', slug: 'fruits', count: 45, image: 'https://images.unsplash.com/photo-1610832958506-aa56368176cf?w=300&h=200&fit=crop' },
    { name: 'Vegetables', emoji: '🥕', slug: 'vegetables', count: 38, image: 'https://images.unsplash.com/photo-1540420773420-3366772f4999?w=300&h=200&fit=crop' },
    { name: 'Dairy', emoji: '🥛', slug: 'dairy', count: 25, image: 'https://images.unsplash.com/photo-1563636619-e9143da7973b?w=300&h=200&fit=crop' },
    { name: 'Bakery', emoji: '🍞', slug: 'bakery', count: 32, image: 'https://images.unsplash.com/photo-1509440159596-0249088772ff?w=300&h=200&fit=crop' },
    { name: 'Electronics', emoji: '📱', slug: 'electronics', count: 28, image: 'https://images.unsplash.com/photo-1498049794561-7780e7231661?w=300&h=200&fit=crop' },
    { name: 'Sports', emoji: '⚽', slug: 'sports', count: 22, image: 'https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=300&h=200&fit=crop' },
    { name: 'Kids', emoji: '🧸', slug: 'kids', count: 35, image: 'https://images.unsplash.com/photo-1545558014-8692077e9b5c?w=300&h=200&fit=crop' },
    { name: 'Beauty', emoji: '💄', slug: 'beauty', count: 41, image: 'https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=300&h=200&fit=crop' },
    { name: 'Mens Clothing', emoji: '👔', slug: 'mens-clothing', count: 32, image: 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=300&h=200&fit=crop' },
    { name: 'Womens Clothing', emoji: '👗', slug: 'womens-clothing', count: 40, image: 'https://images.unsplash.com/photo-1595777707802-221b42c0bbb2?w=300&h=200&fit=crop' },
    { name: 'Kids Clothing', emoji: '👕', slug: 'kids-clothing', count: 28, image: 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=300&h=200&fit=crop' }
  ];

  constructor(
    private subscriptionService: SubscriptionService,
    private productService: ProductService,
    private cartService: CartService
  ) {}

  ngOnInit() {
    this.loadTrendingProducts();
  }

  loadTrendingProducts() {
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        console.log('Products loaded:', products.length);
        this.trendingProducts = products.slice(0, 6);
        this.recommendedProducts = products.slice(6, 12);
        console.log('Trending:', this.trendingProducts.length, 'Recommended:', this.recommendedProducts.length);
      },
      error: (error) => console.error('Error loading products:', error)
    });
  }

  addToCart(product: any): void {
    this.cartService.addToCart(product, 1);
  }

  subscribe(): void {
    if (!this.email || !this.email.includes('@')) {
      this.subscribeError = true;
      this.subscribeMessage = 'Please enter a valid email';
      return;
    }

    this.subscribeLoading = true;
    this.subscriptionService.subscribe(this.email).subscribe({
      next: (response) => {
        this.subscribeLoading = false;
        this.subscribeError = false;
        this.subscribeMessage = 'Successfully subscribed to RevCart!';
        this.email = '';
        setTimeout(() => this.subscribeMessage = '', 3000);
      },
      error: (error) => {
        this.subscribeLoading = false;
        this.subscribeError = true;
        this.subscribeMessage = error.error?.message || 'Error subscribing. Please try again.';
        setTimeout(() => this.subscribeMessage = '', 3000);
      }
    });
  }
}
