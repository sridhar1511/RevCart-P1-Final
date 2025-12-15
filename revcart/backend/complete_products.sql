-- Complete Product Catalog for RevCart (88 Products)
USE revcart_db;

-- Clear existing products (optional)
-- DELETE FROM products;

-- FRUITS (12 Products)
INSERT IGNORE INTO products (id, name, category, price, unit, image, description) VALUES 
(1, 'Fresh Apples', 'fruits', 120.00, '1kg', 'https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=300&h=200&fit=crop', 'Fresh red apples'),
(2, 'Bananas', 'fruits', 60.00, '1kg', 'https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=300&h=200&fit=crop', 'Ripe yellow bananas'),
(3, 'Oranges', 'fruits', 100.00, '1kg', 'https://images.unsplash.com/photo-1547514701-42782101795e?w=300&h=200&fit=crop', 'Juicy oranges'),
(4, 'Grapes', 'fruits', 150.00, '500g', 'https://images.unsplash.com/photo-1585518419759-87a89d9b2d4f?w=300&h=200&fit=crop', 'Sweet grapes'),
(37, 'Strawberries', 'fruits', 200.00, '250g', 'https://images.unsplash.com/photo-1464454709131-ffd692591ee5?w=300&h=200&fit=crop', 'Fresh strawberries'),
(38, 'Mangoes', 'fruits', 180.00, '1kg', 'https://images.unsplash.com/photo-1585518419759-87a89d9b2d4f?w=300&h=200&fit=crop', 'Sweet mangoes'),
(39, 'Pineapple', 'fruits', 80.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fresh pineapple'),
(40, 'Watermelon', 'fruits', 40.00, '1kg', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Juicy watermelon'),
(65, 'Papaya', 'fruits', 70.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fresh papaya'),
(66, 'Guava', 'fruits', 50.00, '500g', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Sweet guava'),
(67, 'Kiwi', 'fruits', 160.00, '500g', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fresh kiwi'),
(68, 'Pomegranate', 'fruits', 120.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fresh pomegranate');

-- VEGETABLES (12 Products)
INSERT IGNORE INTO products (id, name, category, price, unit, image, description) VALUES 
(5, 'Carrots', 'vegetables', 40.00, '500g', 'https://images.unsplash.com/photo-1445282768818-728615cc910a?w=300&h=200&fit=crop', 'Fresh orange carrots'),
(6, 'Tomatoes', 'vegetables', 80.00, '1kg', 'https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=300&h=200&fit=crop', 'Fresh red tomatoes'),
(7, 'Onions', 'vegetables', 30.00, '1kg', 'https://images.unsplash.com/photo-1508747703725-719777637510?w=300&h=200&fit=crop', 'Fresh onions'),
(8, 'Potatoes', 'vegetables', 25.00, '1kg', 'https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=300&h=200&fit=crop', 'Fresh potatoes'),
(33, 'Broccoli', 'vegetables', 60.00, '500g', 'https://images.unsplash.com/photo-1459411621453-7b03977f4bfc?w=300&h=200&fit=crop', 'Fresh broccoli'),
(34, 'Spinach', 'vegetables', 35.00, '250g', 'https://images.unsplash.com/photo-1511689915661-c6dcc9b12e6d?w=300&h=200&fit=crop', 'Fresh spinach leaves'),
(35, 'Bell Peppers', 'vegetables', 70.00, '500g', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Colorful bell peppers'),
(36, 'Cauliflower', 'vegetables', 45.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fresh cauliflower'),
(69, 'Cucumber', 'vegetables', 35.00, '500g', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fresh cucumber'),
(70, 'Cabbage', 'vegetables', 30.00, '1kg', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fresh cabbage'),
(71, 'Garlic', 'vegetables', 50.00, '250g', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fresh garlic'),
(72, 'Ginger', 'vegetables', 60.00, '250g', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fresh ginger');

-- DAIRY (12 Products)
INSERT IGNORE INTO products (id, name, category, price, unit, image, description) VALUES 
(9, 'Milk', 'dairy', 55.00, '1L', 'https://images.unsplash.com/photo-1550583724-b2692b25a968?w=300&h=200&fit=crop', 'Fresh cow milk'),
(10, 'Cheese', 'dairy', 200.00, '250g', 'https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=300&h=200&fit=crop', 'Fresh cheese'),
(11, 'Yogurt', 'dairy', 45.00, '500g', 'https://images.unsplash.com/photo-1488477181946-6428a0291840?w=300&h=200&fit=crop', 'Greek yogurt'),
(12, 'Butter', 'dairy', 120.00, '200g', 'https://images.unsplash.com/photo-1589985643862-8633ae57811f?w=300&h=200&fit=crop', 'Fresh butter'),
(57, 'Cream', 'dairy', 80.00, '200ml', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Heavy cream'),
(58, 'Ice Cream', 'dairy', 150.00, '500ml', 'https://images.unsplash.com/photo-1563805042-7684c019e1cb?w=300&h=200&fit=crop', 'Vanilla ice cream'),
(59, 'Paneer', 'dairy', 180.00, '250g', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fresh paneer'),
(60, 'Ghee', 'dairy', 300.00, '500ml', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Pure cow ghee'),
(73, 'Curd', 'dairy', 40.00, '500g', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fresh curd'),
(74, 'Condensed Milk', 'dairy', 120.00, '400g', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Sweetened condensed milk'),
(75, 'Mozzarella', 'dairy', 250.00, '200g', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fresh mozzarella'),
(76, 'Cottage Cheese', 'dairy', 160.00, '250g', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fresh cottage cheese');

-- BAKERY (12 Products)
INSERT IGNORE INTO products (id, name, category, price, unit, image, description) VALUES 
(13, 'Bread', 'bakery', 35.00, '1 loaf', 'https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=300&h=200&fit=crop', 'Whole wheat bread'),
(14, 'Croissant', 'bakery', 25.00, '1 piece', 'https://images.unsplash.com/photo-1585518419759-87a89d9b2d4f?w=300&h=200&fit=crop', 'Buttery croissant'),
(15, 'Muffins', 'bakery', 80.00, '4 pieces', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Blueberry muffins'),
(16, 'Cookies', 'bakery', 60.00, '6 pieces', 'https://images.unsplash.com/photo-1499636136210-6f4ee915583e?w=300&h=200&fit=crop', 'Chocolate cookies'),
(61, 'Donuts', 'bakery', 120.00, '6 pieces', 'https://images.unsplash.com/photo-1585518419759-87a89d9b2d4f?w=300&h=200&fit=crop', 'Glazed donuts'),
(62, 'Cake', 'bakery', 400.00, '1 piece', 'https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=300&h=200&fit=crop', 'Chocolate cake'),
(63, 'Bagels', 'bakery', 90.00, '4 pieces', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fresh bagels'),
(64, 'Pastry', 'bakery', 50.00, '1 piece', 'https://images.unsplash.com/photo-1585518419759-87a89d9b2d4f?w=300&h=200&fit=crop', 'Fruit pastry'),
(77, 'Biscuits', 'bakery', 40.00, '200g', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Digestive biscuits'),
(78, 'Brownies', 'bakery', 100.00, '4 pieces', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fudgy brownies'),
(79, 'Cheesecake', 'bakery', 350.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'New York cheesecake'),
(80, 'Waffle', 'bakery', 60.00, '2 pieces', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Belgian waffles');

-- ELECTRONICS (12 Products)
INSERT IGNORE INTO products (id, name, category, price, unit, image, description) VALUES 
(17, 'Smartphone', 'electronics', 15000.00, '1 piece', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=300&h=200&fit=crop', 'Latest smartphone'),
(18, 'Headphones', 'electronics', 2500.00, '1 piece', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=300&h=200&fit=crop', 'Wireless headphones'),
(19, 'Laptop', 'electronics', 45000.00, '1 piece', 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=300&h=200&fit=crop', 'Gaming laptop'),
(20, 'Smart Watch', 'electronics', 8000.00, '1 piece', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=300&h=200&fit=crop', 'Fitness smart watch'),
(53, 'Tablet', 'electronics', 20000.00, '1 piece', 'https://images.unsplash.com/photo-1561070791-2526d30994b5?w=300&h=200&fit=crop', '10-inch tablet'),
(54, 'Power Bank', 'electronics', 1500.00, '1 piece', 'https://images.unsplash.com/photo-1609091839311-d5365f9ff1c5?w=300&h=200&fit=crop', '10000mAh power bank'),
(55, 'Bluetooth Speaker', 'electronics', 3000.00, '1 piece', 'https://images.unsplash.com/photo-1589003077984-894e133814c9?w=300&h=200&fit=crop', 'Portable speaker'),
(56, 'Wireless Mouse', 'electronics', 800.00, '1 piece', 'https://images.unsplash.com/photo-1527814050087-3793815479db?w=300&h=200&fit=crop', 'Ergonomic wireless mouse'),
(81, 'USB Cable', 'electronics', 200.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'High-speed USB cable'),
(82, 'Screen Protector', 'electronics', 300.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Tempered glass protector'),
(83, 'Phone Case', 'electronics', 400.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Protective phone case'),
(84, 'Charger', 'electronics', 500.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Fast charging adapter');

-- SPORTS (12 Products)
INSERT IGNORE INTO products (id, name, category, price, unit, image, description) VALUES 
(21, 'Football', 'sports', 800.00, '1 piece', 'https://images.unsplash.com/photo-1461896836934-ffe607ba8211?w=300&h=200&fit=crop', 'Professional football'),
(22, 'Cricket Bat', 'sports', 1200.00, '1 piece', 'https://images.unsplash.com/photo-1624526267942-ab67cb7db225?w=300&h=200&fit=crop', 'Professional cricket bat'),
(23, 'Cricket Ball', 'sports', 300.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Leather cricket ball'),
(24, 'Tennis Racket', 'sports', 1500.00, '1 piece', 'https://images.unsplash.com/photo-1554224311-beee415c15c8?w=300&h=200&fit=crop', 'Professional tennis racket'),
(41, 'Basketball', 'sports', 900.00, '1 piece', 'https://images.unsplash.com/photo-1546519638-68711109d298?w=300&h=200&fit=crop', 'Professional basketball'),
(42, 'Badminton Racket', 'sports', 800.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Lightweight badminton racket'),
(43, 'Table Tennis Paddle', 'sports', 400.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Professional table tennis paddle'),
(44, 'Volleyball', 'sports', 600.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Professional volleyball'),
(45, 'Yoga Mat', 'sports', 600.00, '1 piece', 'https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=300&h=200&fit=crop', 'Premium yoga mat'),
(46, 'Dumbbells', 'sports', 2000.00, '1 pair', 'https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=300&h=200&fit=crop', 'Adjustable dumbbells'),
(47, 'Swimming Goggles', 'sports', 350.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Anti-fog swimming goggles'),
(48, 'Boxing Gloves', 'sports', 1800.00, '1 pair', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Professional boxing gloves');

-- KIDS (8 Products)
INSERT IGNORE INTO products (id, name, category, price, unit, image, description) VALUES 
(25, 'Teddy Bear', 'kids', 500.00, '1 piece', 'https://images.unsplash.com/photo-1585951237318-9ea5e175b891?w=300&h=200&fit=crop', 'Soft teddy bear'),
(26, 'Building Blocks', 'kids', 800.00, '1 set', 'https://images.unsplash.com/photo-1594787318286-3d835c1cab83?w=300&h=200&fit=crop', 'Colorful building blocks'),
(27, 'Puzzle Game', 'kids', 300.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Educational puzzle'),
(28, 'Remote Car', 'kids', 1200.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'RC racing car'),
(85, 'Action Figure', 'kids', 400.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Superhero action figure'),
(86, 'Board Game', 'kids', 600.00, '1 set', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Family board game'),
(87, 'Coloring Book', 'kids', 150.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Creative coloring book'),
(88, 'Toy Train', 'kids', 900.00, '1 set', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Electric toy train');

-- BEAUTY (8 Products)
INSERT IGNORE INTO products (id, name, category, price, unit, image, description) VALUES 
(29, 'Face Cream', 'beauty', 450.00, '50ml', 'https://images.unsplash.com/photo-1556228578-8c89e6adf883?w=300&h=200&fit=crop', 'Anti-aging face cream'),
(30, 'Lipstick', 'beauty', 350.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Matte lipstick'),
(31, 'Shampoo', 'beauty', 250.00, '200ml', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Hair care shampoo'),
(32, 'Perfume', 'beauty', 1500.00, '100ml', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Luxury perfume'),
(49, 'Foundation', 'beauty', 800.00, '30ml', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Liquid foundation'),
(50, 'Mascara', 'beauty', 400.00, '1 piece', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Waterproof mascara'),
(51, 'Face Wash', 'beauty', 200.00, '150ml', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Gentle face wash'),
(52, 'Moisturizer', 'beauty', 350.00, '100ml', 'https://images.unsplash.com/photo-1599599810694-b5ac4dd64b73?w=300&h=200&fit=crop', 'Daily moisturizer');

-- Insert test users for login testing
INSERT IGNORE INTO users (name, email, password, phone, address, role, is_verified) VALUES 
('Test User', 'test@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '1234567890', 'Test Address', 'USER', true),
('Admin User', 'admin@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '9876543210', 'Admin Address', 'ADMIN', true);

-- Verify insertion
SELECT COUNT(*) as total_products FROM products;
SELECT category, COUNT(*) as count FROM products GROUP BY category;
SELECT COUNT(*) as total_users FROM users;
