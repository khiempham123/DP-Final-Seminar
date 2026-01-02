-- Database setup script for DAM Framework
-- This script creates a sample database and tables for testing

-- =====================================
-- MySQL Setup
-- =====================================

-- Create database
CREATE DATABASE IF NOT EXISTS dam_test;
USE dam_test;

-- Create users table
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    age INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert sample data
INSERT INTO users (username, email, age) VALUES
('john_doe', 'john@example.com', 25),
('jane_smith', 'jane@example.com', 30),
('bob_wilson', 'bob@example.com', 22),
('alice_brown', 'alice@example.com', 28);

-- Create products table (for relationship testing)
DROP TABLE IF EXISTS products;
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0,
    category VARCHAR(50)
);

-- Insert sample products
INSERT INTO products (name, price, stock, category) VALUES
('Laptop', 999.99, 10, 'Electronics'),
('Mouse', 29.99, 50, 'Electronics'),
('Keyboard', 79.99, 30, 'Electronics'),
('Chair', 199.99, 15, 'Furniture'),
('Desk', 349.99, 8, 'Furniture');

-- =====================================
-- PostgreSQL Setup
-- =====================================

/*
-- Run these commands in PostgreSQL

CREATE DATABASE dam_test;
\c dam_test

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    age INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, email, age) VALUES
('john_doe', 'john@example.com', 25),
('jane_smith', 'jane@example.com', 30),
('bob_wilson', 'bob@example.com', 22),
('alice_brown', 'alice@example.com', 28);

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INTEGER DEFAULT 0,
    category VARCHAR(50)
);

INSERT INTO products (name, price, stock, category) VALUES
('Laptop', 999.99, 10, 'Electronics'),
('Mouse', 29.99, 50, 'Electronics'),
('Keyboard', 79.99, 30, 'Electronics'),
('Chair', 199.99, 15, 'Furniture'),
('Desk', 349.99, 8, 'Furniture');
*/

-- =====================================
-- SQL Server Setup
-- =====================================

/*
-- Run these commands in SQL Server

CREATE DATABASE dam_test;
GO

USE dam_test;
GO

CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL,
    age INT,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

INSERT INTO users (username, email, age) VALUES
('john_doe', 'john@example.com', 25),
('jane_smith', 'jane@example.com', 30),
('bob_wilson', 'bob@example.com', 22),
('alice_brown', 'alice@example.com', 28);

CREATE TABLE products (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(200) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0,
    category NVARCHAR(50)
);

INSERT INTO products (name, price, stock, category) VALUES
('Laptop', 999.99, 10, 'Electronics'),
('Mouse', 29.99, 50, 'Electronics'),
('Keyboard', 79.99, 30, 'Electronics'),
('Chair', 199.99, 15, 'Furniture'),
('Desk', 349.99, 8, 'Furniture');
*/

-- =====================================
-- Verification Queries
-- =====================================

-- Check data
SELECT * FROM users;
SELECT * FROM products;

-- Test queries that the framework should support
SELECT * FROM users WHERE age > 25;
SELECT * FROM users WHERE username LIKE '%john%';
SELECT age, COUNT(*) as count FROM users GROUP BY age;
SELECT age, COUNT(*) as count FROM users GROUP BY age HAVING COUNT(*) > 1;
SELECT * FROM users ORDER BY username ASC LIMIT 2;
