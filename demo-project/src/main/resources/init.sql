-- =====================================================
-- DAM Framework Demo - H2 Database Init Script
-- =====================================================

-- Categories table
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products table
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    price DECIMAL(12, 2) NOT NULL,
    stock INT DEFAULT 0,
    category_id BIGINT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

-- Customers table
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders table
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(12, 2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

-- =====================================================
-- Insert Sample Data
-- =====================================================

-- Categories
INSERT INTO categories (name, description) VALUES 
    ('Electronics', 'Electronic devices and gadgets'),
    ('Clothing', 'Fashion and apparel'),
    ('Books', 'Books and publications'),
    ('Food & Beverages', 'Food items and drinks'),
    ('Home & Garden', 'Home decoration and garden tools');

-- Products
INSERT INTO products (name, price, stock, category_id, description) VALUES 
    ('iPhone 15 Pro', 1199.99, 50, 1, 'Latest Apple smartphone with A17 Pro chip'),
    ('Samsung Galaxy S24', 999.99, 75, 1, 'Samsung flagship phone with AI features'),
    ('MacBook Pro 14"', 1999.99, 30, 1, 'Apple laptop with M3 Pro chip'),
    ('Dell XPS 15', 1599.99, 40, 1, 'Premium Windows laptop'),
    ('Sony WH-1000XM5', 349.99, 100, 1, 'Noise-cancelling wireless headphones'),
    ('Nike Air Max', 159.99, 200, 2, 'Classic running shoes'),
    ('Levis 501 Jeans', 79.99, 150, 2, 'Original fit jeans'),
    ('The Pragmatic Programmer', 49.99, 80, 3, 'Software development book'),
    ('Clean Code', 39.99, 120, 3, 'A handbook of agile software craftsmanship'),
    ('Organic Coffee Beans', 24.99, 300, 4, '1kg premium arabica coffee'),
    ('Green Tea Collection', 19.99, 250, 4, 'Assorted green tea varieties'),
    ('Garden Tool Set', 89.99, 60, 5, '5-piece gardening tools'),
    ('LED Desk Lamp', 45.99, 90, 5, 'Adjustable brightness desk lamp');

-- Customers
INSERT INTO customers (name, email, phone, address) VALUES 
    ('John Doe', 'john.doe@email.com', '0901234567', '123 Main St, City'),
    ('Jane Smith', 'jane.smith@email.com', '0912345678', '456 Oak Ave, Town'),
    ('Bob Wilson', 'bob.wilson@email.com', '0923456789', '789 Pine Rd, Village'),
    ('Alice Brown', 'alice.brown@email.com', '0934567890', '321 Elm St, Metro'),
    ('Charlie Davis', 'charlie.davis@email.com', '0945678901', '654 Cedar Ln, District');

-- Orders
INSERT INTO orders (customer_id, total_amount, status) VALUES 
    (1, 1549.98, 'COMPLETED'),
    (2, 199.98, 'PENDING'),
    (3, 2089.98, 'SHIPPED'),
    (1, 64.98, 'COMPLETED'),
    (4, 1199.99, 'PROCESSING');
