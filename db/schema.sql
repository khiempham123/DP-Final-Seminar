-- DAM Framework - Database Schema
-- This file contains SQL scripts to create the test database

-- ============================================
-- MySQL Database Setup
-- ============================================

-- Create database
CREATE DATABASE IF NOT EXISTS dam_test CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dam_test;

-- Drop existing tables (in correct order due to foreign keys)
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS employee_profiles;
DROP TABLE IF EXISTS departments;
DROP TABLE IF EXISTS users;

-- ============================================
-- Basic Users table (for simple CRUD demo)
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    age INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Departments table (for OneToMany demo)
-- ============================================
CREATE TABLE IF NOT EXISTS departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Employee Profiles table (for OneToOne demo)
-- ============================================
CREATE TABLE IF NOT EXISTS employee_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    address VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    date_of_birth DATE,
    bio TEXT,
    linkedin_url VARCHAR(255),
    github_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Employees table (for ManyToOne demo)
-- ============================================
CREATE TABLE IF NOT EXISTS employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    hire_date DATE,
    salary DECIMAL(12,2),
    department_id BIGINT,
    profile_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_email (email),
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL,
    FOREIGN KEY (profile_id) REFERENCES employee_profiles(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Sample Data
-- ============================================

-- Users
INSERT INTO users (username, email, age) VALUES 
    ('admin', 'admin@example.com', 30),
    ('user1', 'user1@example.com', 25),
    ('user2', 'user2@example.com', 22),
    ('test_user', 'test@test.com', 28);

-- Departments
INSERT INTO departments (name, code, description) VALUES 
    ('Engineering', 'ENG', 'Software Engineering Department'),
    ('Human Resources', 'HR', 'Human Resources Department'),
    ('Marketing', 'MKT', 'Marketing and Sales Department'),
    ('Finance', 'FIN', 'Finance and Accounting Department');

-- Employee Profiles
INSERT INTO employee_profiles (address, city, country, postal_code, date_of_birth, bio) VALUES 
    ('123 Main St', 'New York', 'USA', '10001', '1990-05-15', 'Senior Software Engineer'),
    ('456 Oak Ave', 'San Francisco', 'USA', '94102', '1988-08-20', 'HR Manager'),
    ('789 Pine Rd', 'Los Angeles', 'USA', '90001', '1995-03-10', 'Marketing Specialist');

-- Employees
INSERT INTO employees (first_name, last_name, email, phone, hire_date, salary, department_id, profile_id) VALUES 
    ('John', 'Doe', 'john.doe@company.com', '555-0101', '2020-01-15', 85000.00, 1, 1),
    ('Jane', 'Smith', 'jane.smith@company.com', '555-0102', '2019-06-01', 75000.00, 2, 2),
    ('Bob', 'Johnson', 'bob.johnson@company.com', '555-0103', '2021-03-20', 65000.00, 3, 3),
    ('Alice', 'Williams', 'alice.williams@company.com', '555-0104', '2022-01-10', 90000.00, 1, NULL),
    ('Charlie', 'Brown', 'charlie.brown@company.com', '555-0105', '2021-09-05', 70000.00, 1, NULL);

-- ============================================
-- PostgreSQL Database Setup  
-- ============================================
/*
CREATE DATABASE dam_test;

\c dam_test;

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    age INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS departments (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS employee_profiles (
    id BIGSERIAL PRIMARY KEY,
    address VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    date_of_birth DATE,
    bio TEXT,
    linkedin_url VARCHAR(255),
    github_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS employees (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    hire_date DATE,
    salary DECIMAL(12,2),
    department_id BIGINT REFERENCES departments(id) ON DELETE SET NULL,
    profile_id BIGINT REFERENCES employee_profiles(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
*/

-- ============================================
-- SQL Server Database Setup
-- ============================================
/*
CREATE DATABASE dam_test;
GO

USE dam_test;
GO

CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(100) NOT NULL UNIQUE,
    email NVARCHAR(255) NOT NULL UNIQUE,
    age INT,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);

CREATE TABLE departments (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    code NVARCHAR(20) NOT NULL UNIQUE,
    description NVARCHAR(500),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);

CREATE TABLE employee_profiles (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    address NVARCHAR(255),
    city NVARCHAR(100),
    country NVARCHAR(100),
    postal_code NVARCHAR(20),
    date_of_birth DATE,
    bio NVARCHAR(MAX),
    linkedin_url NVARCHAR(255),
    github_url NVARCHAR(255),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);

CREATE TABLE employees (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    first_name NVARCHAR(100) NOT NULL,
    last_name NVARCHAR(100) NOT NULL,
    email NVARCHAR(255) NOT NULL UNIQUE,
    phone NVARCHAR(20),
    hire_date DATE,
    salary DECIMAL(12,2),
    department_id BIGINT FOREIGN KEY REFERENCES departments(id) ON DELETE SET NULL,
    profile_id BIGINT FOREIGN KEY REFERENCES employee_profiles(id) ON DELETE SET NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);
GO
*/
