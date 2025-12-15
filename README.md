# RevCart - E-commerce Application

A full-stack e-commerce platform built with Spring Boot backend and Angular frontend, featuring Docker containerization and Jenkins CI/CD pipeline.

## 🚀 Quick Start

```bash
# Clone the repository
git clone https://github.com/sridhar1511/RevCart_P1-Final.git
cd RevCart_P1-Final

# Start the application
docker-compose up -d --build
```

**Access URLs:**
- Frontend: http://localhost:4200
- Backend API: http://localhost:8081/api
- MySQL: localhost:3307
- MongoDB: localhost:27017

## 🏗️ Architecture

### Backend (Spring Boot)
- **Port**: 8081
- **Database**: MySQL (products, users, orders)
- **NoSQL**: MongoDB (logs, sessions)
- **Authentication**: Custom provider with plain text support
- **APIs**: REST endpoints for cart, orders, products, users

### Frontend (Angular)
- **Port**: 4200
- **Framework**: Angular 15+
- **State Management**: localStorage for cart/wishlist/orders
- **Styling**: Bootstrap + custom CSS
- **Proxy**: Nginx for API routing

## 📦 Features

- **Product Catalog**: 88+ products across 8 categories
- **Shopping Cart**: Local-first with server sync
- **Wishlist**: localStorage-based implementation
- **User Authentication**: Login/register with custom auth
- **Order Management**: Local order storage with real-time updates
- **Delivery System**: Agent dashboard for order tracking
- **Responsive Design**: Mobile-friendly interface

## 🐳 Docker Setup

### Services
```yaml
mysql:        # Port 3307 (external)
mongodb:      # Port 27017 (external)  
backend:      # Port 8081
frontend:     # Port 4200
```

### Health Checks
- MySQL: `mysqladmin ping` with 20s timeout
- Backend: Waits for MySQL healthy status
- Automatic restart policies configured

## 🔧 Development

### Prerequisites
- Docker & Docker Compose
- Java 17+ (for local development)
- Node.js 18+ (for local development)
- MySQL 8.0+
- MongoDB 5.0+

### Local Development
```bash
# Backend
cd revcart/backend
mvn spring-boot:run

# Frontend
cd revcart/frontend
npm install
ng serve
```

### Database Configuration
```properties
# MySQL (application.properties)
spring.datasource.url=jdbc:mysql://mysql:3306/revcart
spring.datasource.username=root
spring.datasource.password=admin
```

## 🚀 CI/CD Pipeline

### Jenkins Configuration
- **Polling**: Every 10 minutes (H/10 * * * *)
- **Repository**: https://github.com/sridhar1511/RevCart_P1-Final.git
- **Deployment**: Docker Compose with automatic builds
- **Error Handling**: Graceful fallbacks for build failures

### Pipeline Stages
1. **Checkout**: Pull latest code from GitHub
2. **Deploy**: `docker-compose up -d --build`
3. **Health Check**: Verify container status

## 🔐 Authentication

### Test Credentials
```
Customer Login:
- Email: test@example.com
- Password: password

Delivery Agent:
- Username: agent
- Password: agent123
```

### Security Features
- Custom authentication provider
- Plain text passwords (development only)
- Session management with localStorage
- CORS configuration for cross-origin requests

## 📱 Key Components

### Cart Service
- Local-first approach prevents item reappearance
- Background server synchronization
- Real-time quantity updates

### Order Management
- localStorage-based order storage
- Delivery agent assignment
- Status tracking (Pending → Processing → Shipped → Delivered)

### Address Service
- Automatic ID generation
- Default address handling
- localStorage persistence

## 🛠️ Troubleshooting

### Common Issues

**MySQL Connection Refused**
```bash
# Check MySQL health
docker-compose logs mysql

# Restart with health checks
docker-compose down && docker-compose up -d
```

**Frontend Build Errors**
```bash
# Clear node modules
rm -rf node_modules package-lock.json
npm install
```

**Jenkins Pipeline Failures**
- Ensure Docker is available in Jenkins environment
- Check repository URL and credentials
- Verify Jenkinsfile syntax

### Port Conflicts
```bash
# Check port usage
netstat -tulpn | grep :4200
netstat -tulpn | grep :8081
netstat -tulpn | grep :3307
```

## 📊 Monitoring

### Application Logs
```bash
# View all services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
```

### Health Endpoints
- Backend Health: http://localhost:8081/actuator/health
- Frontend Status: http://localhost:4200
- Database Status: Check Docker logs

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Team

- **Developer**: Sridhar S
- **Repository**: https://github.com/sridhar1511/RevCart_P1-Final.git
- **Contact**: [Your Email]

---

**Built with ❤️ using Spring Boot, Angular, Docker, and Jenkins**