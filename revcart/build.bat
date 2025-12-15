@echo off
echo Building backend...
cd backend
call mvn clean package -DskipTests
cd ..

echo Building Docker image...
docker build -t revcart-all-in-one .

echo Done! Run with: docker run -d -p 4200:4200 -p 8081:8081 -p 3306:3306 -p 27017:27017 --name revcart revcart-all-in-one
