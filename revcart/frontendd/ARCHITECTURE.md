# RevCart Architecture

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                             │
│                    (Angular 18 Frontend)                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │   Pages      │  │ Components   │  │   Guards     │          │
│  ├──────────────┤  ├──────────────┤  ├──────────────┤          │
│  │ Home         │  │ Header       │  │ AuthGuard    │          │
│  │ Products     │  │ Footer       │  │ AdminGuard   │          │
│  │ Cart         │  │ ProductCard  │  │              │          │
│  │ Checkout     │  │              │  │              │          │
│  │ Orders       │  │              │  │              │          │
│  │ Profile      │  │              │  │              │          │
│  │ Admin        │  │              │  │              │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              SERVICES LAYER                             │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │ AuthService │ ProductService │ CartService │ OrderService│  │
│  │ PaymentService │ NotificationService │ UserService      │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │         HTTP CLIENT & INTERCEPTORS                       │  │
│  │         (JWT Token Management)                           │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
                              ↓ HTTP/REST
┌─────────────────────────────────────────────────────────────────┐
│                      API GATEWAY LAYER                          │
│                  (Spring Boot Backend)                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │           CONTROLLER LAYER (REST API)                   │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │ ProductController │ CartController │ OrderController    │  │
│  │ PaymentController │ NotificationController │ UserController
│  │ AuthController │ WebSocketController                    │  │
│  └──────────────────────────────────────────────────────────┘  │
│                              ↓
│  ┌──────────────────────────────────────────────────────────┐  │
│  │         SECURITY & AUTHENTICATION LAYER                 │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │ SecurityConfig │ AuthTokenFilter │ JwtUtils             │  │
│  │ UserDetailsServiceImpl │ UserDetailsImpl                  │  │
│  └──────────────────────────────────────────────────────────┘  │
│                              ↓
│  ┌──────────────────────────────────────────────────────────┐  │
│  │           SERVICE LAYER (Business Logic)                │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │ ProductService │ CartService │ OrderService             │  │
│  │ PaymentService │ NotificationService │ UserService      │  │
│  └──────────────────────────────────────────────────────────┘  │
│                              ↓
│  ┌──────────────────────────────────────────────────────────┐  │
│  │        REPOSITORY LAYER (Data Access)                   │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │ ProductRepository │ CartRepository │ OrderRepository    │  │
│  │ PaymentRepository │ UserRepository │ AddressRepository  │  │
│  │ CartItemRepository │ OrderItemRepository                │  │
│  │ NotificationRepository (MongoDB)                        │  │
│  └──────────────────────────────────────────────────────────┘  │
│                              ↓
│  ┌──────────────────────────────────────────────────────────┐  │
│  │         EXCEPTION HANDLING LAYER                        │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │ GlobalExceptionHandler │ Custom Exceptions              │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
                    ↓ JDBC/JPA    ↓ MongoDB Driver
┌──────────────────────────────────────────────────────────────────┐
│                    DATA PERSISTENCE LAYER                        │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌────────────────────────┐      ┌────────────────────────┐    │
│  │   MySQL Database       │      │  MongoDB Database      │    │
│  ├────────────────────────┤      ├────────────────────────┤    │
│  │ users                  │      │ notifications          │    │
│  │ products               │      │ delivery_logs          │    │
│  │ carts                  │      │                        │    │
│  │ cart_items             │      │                        │    │
│  │ orders                 │      │                        │    │
│  │ order_items            │      │                        │    │
│  │ payments               │      │                        │    │
│  │ addresses              │      │                        │    │
│  └────────────────────────┘      └────────────────────────┘    │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

## Data Flow Architecture

### User Registration & Login Flow
```
┌─────────────┐
│   User      │
│  Registers  │
└──────┬──────┘
       │
       ↓
┌──────────────────────┐
│ Frontend: Register   │
│ Component            │
└──────┬───────────────┘
       │
       ↓ POST /api/auth/signup
┌──────────────────────┐
│ Backend: AuthController
│ signup()             │
└──────┬───────────────┘
       │
       ↓
┌──────────────────────┐
│ UserDetailsServiceImpl│
│ createUser()         │
└──────┬───────────────┘
       │
       ↓
┌──────────────────────┐
│ UserRepository       │
│ save(user)           │
└──────┬───────────────┘
       │
       ↓
┌──────────────────────┐
│ MySQL: users table   │
│ User stored          │
└──────────────────────┘
```

