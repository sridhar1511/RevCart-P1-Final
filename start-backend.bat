@echo off
echo Starting RevCart Backend...
cd /d "c:\Users\Sridhar S\OneDrive\Desktop\revcart_p1\revcart\backend"

echo Testing MySQL connection...
mysql -u root -padmin -e "SELECT 1;" 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Cannot connect to MySQL. Please start MySQL service.
    pause
    exit /b 1
)

echo MySQL connection OK. Starting Spring Boot application...
java -jar target/revcart-backend-1.0.0.jar ^
    --spring.datasource.url=jdbc:mysql://localhost:3306/revcart_db?createDatabaseIfNotExist=true^&useSSL=false^&serverTimezone=UTC^&allowPublicKeyRetrieval=true ^
    --spring.datasource.username=root ^
    --spring.datasource.password=admin ^
    --spring.data.mongodb.uri=mongodb://localhost:27017/revcart_db ^
    --server.port=8081

pause