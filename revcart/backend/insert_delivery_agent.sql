USE revcart_db;

-- Create delivery_agents table if it doesn't exist
CREATE TABLE IF NOT EXISTS delivery_agents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    status ENUM('AVAILABLE', 'BUSY', 'OFFLINE') DEFAULT 'AVAILABLE',
    is_verified BOOLEAN DEFAULT TRUE,
    otp VARCHAR(6),
    otp_expiry DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert test delivery agent with plain text password
INSERT IGNORE INTO delivery_agents (name, email, password, phone, status, is_verified) VALUES 
('John Delivery', 'agent@revcart.com', 'agent123', '9876543210', 'AVAILABLE', TRUE);

SELECT 'Delivery agent inserted successfully' as status;