### Shopping & Order Flow
```
┌──────────────┐
│ Browse       │
│ Products     │
└──────┬───────┘
       │
       ↓ GET /api/products
┌──────────────────────┐
│ ProductController    │
│ getAllProducts()     │
└──────┬───────────────┘
       │
       ↓
┌──────────────────────┐
│ ProductService       │
│ getAllProducts()     │
└──────┬───────────────┘
       │
       ↓
┌──────────────────────┐
│ ProductRepository    │
│ findAll()            │
└──────┬───────────────┘
       │
       ↓
┌──────────────────────┐
│ MySQL: products      │
│ Return products      │
└──────┬───────────────┘
       │
       ↓ Display Products
┌──────────────────────┐
│ Add to Cart          │
└──────┬───────────────┘
       │
       ↓ POST /api/cart/add
┌──────────────────────┐
│ CartController       │
│ addToCart()          │
└──────┬───────────────┘
       │
       ↓
┌──────────────────────┐
│ CartService          │
│ addToCart()          │
└──────┬───────────────┘
       │
       ↓
┌──────────────────────┐
│ CartRepository       │
│ save(cartItem)       │
└──────┬───────────────┘
       │
       ↓
┌──────────────────────┐
│ MySQL: cart_items    │
│ Item added           │
└──────┬───────────────┘
       │
       ↓ Proceed to Checkout
┌──────────────────────┐
│ POST /api/orders     │
│ Create Order         │
└──────┬───────────────┘
       │
       ↓
┌──────────────────────┐
│ OrderController      │
│ createOrder()        │
└──────┬───────────────┘
       │
       ↓
┌──────────────────────┐
│ OrderService         │
│ createOrder()        │
└──────┬───────────────┘
       │
       ├─→ Create Order Record
       ├─→ Create Order Items
       ├─→ Clear Cart
       └─→ Send Notification
       │
       ↓
┌──────────────────────┐
│ MySQL: orders        │
│ Order stored         │
└──────┬───────────────┘
       │
       ↓
┌──────────────────────┐
│ NotificationService  │
│ sendOrderConfirm()   │
└──────┬───────────────┘
       │
       ↓
┌──────────────────────┐
│ MongoDB: notifications
│ Notification stored  │
└──────────────────────┘
```

## Component Hierarchy

```
AppComponent
├── Header
│   ├── Navigation Menu
│   ├── Search Bar
│   └── User Menu
├── Router Outlet
│   ├── HomeComponent
│   ├── ProductsComponent
│   │   └── ProductCard (multiple)
│   ├── ProductDetailComponent
│   ├── CartComponent
│   ├── CheckoutComponent
│   ├── LoginComponent
│   ├── RegisterComponent
│   ├── ProfileComponent
│   ├── OrdersComponent
│   └── AdminDashboardComponent
└── Footer
    ├── Links
    ├── Contact Info
    └── Social Media
```

## Service Dependencies

```
AuthService
├── HttpClient
└── BehaviorSubject

ProductService
├── HttpClient
└── Observable

CartService
├── HttpClient
├── AuthService
└── BehaviorSubject

OrderService
├── HttpClient
├── AuthService
└── Observable

PaymentService
├── HttpClient
├── AuthService
└── Observable

NotificationService
├── HttpClient
├── AuthService
├── BehaviorSubject
└── Observable

UserService
├── HttpClient
├── AuthService
└── Observable
```

## Database Schema Relationships

```
┌─────────────┐
│   users     │
├─────────────┤
│ id (PK)     │
│ name        │
│ email       │
│ password    │
│ phone       │
│ address     │
│ role        │
└──────┬──────┘
       │
       ├─→ 1:1 ─→ ┌─────────────┐
       │          │   carts     │
       │          ├─────────────┤
       │          │ id (PK)     │
       │          │ user_id (FK)│
       │          └──────┬──────┘
       │                 │
       │                 ├─→ 1:N ─→ ┌──────────────┐
       │                 │          │ cart_items   │
       │                 │          ├──────────────┤
       │                 │          │ id (PK)      │
       │                 │          │ cart_id (FK) │
       │                 │          │ product_id   │
       │                 │          │ quantity     │
       │                 │          └──────────────┘
       │                 │
       │                 └─→ 1:N ─→ ┌──────────────┐
       │                            │ orders       │
       │                            ├──────────────┤
       │                            │ id (PK)      │
       │                            │ user_id (FK) │
       │                            │ total_amount │
       │                            │ status       │
       │                            │ order_date   │
       │                            └──────┬───────┘
       │                                   │
       │                                   ├─→ 1:N ─→ ┌──────────────┐
       │                                   │          │ order_items  │
       │                                   │          ├──────────────┤
       │                                   │          │ id (PK)      │
       │                                   │          │ order_id (FK)│
       │                                   │          │ product_id   │
       │                                   │          │ quantity     │
       │                                   │          │ price        │
       │                                   │          └──────────────┘
       │                                   │
       │                                   └─→ 1:1 ─→ ┌──────────────┐
       │                                              │ payments     │
       │                                              ├──────────────┤
       │                                              │ id (PK)      │
       │                                              │ order_id (FK)│
       │                                              │ amount       │
       │                                              │ method       │
       │                                              │ status       │
       │                                              └──────────────┘
       │
       ├─→ 1:N ─→ ┌──────────────┐
       │          │ addresses    │
       │          ├──────────────┤
       │          │ id (PK)      │
       │          │ user_id (FK) │
       │          │ name         │
       │          │ address_line │
       │          │ city         │
       │          │ state        │
       │          │ pincode      │
       │          │ phone        │
       │          │ is_default   │
       │          └──────────────┘
       │
       └─→ 1:N ─→ ┌──────────────┐
                  │ products     │
                  ├──────────────┤
                  │ id (PK)      │
                  │ name         │
                  │ category     │
                  │ price        │
                  │ unit         │
                  │ image        │
                  │ description  │
                  │ stock_qty    │
                  └──────────────┘

MongoDB Collections:
┌──────────────────────┐
│ notifications        │
├──────────────────────┤
│ _id (ObjectId)       │
│ user_id              │
│ title                │
│ message              │
│ type                 │
│ read                 │
│ created_at           │
└──────────────────────┘

┌──────────────────────┐
│ delivery_logs        │
├──────────────────────┤
│ _id (ObjectId)       │
│ order_id             │
│ status               │
│ location             │
│ timestamp            │
│ notes                │
└──────────────────────┘
```

