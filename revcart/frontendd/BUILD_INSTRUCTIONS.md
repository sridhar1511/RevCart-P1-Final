# RevCart Build & Run Instructions

## Backend Setup

### Prerequisites
- Java 17 or higher
- Maven 3.8+
- MySQL 8.0+
- MongoDB (optional, for logs)

### Build Backend

```bash
cd backend
mvn clean install
```

### Run Backend

```bash
mvn spring-boot:run
```

Or run the JAR directly:

```bash
mvn clean package
java -jar target/revcart-backend-1.0.0.jar
```

**Backend runs on**: `http://localhost:8081`

---

## Frontend Setup

### Prerequisites
- Node.js 18+
- npm or yarn
- Angular CLI 18

### Install Dependencies

```bash
cd frontend
npm install
```

### Run Frontend (Development)

```bash
ng serve
```

Or:

```bash
npm start
```

**Frontend runs on**: `http://localhost:4200`

### Build Frontend (Production)

```bash
ng build --configuration production
```

Output: `dist/` folder

---

## Database Setup

### MySQL

1. Create database:
```sql
CREATE DATABASE revcart;
```

2. Update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/revcart
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### MongoDB (Optional)

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/revcart
```

---

## Configuration Files

### Backend: `application.properties`

```properties
# Server
server.port=9090
server.servlet.context-path=/

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/revcart
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/revcart

# JWT
jwt.secret=your_secret_key_here_min_32_characters_long
jwt.expiration=86400000

# Email (Optional)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# CORS
cors.allowed-origins=http://localhost:4200
```

### Frontend: `environment.ts`

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:9090/api'
};
```

---

## Common Issues & Solutions

### Maven Build Error: "No plugin found for prefix"

**Solution**: Use correct Maven commands:
```bash
mvn clean install
mvn spring-boot:run
mvn clean package
```

**NOT**: `mvn spring-root:...` (this is incorrect)

### Port Already in Use

**Backend (9090)**:
```bash
# Windows
netstat -ano | findstr :9090
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :9090
kill -9 <PID>
```

**Frontend (4200)**:
```bash
# Windows
netstat -ano | findstr :4200
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :4200
kill -9 <PID>
```

### MySQL Connection Error

1. Verify MySQL is running
2. Check credentials in `application.properties`
3. Ensure database exists: `CREATE DATABASE revcart;`

### WebSocket Connection Error

- Ensure backend is running on port 9090
- Check CORS configuration
- Verify WebSocket endpoints are accessible

---

## Testing

### Backend Tests

```bash
mvn test
```

### Frontend Tests

```bash
ng test
```

---

## Deployment

### Backend Deployment

1. Build JAR:
```bash
mvn clean package
```

2. Deploy to server:
```bash
java -jar revcart-backend-1.0.0.jar
```

### Frontend Deployment

1. Build production:
```bash
ng build --configuration production
```

2. Deploy `dist/` folder to web server (Nginx, Apache, etc.)

---

## Verification Checklist

- [ ] Backend running on http://localhost:9090
- [ ] Frontend running on http://localhost:4200
- [ ] MySQL database created and connected
- [ ] Can access API endpoints
- [ ] WebSocket connection working
- [ ] Email service configured (optional)
- [ ] JWT tokens being generated
- [ ] CORS enabled for frontend

---

## Quick Start (All-in-One)

### Terminal 1 - Backend
```bash
cd backend
mvn spring-boot:run
```

### Terminal 2 - Frontend
```bash
cd frontend
npm install
ng serve
```

### Terminal 3 - MySQL (if not running as service)
```bash
mysql -u root -p
CREATE DATABASE revcart;
```

Then open browser: `http://localhost:4200`

---

## Support

For issues:
1. Check application logs
2. Verify all prerequisites are installed
3. Ensure ports 9090 and 4200 are available
4. Check database connection
5. Review error messages carefully
