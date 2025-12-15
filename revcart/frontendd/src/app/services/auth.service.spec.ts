import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login user', () => {
    const mockResponse = { token: 'test-token', user: { id: 1, email: 'test@test.com' } };
    
    service.login('test@test.com', 'password').subscribe(response => {
      expect(response.token).toBe('test-token');
    });

    const req = httpMock.expectOne('http://localhost:5258/api/auth/signin');
    expect(req.request.method).toBe('POST');
    req.flush(mockResponse);
  });

  it('should register user', () => {
    const userData = { name: 'John', email: 'john@test.com', password: 'password' };
    
    service.register(userData).subscribe(response => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne('http://localhost:5258/api/auth/signup');
    expect(req.request.method).toBe('POST');
    req.flush({ message: 'User registered' });
  });

  it('should logout user', () => {
    localStorage.setItem('token', 'test-token');
    service.logout();
    expect(localStorage.getItem('token')).toBeNull();
  });

  it('should check if authenticated', () => {
    localStorage.setItem('token', 'test-token');
    expect(service.isAuthenticated()).toBe(true);
    
    localStorage.removeItem('token');
    expect(service.isAuthenticated()).toBe(false);
  });

  it('should get token', () => {
    localStorage.setItem('token', 'test-token');
    expect(service.getToken()).toBe('test-token');
  });

  it('should check if admin', () => {
    const adminUser = { roles: ['ROLE_ADMIN'] };
    service.setCurrentUser(adminUser, 'token');
    expect(service.isAdmin()).toBe(true);
  });
});
