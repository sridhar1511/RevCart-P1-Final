import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ProductService } from './product.service';

describe('ProductService', () => {
  let service: ProductService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProductService]
    });
    service = TestBed.inject(ProductService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all products', () => {
    const mockProducts = [
      { id: 1, name: 'Apple', price: 120 },
      { id: 2, name: 'Banana', price: 60 }
    ];

    service.getAllProducts().subscribe(products => {
      expect(products.length).toBe(2);
      expect(products[0].name).toBe('Apple');
    });

    const req = httpMock.expectOne('http://localhost:5258/api/products');
    expect(req.request.method).toBe('GET');
    req.flush(mockProducts);
  });

  it('should get product by id', () => {
    const mockProduct = { id: 1, name: 'Apple', price: 120 };

    service.getProductById(1).subscribe(product => {
      expect(product.name).toBe('Apple');
    });

    const req = httpMock.expectOne('http://localhost:5258/api/products/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockProduct);
  });

  it('should get products by category', () => {
    const mockProducts = [{ id: 1, name: 'Apple', category: 'fruits' }];

    service.getProductsByCategory('fruits').subscribe(products => {
      expect(products[0].category).toBe('fruits');
    });

    const req = httpMock.expectOne('http://localhost:5258/api/products/category/fruits');
    expect(req.request.method).toBe('GET');
    req.flush(mockProducts);
  });

  it('should search products', () => {
    const mockProducts = [{ id: 1, name: 'Apple', price: 120 }];

    service.searchProducts('Apple').subscribe(products => {
      expect(products[0].name).toBe('Apple');
    });

    const req = httpMock.expectOne('http://localhost:5258/api/products/search?name=Apple');
    expect(req.request.method).toBe('GET');
    req.flush(mockProducts);
  });

  it('should get all categories', () => {
    const mockCategories = ['fruits', 'vegetables', 'dairy'];

    service.getAllCategories().subscribe(categories => {
      expect(categories.length).toBe(3);
    });

    const req = httpMock.expectOne('http://localhost:5258/api/products/categories');
    expect(req.request.method).toBe('GET');
    req.flush(mockCategories);
  });
});
