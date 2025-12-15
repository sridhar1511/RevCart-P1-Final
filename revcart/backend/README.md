# RevCart Backend API

Spring Boot backend for RevCart grocery delivery application.

## Features

- **JWT Authentication** with Spring Security
- **RESTful APIs** for all operations
- **MySQL Database** with JPA/Hibernate
- **Role-based Access Control** (USER/ADMIN)
- **Comprehensive Testing** with JUnit 5
- **Global Exception Handling**
- **CORS Configuration** for Angular frontend

## API Endpoints

### Authentication
- `POST /api/auth/signin` - User login
- `POST /api/auth/signup` - User registration

### Products
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/category/{category}` - Get products by category
- `GET /api/products/search?name={name}` - Search products

### Cart Management
- `GET /api/cart` - Get user cart
- `POST /api/cart/add` - Add item to cart
- `PUT /api/cart/update` - Update cart item
- `DELETE /api/cart/remove/{productId}` - Remove item
- `DELETE /api/cart/clear` - Clear cart

### Order Management
- `POST /api/orders` - Create order
- `GET /api/orders` - Get user orders
- `GET /api/orders/{orderId}` - Get specific order

### User Profile
- `GET /api/user/profile` - Get user profile
- `PUT /api/user/profile` - Update profile
- `POST /api/user/change-password` - Change password

### Address Management
- `GET /api/user/addresses` - Get user addresses
- `POST /api/user/addresses` - Add address
- `PUT /api/user/addresses/{id}` - Update address
- `DELETE /api/user/addresses/{id}` - Delete address

### Admin Operations
- `GET /api/admin/users` - Get all users
- `POST /api/admin/products` - Add product
- `PUT /api/admin/products/{id}` - Update product
- `DELETE /api/admin/products/{id}` - Delete product
- `GET /api/admin/orders` - Get all orders
- `PUT /api/admin/orders/{id}/status` - Update order status
- `GET /api/admin/dashboard` - Get dashboard stats

## Setup

### Prerequisites
- Java 23
- Maven 3.6+
- MySQL 8.0+

### Database Setup
```sql
CREATE DATABASE revcart_db;
```

### Configuration
Update `application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=root
```

### Run Application
```bash
mvn spring-boot:run
```

### Run Tests
```bash
mvn test
```

## Default Admin User
- Email: `saithota1207@gmail.com`
- Password: `admin123`

## Security Features
- JWT token-based authentication
- Password encryption with BCrypt
- Role-based access control
- CORS configuration for frontend
- Global exception handling

## Testing
- Unit tests for controllers
- Integration tests with H2 database
- Security tests for authentication
- API endpoint testing with MockMvc