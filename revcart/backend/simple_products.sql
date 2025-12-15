-- Simple Product Insert for RevCart Database
USE revcart_db;

-- Clear existing products
DELETE FROM products;

-- Insert products using existing table structure
INSERT INTO products (name, description, price, category, image, unit) VALUES
('Fresh Apples', 'Fresh red apples', 120.00, 'fruits', 'https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=300&h=200&fit=crop', '1kg'),
('Bananas', 'Ripe yellow bananas', 60.00, 'fruits', 'https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=300&h=200&fit=crop', '1kg'),
('Oranges', 'Juicy oranges', 100.00, 'fruits', 'https://images.unsplash.com/photo-1547514701-42782101795e?w=300&h=200&fit=crop', '1kg'),
('Grapes', 'Sweet grapes', 150.00, 'fruits', 'https://images.unsplash.com/photo-1537640538966-79f369143f8f?w=300&h=200&fit=crop', '500g'),
('Strawberries', 'Fresh strawberries', 200.00, 'fruits', 'https://images.unsplash.com/photo-1518635017498-87f514b751ba?w=300&h=200&fit=crop', '250g'),
('Mangoes', 'Sweet mangoes', 180.00, 'fruits', 'https://www.metropolisindia.com/upgrade/blog/upload/25/05/benefits-of-mangoes1747828357.webp', '1kg'),
('Carrots', 'Fresh orange carrots', 40.00, 'vegetables', 'https://images.unsplash.com/photo-1445282768818-728615cc910a?w=300&h=200&fit=crop', '500g'),
('Tomatoes', 'Fresh red tomatoes', 80.00, 'vegetables', 'https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=300&h=200&fit=crop', '1kg'),
('Onions', 'Fresh onions', 30.00, 'vegetables', 'https://images.unsplash.com/photo-1508747703725-719777637510?w=300&h=200&fit=crop', '1kg'),
('Potatoes', 'Fresh potatoes', 25.00, 'vegetables', 'https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=300&h=200&fit=crop', '1kg'),
('Broccoli', 'Fresh broccoli', 60.00, 'vegetables', 'https://images.unsplash.com/photo-1628773822503-930a7eaecf80?w=300&h=200&fit=crop', '500g'),
('Spinach', 'Fresh spinach leaves', 35.00, 'vegetables', 'https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=300&h=200&fit=crop', '250g'),
('Milk', 'Fresh cow milk', 55.00, 'dairy', 'https://nutritionsource.hsph.harvard.edu/wp-content/uploads/2024/11/AdobeStock_354060824-1024x683.jpeg', '1L'),
('Cheese', 'Fresh cheese', 200.00, 'dairy', 'https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=300&h=200&fit=crop', '250g'),
('Yogurt', 'Greek yogurt', 45.00, 'dairy', 'https://img.freepik.com/free-vector/realistic-vector-icon-illustration-strawberry-yoghurt-jar-with-spoon-full-yogurt-isolated_134830-2521.jpg', '500g'),
('Butter', 'Fresh butter', 120.00, 'dairy', 'https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d?w=300&h=200&fit=crop', '200g'),
('Paneer', 'Fresh paneer', 180.00, 'dairy', 'https://chennaionlineshopping.in/image/cache/catalog/Products/panner/amul%20panner-800x800.jpg', '250g'),
('Bread', 'Whole wheat bread', 35.00, 'bakery', 'https://assets.bonappetit.com/photos/5c62e4a3e81bbf522a9579ce/1:1/pass/milk-bread.jpg', '1 loaf'),
('Croissant', 'Buttery croissant', 25.00, 'bakery', 'https://sugargeekshow.com/wp-content/uploads/2022/11/croissants_featured.jpg', '1 piece'),
('Muffins', 'Blueberry muffins', 80.00, 'bakery', 'https://images.unsplash.com/photo-1607958996333-41aef7caefaa?w=300&h=200&fit=crop', '4 pieces'),
('Cookies', 'Chocolate cookies', 60.00, 'bakery', 'https://images.unsplash.com/photo-1499636136210-6f4ee915583e?w=300&h=200&fit=crop', '6 pieces'),
('Cake', 'Chocolate cake', 400.00, 'bakery', 'https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=300&h=200&fit=crop', '1 piece'),
('Smartphone', 'Latest smartphone', 15000.00, 'electronics', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=300&h=200&fit=crop', '1 piece'),
('Headphones', 'Wireless headphones', 2500.00, 'electronics', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=300&h=200&fit=crop', '1 piece'),
('Laptop', 'Gaming laptop', 45000.00, 'electronics', 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=300&h=200&fit=crop', '1 piece'),
('Power Bank', '10000mAh power bank', 1500.00, 'electronics', 'https://i03.appmifile.com/333_item_in/08/07/2025/9047b35e12fa25cb45fc93a824a29e87.jpg', '1 piece'),
('Football', 'Professional football', 800.00, 'sports', 'https://images.unsplash.com/photo-1486286701208-1d58e9338013?w=300&h=200&fit=crop', '1 piece'),
('Cricket Bat', 'Professional cricket bat', 1200.00, 'sports', 'https://dkpcricketonline.com/cdn/shop/collections/image_419d887e-bcd5-4469-9925-776dc84db52b.heic', '1 piece'),
('Basketball', 'Professional basketball', 900.00, 'sports', 'https://static.nbastore.in/resized/900X900/1180/wilson-nba-mens-drv-pro-basketball-brown-brown-68dc39e5a64de.jpg', '1 piece'),
('Teddy Bear', 'Soft teddy bear', 500.00, 'kids', 'https://tse1.mm.bing.net/th/id/OIP.IQUsCBaKM8Ox51lI1XH5BAHaFR?pid=Api&P=0&h=180', '1 piece'),
('Building Blocks', 'Educational building blocks', 800.00, 'kids', 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=300&h=200&fit=crop', '1 set'),
('Face Cream', 'Anti-aging face cream', 800.00, 'beauty', 'https://images.unsplash.com/photo-1556228720-195a672e8a03?w=300&h=200&fit=crop', '50ml'),
('Lipstick', 'Matte lipstick', 400.00, 'beauty', 'https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=300&h=200&fit=crop', '1 piece');

SELECT 'Products inserted successfully' as status;
SELECT COUNT(*) as total_products FROM products;