@echo off
echo Starting RevCart Frontend...
cd /d "c:\Users\Sridhar S\OneDrive\Desktop\revcart_p1\revcart\frontendd"

echo Checking Node.js...
node --version
if %errorlevel% neq 0 (
    echo ERROR: Node.js not found. Please install Node.js.
    pause
    exit /b 1
)

echo Starting Angular development server...
ng serve --host 0.0.0.0 --port 4200

pause