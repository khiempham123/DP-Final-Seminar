-- =====================================================
-- DAM Framework Demo - Database Setup Script
-- =====================================================
-- Run this script in MySQL to create the demo database
-- =====================================================

-- Create database
CREATE DATABASE IF NOT EXISTS dam_demo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dam_demo;

-- =====================================================
-- Drop existing tables
-- =====================================================
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS customers;

-- =====================================================
-- Categories table
-- =====================================================
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- Products table
-- =====================================================
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    price DECIMAL(12, 2) NOT NULL,
    stock INT DEFAULT 0,
    category_id BIGINT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- Customers table
-- =====================================================
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- Orders table
-- =====================================================
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(12, 2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- Order Items table
-- =====================================================
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(12, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
    ('Laptop Dell XPS 15', 1299.99, 50, 1, 'High-performance laptop'),
    ('iPhone 15 Pro', 999.99, 100, 1, 'Latest Apple smartphone'),
    ('Samsung TV 55 inch', 699.99, 30, 1, '4K Smart TV'),
    ('T-Shirt Cotton', 29.99, 200, 2, 'Comfortable cotton t-shirt'),
    ('Jeans Levi 501', 79.99, 150, 2, 'Classic jeans'),
    ('Winter Jacket', 149.99, 80, 2, 'Warm winter jacket'),
    ('Java Programming Book', 49.99, 100, 3, 'Learn Java programming'),
    ('Design Patterns Book', 59.99, 75, 3, 'GoF Design Patterns'),
    ('Clean Code Book', 44.99, 90, 3, 'Clean code principles'),
    ('Organic Coffee', 19.99, 300, 4, 'Premium organic coffee beans'),
    ('Green Tea', 12.99, 250, 4, 'Japanese green tea'),
    ('Garden Tools Set', 89.99, 40, 5, 'Complete garden tools set'),
    ('Flower Pot', 24.99, 100, 5, 'Decorative flower pot');

-- Customers
INSERT INTO customers (name, email, phone, address) VALUES 
    ('John Doe', 'john.doe@email.com', '0901234567', '123 Main Street, City A'),
    ('Jane Smith', 'jane.smith@email.com', '0912345678', '456 Oak Avenue, City B'),
    ('Bob Wilson', 'bob.wilson@email.com', '0923456789', '789 Pine Road, City C'),
    ('Alice Brown', 'alice.brown@email.com', '0934567890', '321 Elm Street, City D'),
    ('Charlie Davis', 'charlie.davis@email.com', '0945678901', '654 Maple Lane, City E');

-- Orders
INSERT INTO orders (customer_id, total_amount, status) VALUES 
    (1, 1349.98, 'COMPLETED'),
    (2, 109.98, 'COMPLETED'),
    (1, 699.99, 'PENDING'),
    (3, 154.97, 'PROCESSING'),
    (4, 79.99, 'COMPLETED');

-- Order Items
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES 
    (1, 1, 1, 1299.99),
    (1, 10, 2, 19.99),
    (2, 4, 2, 29.99),
    (2, 7, 1, 49.99),
    (3, 3, 1, 699.99),
    (4, 7, 1, 49.99),
    (4, 8, 1, 59.99),
    (4, 9, 1, 44.99),
    (5, 5, 1, 79.99);

-- =====================================================
-- Verify Data
-- =====================================================
SELECT 'Categories:' AS '';
SELECT * FROM categories;

SELECT 'Products:' AS '';
SELECT p.id, p.name, p.price, p.stock, c.name as category FROM products p LEFT JOIN categories c ON p.category_id = c.id;

SELECT 'Customers:' AS '';
SELECT * FROM customers;

SELECT 'Orders:' AS '';
SELECT o.id, c.name as customer, o.total_amount, o.status, o.order_date FROM orders o JOIN customers c ON o.customer_id = c.id;

SELECT 'Order Items:' AS '';
SELECT oi.id, o.id as order_id, p.name as product, oi.quantity, oi.unit_price 
FROM order_items oi 
JOIN orders o ON oi.order_id = o.id 
JOIN products p ON oi.product_id = p.id;

SELECT '=== Database setup completed successfully! ===' AS '';
