USE revcart_db;

INSERT INTO products (name, description, price, category, image, stock_quantity, unit) VALUES
-- Fruits
('Papaya', 'Sweet papaya', 80.00, 'fruits', '', 100, '1kg'),
('Guava', 'Fresh guava', 90.00, 'fruits', '', 100, '500g'),
('Kiwi', 'Fresh kiwi fruit', 250.00, 'fruits', '', 100, '250g'),
('Pomegranate', 'Juicy pomegranate', 180.00, 'fruits', '', 100, '500g'),

-- Vegetables
('Cucumber', 'Fresh cucumber', 35.00, 'vegetables', '', 100, '500g'),
('Cabbage', 'Fresh cabbage', 25.00, 'vegetables', '', 100, '1 piece'),
('Garlic', 'Fresh garlic', 120.00, 'vegetables', '', 100, '250g'),
('Ginger', 'Fresh ginger', 80.00, 'vegetables', '', 100, '250g'),

-- Dairy
('Curd', 'Fresh curd', 40.00, 'dairy', '', 100, '500g'),
('Condensed Milk', 'Sweet condensed milk', 85.00, 'dairy', '', 100, '400g'),
('Mozzarella', 'Fresh mozzarella cheese', 220.00, 'dairy', '', 100, '200g'),
('Cottage Cheese', 'Fresh cottage cheese', 160.00, 'dairy', '', 100, '250g'),

-- Bakery
('Biscuits', 'Assorted biscuits', 45.00, 'bakery', '', 100, '200g'),
('Brownies', 'Chocolate brownies', 150.00, 'bakery', '', 100, '4 pieces'),
('Cheesecake', 'Creamy cheesecake', 350.00, 'bakery', '', 100, '1 piece'),
('Waffle', 'Belgian waffle', 80.00, 'bakery', '', 100, '2 pieces'),

-- Electronics
('USB Cable', 'Type-C USB cable', 200.00, 'electronics', '', 100, '1 piece'),
('Screen Protector', 'Tempered glass protector', 150.00, 'electronics', '', 100, '1 piece'),
('Phone Case', 'Protective phone case', 300.00, 'electronics', '', 100, '1 piece'),

-- Sports
('Swimming Goggles', 'Anti-fog swimming goggles', 350.00, 'sports', '', 100, '1 piece'),
('Boxing Gloves', 'Professional boxing gloves', 1800.00, 'sports', '', 100, '1 pair'),

-- Kids/Toys
('Teddy Bear', 'Soft teddy bear', 500.00, 'kids', '', 100, '1 piece'),
('Building Blocks', 'Educational building blocks', 800.00, 'kids', '', 100, '1 set'),
('Remote Control Car', 'RC racing car', 1200.00, 'kids', '', 100, '1 piece'),
('Action Figure', 'Superhero action figure', 400.00, 'kids', '', 100, '1 piece'),
('Board Game', 'Family board game', 900.00, 'kids', '', 100, '1 piece'),
('Coloring Book', 'Kids coloring book', 80.00, 'kids', '', 100, '1 piece'),
('Toy Train', 'Electric toy train', 1500.00, 'kids', '', 100, '1 set'),

-- Beauty
('Perfume', 'Luxury perfume', 1500.00, 'beauty', '', 100, '50ml'),
('Foundation', 'Liquid foundation', 600.00, 'beauty', '', 100, '30ml'),
('Mascara', 'Waterproof mascara', 350.00, 'beauty', '', 100, '1 piece'),
('Face Wash', 'Gentle face wash', 250.00, 'beauty', '', 100, '150ml'),
('Moisturizer', 'Daily moisturizer', 400.00, 'beauty', '', 100, '100ml');