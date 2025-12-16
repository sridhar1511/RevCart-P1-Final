@echo off
echo Building and pushing RevCart Docker images...

REM Replace 'yourusername' with your actual Docker Hub username
set DOCKER_USERNAME=sridhar151102

echo.
echo 1. Building backend image...
docker build -t %DOCKER_USERNAME%/revcart-backend:latest ./revcart/backend

echo.
echo 2. Building frontend image...
docker build -t %DOCKER_USERNAME%/revcart-frontend:latest ./revcart/frontend

echo.
echo 3. Pushing backend image to Docker Hub...
docker push %DOCKER_USERNAME%/revcart-backend:latest

echo.
echo 4. Pushing frontend image to Docker Hub...
docker push %DOCKER_USERNAME%/revcart-frontend:latest

echo.
echo Docker images pushed successfully!
echo Backend: %DOCKER_USERNAME%/revcart-backend:latest
echo Frontend: %DOCKER_USERNAME%/revcart-frontend:latest

pause