## API Request/Response Flow

```
Client Request
    ↓
HTTP Request (with JWT Token)
    ↓
AuthTokenFilter
    ├─ Extract Token
    ├─ Validate Token
    └─ Set Authentication
    ↓
Controller
    ├─ Validate Input
    └─ Call Service
    ↓
Service
    ├─ Business Logic
    └─ Call Repository
    ↓
Repository
    ├─ Database Query
    └─ Return Data
    ↓
Service
    ├─ Process Data
    └─ Return Result
    ↓
Controller
    ├─ Format Response
    └─ Return HTTP Response
    ↓
HTTP Response (JSON)
    ↓
Client Receives Response
```

## Security Flow

```
User Login
    ↓
POST /api/auth/signin
    ↓
AuthController.signin()
    ↓
UserDetailsServiceImpl.loadUserByUsername()
    ↓
Verify Password (bcrypt)
    ↓
Generate JWT Token
    ↓
Return Token to Client
    ↓
Client Stores Token (localStorage)
    ↓
Subsequent Requests
    ↓
Include Token in Authorization Header
    ↓
AuthTokenFilter.doFilterInternal()
    ↓
Validate Token
    ↓
Extract User Info
    ↓
Set Authentication Context
    ↓
Allow Request to Proceed
```

## Deployment Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Production Environment                   │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              Load Balancer / Reverse Proxy           │  │
│  │              (Nginx / Apache)                        │  │
│  └──────────────────────────────────────────────────────┘  │
│                    ↓                    ↓                   │
│  ┌──────────────────────┐  ┌──────────────────────┐        │
│  │  Frontend Server 1   │  │  Frontend Server 2   │        │
│  │  (Angular Build)     │  │  (Angular Build)     │        │
│  └──────────────────────┘  └──────────────────────┘        │
│                                                             │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              API Load Balancer                       │  │
│  └──────────────────────────────────────────────────────┘  │
│                    ↓                    ↓                   │
│  ┌──────────────────────┐  ┌──────────────────────┐        │
│  │  Backend Server 1    │  │  Backend Server 2    │        │
│  │  (Spring Boot)       │  │  (Spring Boot)       │        │
│  └──────────────────────┘  └──────────────────────┘        │
│                                                             │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              Database Layer                          │  │
│  ├──────────────────────────────────────────────────────┤  │
│  │  ┌──────────────────┐      ┌──────────────────┐    │  │
│  │  │  MySQL Master    │      │  MySQL Slave     │    │  │
│  │  │  (Primary)       │      │  (Replica)       │    │  │
│  │  └──────────────────┘      └──────────────────┘    │  │
│  │                                                     │  │
│  │  ┌──────────────────┐      ┌──────────────────┐    │  │
│  │  │  MongoDB Primary │      │  MongoDB Replica │    │  │
│  │  └──────────────────┘      └──────────────────┘    │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                             │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              Monitoring & Logging                    │  │
│  │  (ELK Stack / Prometheus / Grafana)                 │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

This architecture provides:
- **Scalability**: Horizontal scaling of frontend and backend
- **Reliability**: Database replication and failover
- **Performance**: Load balancing and caching
- **Security**: Multiple layers of authentication and authorization
- **Maintainability**: Clear separation of concerns
- **Monitoring**: Comprehensive logging and monitoring
