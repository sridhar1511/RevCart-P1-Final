@echo off
echo Checking Jenkins Status...
echo.

REM Check if Jenkins service is running
sc query "Jenkins" >nul 2>&1
if %errorlevel% == 0 (
    echo ✅ Jenkins service is installed
    sc query "Jenkins" | findstr "STATE" | findstr "RUNNING" >nul
    if %errorlevel% == 0 (
        echo ✅ Jenkins service is running
    ) else (
        echo ❌ Jenkins service is not running
        echo Starting Jenkins service...
        net start Jenkins
    )
) else (
    echo ❌ Jenkins service not found
)

echo.
echo Checking Jenkins web interface...
curl -s -o nul -w "%%{http_code}" http://localhost:8080 > temp_status.txt
set /p status=<temp_status.txt
del temp_status.txt

if "%status%"=="200" (
    echo ✅ Jenkins web interface is accessible at http://localhost:8080
) else if "%status%"=="403" (
    echo ⚠️  Jenkins is running but requires authentication at http://localhost:8080
) else (
    echo ❌ Jenkins web interface not accessible (HTTP %status%)
)

echo.
echo Jenkins Status Check Complete
pause