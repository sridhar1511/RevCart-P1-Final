@echo off
echo Starting RevCart Backend...
cd /d "D:\RevCart_p1\backend"

REM Check if target/classes exists with compiled code
if exist "target\revcart-backend-1.0.0.jar" (
    echo Running JAR file...
    java -jar target\revcart-backend-1.0.0.jar
    pause
    exit /b 0
)

REM Try Maven
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Maven found. Building and running...
    call mvn clean package -DskipTests
    if %ERRORLEVEL% EQU 0 (
        java -jar target\revcart-backend-1.0.0.jar
    )
    pause
    exit /b %ERRORLEVEL%
)

REM Try Gradle
where gradle >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Gradle found. Building and running...
    call gradle clean build -x test
    if %ERRORLEVEL% EQU 0 (
        java -jar build\libs\revcart-backend-1.0.0.jar
    )
    pause
    exit /b %ERRORLEVEL%
)

echo Error: Maven or Gradle not found. Please install Maven or Gradle.
pause
exit /b 1
