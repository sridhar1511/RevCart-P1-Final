@echo off
REM Script to populate RevCart database with products

echo Populating RevCart database...
echo.

REM Execute the complete_products.sql file
mysql -u root -proot revcart_db < complete_products.sql

if %errorlevel% equ 0 (
    echo.
    echo Database populated successfully!
    echo.
    pause
) else (
    echo.
    echo Error: Failed to populate database
    echo Make sure MySQL is running and credentials are correct
    echo.
    pause
)
