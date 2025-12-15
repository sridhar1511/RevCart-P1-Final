import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CartService } from './cart.service';
import { AuthService } from './auth.service';

describe('CartService', () => {
  let service: CartService;
  let authService: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CartService, AuthService]
    });
    service = TestBed.inject(CartService);
    authService = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add item to cart', () => {
    const product = { id: 1, name: 'Apple', price: 120 };
    service.addToCart(product, 2);
    
    service.cartItems$.subscribe(items => {
      expect(items.length).toBeGreaterThan(0);
    });
  });

  it('should remove item from cart', () => {
    const product = { id: 1, name: 'Apple', price: 120 };
    service.addToCart(product, 2);
    service.removeFromCart(1);
    
    service.cartItems$.subscribe(items => {
      expect(items.find(i => i.id === 1)).toBeUndefined();
    });
  });

  it('should update quantity', () => {
    const product = { id: 1, name: 'Apple', price: 120 };
    service.addToCart(product, 2);
    service.updateQuantity(1, 5);
    
    service.cartItems$.subscribe(items => {
      const item = items.find(i => i.id === 1);
      expect(item?.quantity).toBe(5);
    });
  });

  it('should get cart total', () => {
    const product = { id: 1, name: 'Apple', price: 120, quantity: 2 };
    service.cartItems.next([product]);
    
    const total = service.getCartTotal();
    expect(total).toBe(240);
  });

  it('should get cart item count', () => {
    const product = { id: 1, name: 'Apple', price: 120, quantity: 2 };
    service.cartItems.next([product]);
    
    const count = service.getCartItemCount();
    expect(count).toBe(2);
  });

  it('should clear cart', () => {
    const product = { id: 1, name: 'Apple', price: 120 };
    service.addToCart(product, 2);
    service.clearCart();
    
    service.cartItems$.subscribe(items => {
      expect(items.length).toBe(0);
    });
  });
});
