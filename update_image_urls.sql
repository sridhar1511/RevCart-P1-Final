-- Disable safe update mode first
SET SQL_SAFE_UPDATES = 0;

-- Update image URLs for missing product images with working URLs
UPDATE products SET image_url = CASE 
    WHEN name = 'Papaya' THEN 'https://via.placeholder.com/300x300/FF6B35/FFFFFF?text=Papaya'
    WHEN name = 'Guava' THEN 'https://via.placeholder.com/300x300/8BC34A/FFFFFF?text=Guava'
    WHEN name = 'Kiwi' THEN 'https://via.placeholder.com/300x300/4CAF50/FFFFFF?text=Kiwi'
    WHEN name = 'Pomegranate' THEN 'https://via.placeholder.com/300x300/E91E63/FFFFFF?text=Pomegranate'
    WHEN name = 'Cucumber' THEN 'https://via.placeholder.com/300x300/4CAF50/FFFFFF?text=Cucumber'
    WHEN name = 'Cabbage' THEN 'https://via.placeholder.com/300x300/8BC34A/FFFFFF?text=Cabbage'
    WHEN name = 'Garlic' THEN 'https://via.placeholder.com/300x300/FFC107/000000?text=Garlic'
    WHEN name = 'Ginger' THEN 'https://via.placeholder.com/300x300/FF9800/FFFFFF?text=Ginger'
    WHEN name = 'Curd' THEN 'https://via.placeholder.com/300x300/FFFFFF/000000?text=Curd'
    WHEN name = 'Condensed Milk' THEN 'https://via.placeholder.com/300x300/FFF8E1/000000?text=Condensed+Milk'
    WHEN name = 'Mozzarella' THEN 'https://via.placeholder.com/300x300/FFFDE7/000000?text=Mozzarella'
    WHEN name = 'Cottage Cheese' THEN 'https://via.placeholder.com/300x300/F5F5F5/000000?text=Cottage+Cheese'
    WHEN name = 'Donuts' THEN 'https://via.placeholder.com/300x300/8D6E63/FFFFFF?text=Donuts'
    WHEN name = 'Bagels' THEN 'https://via.placeholder.com/300x300/D7CCC8/000000?text=Bagels'
    WHEN name = 'Biscuits' THEN 'https://via.placeholder.com/300x300/FFCC02/000000?text=Biscuits'
    WHEN name = 'Brownies' THEN 'https://via.placeholder.com/300x300/5D4037/FFFFFF?text=Brownies'
    WHEN name = 'Cheesecake' THEN 'https://via.placeholder.com/300x300/FFF3E0/000000?text=Cheesecake'
    WHEN name = 'Waffle' THEN 'https://via.placeholder.com/300x300/FFE0B2/000000?text=Waffle'
    WHEN name = 'USB Cable' THEN 'https://via.placeholder.com/300x300/424242/FFFFFF?text=USB+Cable'
    WHEN name = 'Phone Case' THEN 'https://via.placeholder.com/300x300/2196F3/FFFFFF?text=Phone+Case'
    WHEN name = 'Screen Protector' THEN 'https://via.placeholder.com/300x300/E3F2FD/000000?text=Screen+Protector'
    WHEN name = 'Charger' THEN 'https://via.placeholder.com/300x300/607D8B/FFFFFF?text=Charger'
    WHEN name = 'Building Blocks' THEN 'https://via.placeholder.com/300x300/F44336/FFFFFF?text=Building+Blocks'
    WHEN name = 'Puzzle' THEN 'https://via.placeholder.com/300x300/9C27B0/FFFFFF?text=Puzzle'
    WHEN name = 'Remote Control Car' THEN 'https://via.placeholder.com/300x300/FF5722/FFFFFF?text=RC+Car'
    WHEN name = 'Action Figure' THEN 'https://via.placeholder.com/300x300/3F51B5/FFFFFF?text=Action+Figure'
    WHEN name = 'Board Game' THEN 'https://via.placeholder.com/300x300/795548/FFFFFF?text=Board+Game'
    WHEN name = 'Coloring Book' THEN 'https://via.placeholder.com/300x300/FFEB3B/000000?text=Coloring+Book'
    WHEN name = 'Toy Train' THEN 'https://via.placeholder.com/300x300/009688/FFFFFF?text=Toy+Train'
    WHEN name = 'Shampoo' THEN 'https://via.placeholder.com/300x300/00BCD4/FFFFFF?text=Shampoo'
    WHEN name = 'Face Wash' THEN 'https://via.placeholder.com/300x300/4FC3F7/FFFFFF?text=Face+Wash'
    WHEN name = 'Moisturizer' THEN 'https://via.placeholder.com/300x300/81C784/FFFFFF?text=Moisturizer'
END
WHERE name IN ('Papaya','Guava','Kiwi','Pomegranate','Cucumber','Cabbage','Garlic','Ginger','Curd','Condensed Milk','Mozzarella','Cottage Cheese','Donuts','Bagels','Biscuits','Brownies','Cheesecake','Waffle','USB Cable','Phone Case','Screen Protector','Charger','Building Blocks','Puzzle','Remote Control Car','Action Figure','Board Game','Coloring Book','Toy Train','Shampoo','Face Wash','Moisturizer');

-- Remove Test Product
DELETE FROM products WHERE name = 'Test Product';

-- Re-enable safe update mode
SET SQL_SAFE_UPDATES = 1;