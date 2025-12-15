-- Increase image_url column size to handle longer URLs
ALTER TABLE products MODIFY COLUMN image_url TEXT;

-- Disable safe update mode
SET SQL_SAFE_UPDATES = 0;

-- Update image URLs for missing product images with your custom URL
UPDATE products SET image_url = 'YOUR_IMAGE_URL_HERE'
WHERE name IN ('Papaya','Guava','Kiwi','Pomegranate','Cucumber','Cabbage','Garlic','Ginger','Curd','Condensed Milk','Mozzarella','Cottage Cheese','Donuts','Bagels','Biscuits','Brownies','Cheesecake','Waffle','USB Cable','Phone Case','Screen Protector','Charger','Building Blocks','Puzzle','Remote Control Car','Action Figure','Board Game','Coloring Book','Toy Train','Shampoo','Face Wash','Moisturizer');

-- Remove Test Product
DELETE FROM products WHERE name = 'Test Product';

-- Re-enable safe update mode
SET SQL_SAFE_UPDATES = 1;