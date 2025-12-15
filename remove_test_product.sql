-- Disable safe update mode first
SET SQL_SAFE_UPDATES = 0;

-- Remove Test Product
DELETE FROM products WHERE name = 'Test Product';

-- Re-enable safe update mode
SET SQL_SAFE_UPDATES = 1;