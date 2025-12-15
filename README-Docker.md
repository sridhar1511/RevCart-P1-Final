# RevCart Docker & Jenkins Setup

## Prerequisites
- Docker and Docker Compose installed
- Jenkins installed with required plugins
- Git configured

## Docker Setup

### 1. Build Images
```bash
# Build all images
docker-compose build

# Or build individually
docker build -f Dockerfile.backend -t revcart-backend .
docker build -f Dockerfile.frontend -t revcart-frontend .
```

### 2. Run Application
```bash
# Start all services
docker-compose up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f
```

### 3. Access Application
- Frontend: http://localhost:4200
- Backend API: http://localhost:8081
- MySQL: localhost:3306
- MongoDB: localhost:27017

## Jenkins Setup

### 1. Install Required Plugins
- Docker Pipeline
- NodeJS Plugin
- Maven Integration
- Git Plugin

### 2. Configure Tools
- Maven 3.9.0
- NodeJS 18
- Docker

### 3. Create Pipeline Job
- New Item → Pipeline
- Pipeline script from SCM
- Repository: https://github.com/sridhar1511/RevCart_P1.git
- Script Path: Jenkinsfile

## Login Credentials
- Admin: admin@revcart.com / admin123
- User: sridharsag920@gmail.com / sridhar123
- Delivery Agent: agent@revcart.com / agent123

## Troubleshooting
- Check container logs: `docker-compose logs [service-name]`
- Restart services: `docker-compose restart`
- Clean rebuild: `docker-compose down && docker-compose up --build`