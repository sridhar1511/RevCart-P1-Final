import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { OrderService } from './order.service';
import { AuthService } from './auth.service';

describe('OrderService', () => {
  let service: OrderService;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [OrderService, AuthService]
    });
    service = TestBed.inject(OrderService);
    httpMock = TestBed.inject(HttpTestingController);
    authService = TestBed.inject(AuthService);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should create order', () => {
    const mockResponse = { order: { id: 1, status: 'PENDING' } };

    service.createOrder('123 Main St', '9876543210').subscribe(response => {
      expect(response.order.id).toBe(1);
    });

    const req = httpMock.expectOne(req => req.url.includes('/api/orders'));
    expect(req.request.method).toBe('POST');
    req.flush(mockResponse);
  });

  it('should get user orders', () => {
    const mockOrders = [
      { id: 1, status: 'DELIVERED' },
      { id: 2, status: 'PENDING' }
    ];

    service.getUserOrders().subscribe(orders => {
      expect(orders.length).toBe(2);
    });

    const req = httpMock.expectOne('http://localhost:5258/api/orders');
    expect(req.request.method).toBe('GET');
    req.flush(mockOrders);
  });

  it('should get order by id', () => {
    const mockOrder = { id: 1, status: 'PENDING' };

    service.getOrderById(1).subscribe(order => {
      expect(order.id).toBe(1);
    });

    const req = httpMock.expectOne('http://localhost:5258/api/orders/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockOrder);
  });

  it('should cancel order', () => {
    service.cancelOrder(1).subscribe(response => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne('http://localhost:5258/api/orders/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({ message: 'Order cancelled' });
  });

  it('should update order status', () => {
    const mockOrder = { id: 1, status: 'CONFIRMED' };

    service.updateOrderStatus(1, 'CONFIRMED').subscribe(order => {
      expect(order.status).toBe('CONFIRMED');
    });

    const req = httpMock.expectOne(req => req.url.includes('/api/orders/1/status'));
    expect(req.request.method).toBe('PUT');
    req.flush(mockOrder);
  });
});
