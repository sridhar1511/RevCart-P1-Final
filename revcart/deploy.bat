@echo off
echo Starting RevCart Deployment...

echo Stopping existing services...
taskkill /F /IM java.exe /T 2>nul
taskkill /F /IM node.exe /T 2>nul

echo Building Frontend...
cd frontend
call npm install
call npm run build
cd ..

echo Building Backend...
cd backend
call mvn clean package -DskipTests
cd ..

echo Starting Services...
start "Backend" cmd /k "cd backend && mvn spring-boot:run"
timeout /t 10
start "Frontend" cmd /k "cd frontend && npm start"

echo Deployment Complete!
echo Frontend: http://localhost:4200
echo Backend: http://localhost:8081
pause