@echo off
echo Stopping and removing containers...
docker-compose down

echo Removing old images...
docker rmi revcart-backend revcart-frontend 2>nul

echo Building and starting containers...
docker-compose up --build -d

echo Waiting for services to start...
timeout /t 10

echo Checking container status...
docker-compose ps

echo.
echo Application should be available at:
echo Frontend: http://localhost:4200
echo Backend API: http://localhost:8081
echo.
echo Test login credentials:
echo Email: test@example.com
echo Password: secret
echo.
echo Admin login:
echo Email: admin@example.com
echo Password: secret