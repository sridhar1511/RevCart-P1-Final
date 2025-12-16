@echo off
echo Syncing data from local MySQL (3306) to Docker MySQL (3307)...

echo 1. Creating backup from local database...
mysqldump -h localhost -P 3306 -u root -padmin revcart_db > local_backup.sql

echo 2. Starting Docker containers...
docker-compose -f docker-compose-hub.yml up -d

echo 3. Waiting for MySQL to be ready...
timeout /t 15

echo 4. Restoring data to Docker MySQL...
mysql -h localhost -P 3307 -u root -padmin revcart_db < local_backup.sql

echo 5. Verifying data sync...
mysql -h localhost -P 3307 -u root -padmin -e "USE revcart_db; SELECT COUNT(*) as product_count FROM products;"

echo Data sync completed!
pause