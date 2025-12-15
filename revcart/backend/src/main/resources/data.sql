-- Insert additional coupons
INSERT IGNORE INTO coupons (code, discount_percentage, min_order_amount, max_uses, used_count, active, created_at) VALUES
('WELCOME10', 10, 500.00, 100, 0, true, NOW()),
('SAVE20', 20, 1000.00, 50, 0, true, NOW()),
('MEGA30', 30, 2000.00, 25, 0, true, NOW()),
('FIRST15', 15, 300.00, 200, 0, true, NOW()),
('BULK25', 25, 1500.00, 30, 0, true, NOW()),
('STUDENT5', 5, 200.00, 500, 0, true, NOW()),
('WEEKEND12', 12, 800.00, 75, 0, true, NOW()),
('FLASH40', 40, 3000.00, 10, 0, true, NOW()),
('LOYALTY18', 18, 600.00, 100, 0, true, NOW()),
('NEWUSER8', 8, 400.00, 150, 0, true, NOW());