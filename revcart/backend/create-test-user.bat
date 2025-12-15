@echo off
echo Creating test users for login testing...
mysql -u root -p revcart_db < create_test_user.sql
echo Test users created successfully!
echo.
echo You can now test login with:
echo Email: test@example.com
echo Password: secret
echo.
echo Or admin login with:
echo Email: admin@example.com  
echo Password: secret
pause