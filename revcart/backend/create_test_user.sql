-- Create test user for login testing
USE revcart_db;

-- Insert test user with encoded password
-- Password hash for 'secret'
INSERT INTO users (name, email, password, phone, address, role, is_verified) VALUES 
('Test User', 'test@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '1234567890', 'Test Address', 'USER', true)
ON DUPLICATE KEY UPDATE 
password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.',
is_verified = true;

-- Also create an admin user for testing
INSERT INTO users (name, email, password, phone, address, role, is_verified) VALUES 
('Admin User', 'admin@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '9876543210', 'Admin Address', 'ADMIN', true)
ON DUPLICATE KEY UPDATE 
password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.',
is_verified = true;

-- The above password hash corresponds to 'secret' 
-- For testing, you can use:
-- Email: test@example.com
-- Password: secret

SELECT 'Test user created successfully' as status;