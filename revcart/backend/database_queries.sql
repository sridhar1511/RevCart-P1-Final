-- RevCart Database Queries
-- MySQL Database: revcart_db

-- Create Database
CREATE DATABASE IF NOT EXISTS revcart_db;
USE revcart_db;

-- Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(120) NOT NULL,
    phone VARCHAR(15),
    address TEXT,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products Table
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    image TEXT,
    description TEXT,
    stock_quantity INT DEFAULT 100,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert Admin User
INSERT INTO users (name, email, password, phone, address, role) VALUES 
('Sai Thota', 'saithota1207@gmail.com', '$2a$10$encrypted_password_here', '9000795258', 'Chennai, India', 'ADMIN');

-- Insert Sample Products
INSERT INTO products (name, category, price, unit, image, description) VALUES 
-- Fruits
('Fresh Apples', 'fruits', 120.00, '1kg', 'https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=300&h=200&fit=crop', 'Fresh red apples'),
('Bananas', 'fruits', 60.00, '1kg', 'https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=300&h=200&fit=crop', 'Ripe yellow bananas'),
('Oranges', 'fruits', 100.00, '1kg', 'https://images.unsplash.com/photo-1547514701-42782101795e?w=300&h=200&fit=crop', 'Juicy oranges'),
('Mangoes', 'fruits', 180.00, '1kg', 'https://www.metropolisindia.com/upgrade/blog/upload/25/05/benefits-of-mangoes1747828357.webp', 'Sweet mangoes'),

-- Vegetables
('Carrots', 'vegetables', 40.00, '500g', 'https://images.unsplash.com/photo-1445282768818-728615cc910a?w=300&h=200&fit=crop', 'Fresh orange carrots'),
('Tomatoes', 'vegetables', 80.00, '1kg', 'https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=300&h=200&fit=crop', 'Fresh red tomatoes'),
('Onions', 'vegetables', 30.00, '1kg', 'https://images.unsplash.com/photo-1508747703725-719777637510?w=300&h=200&fit=crop', 'Fresh onions'),
('Potatoes', 'vegetables', 25.00, '1kg', 'https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=300&h=200&fit=crop', 'Fresh potatoes'),

-- Dairy
('Milk', 'dairy', 55.00, '1L', 'https://nutritionsource.hsph.harvard.edu/wp-content/uploads/2024/11/AdobeStock_354060824-1024x683.jpeg', 'Fresh cow milk'),
('Cheese', 'dairy', 200.00, '250g', 'https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=300&h=200&fit=crop', 'Fresh cheese'),
('Yogurt', 'dairy', 45.00, '500g', 'https://img.freepik.com/free-vector/realistic-vector-icon-illustration-strawberry-yoghurt-jar-with-spoon-full-yogurt-isolated_134830-2521.jpg?semt=ais_hybrid&w=740&q=80', 'Greek yogurt'),
('Paneer', 'dairy', 180.00, '250g', 'https://chennaionlineshopping.in/image/cache/catalog/Products/panner/amul%20panner-800x800.jpg', 'Fresh paneer'),

-- Electronics
('Smartphone', 'electronics', 15000.00, '1 piece', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=300&h=200&fit=crop', 'Latest smartphone'),
('Wireless Mouse', 'electronics', 800.00, '1 piece', 'https://m.media-amazon.com/images/I/51vMo-pHZ5L.jpg', 'Ergonomic wireless mouse'),
('Power Bank', 'electronics', 1500.00, '1 piece', 'https://i03.appmifile.com/333_item_in/08/07/2025/9047b35e12fa25cb45fc93a824a29e87.jpg', '10000mAh power bank');

-- Common Queries

-- Get all products
SELECT * FROM products;

-- Get products by category
SELECT * FROM products WHERE category = 'fruits';

-- Search products by name
SELECT * FROM products WHERE name LIKE '%apple%';

-- Get user by email
SELECT * FROM users WHERE email = 'saithota1207@gmail.com';

-- Get products with price range
SELECT * FROM products WHERE price BETWEEN 50 AND 200;

-- Count products by category
SELECT category, COUNT(*) as product_count FROM products GROUP BY category;

-- Get all admin users
SELECT * FROM users WHERE role = 'ADMIN';

-- Update product stock
UPDATE products SET stock_quantity = 50 WHERE id = 1;

-- Delete product
DELETE FROM products WHERE id = 1;

-- Get products ordered by price
SELECT * FROM products ORDER BY price ASC;

-- Get expensive products (above 1000)
SELECT * FROM products WHERE price > 1000;

-- Update user profile
UPDATE users SET phone = '9876543210', address = 'New Address' WHERE id = 1;