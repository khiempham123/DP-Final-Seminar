-- ==============================================================================
-- DAM Framework - Database Creation Script
-- File: 01_create_database.sql
-- Purpose: Create test database for framework testing
-- Database: MySQL, PostgreSQL, SQL Server compatible
-- ==============================================================================

-- ==============================================================================
-- MYSQL VERSION
-- ==============================================================================

-- Drop and create database
DROP DATABASE IF EXISTS employee_db;
CREATE DATABASE employee_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE employee_db;

-- ==============================================================================
-- POSTGRESQL VERSION (Uncomment to use)
-- ==============================================================================
-- DROP DATABASE IF EXISTS employee_db;
-- CREATE DATABASE employee_db ENCODING 'UTF8';
-- \c employee_db;

-- ==============================================================================
-- SQL SERVER VERSION (Uncomment to use)
-- ==============================================================================
-- USE master;
-- GO
-- IF EXISTS(SELECT * FROM sys.databases WHERE name='employee_db')
-- DROP DATABASE employee_db;
-- GO
-- CREATE DATABASE employee_db;
-- GO
-- USE employee_db;
-- GO

-- ==============================================================================
-- Table: departments
-- Purpose: Store company departments (Parent table for employees)
-- ==============================================================================

CREATE TABLE departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) NOT NULL UNIQUE,
    location VARCHAR(200),
    budget DECIMAL(15, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_dept_code UNIQUE (code)
) ENGINE=InnoDB;

-- ==============================================================================
-- Table: employee_profiles
-- Purpose: Store employee personal profiles (OneToOne with employees)
-- ==============================================================================

CREATE TABLE employee_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bio TEXT,
    address VARCHAR(500),
    phone VARCHAR(20),
    emergency_contact VARCHAR(100),
    emergency_phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ==============================================================================
-- Table: employees
-- Purpose: Store employee information (Core table with relationships)
-- Relationships:
--   - ManyToOne with departments (many employees in one department)
--   - OneToOne with employee_profiles (one employee has one profile)
-- ==============================================================================

CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    hire_date DATE,
    salary DECIMAL(10, 2),
    department_id BIGINT,
    profile_id BIGINT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign Keys
    CONSTRAINT fk_emp_department FOREIGN KEY (department_id) 
        REFERENCES departments(id) ON DELETE SET NULL,
    CONSTRAINT fk_emp_profile FOREIGN KEY (profile_id) 
        REFERENCES employee_profiles(id) ON DELETE SET NULL,
    
    -- Indexes for performance
    INDEX idx_department_id (department_id),
    INDEX idx_hire_date (hire_date),
    INDEX idx_salary (salary)
) ENGINE=InnoDB;

-- ==============================================================================
-- Table: projects (Optional - for testing additional queries)
-- ==============================================================================

CREATE TABLE projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    budget DECIMAL(15, 2),
    status VARCHAR(20) DEFAULT 'PLANNING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ==============================================================================
-- Table: employee_projects (Many-to-Many relationship)
-- ==============================================================================

CREATE TABLE employee_projects (
    employee_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    role VARCHAR(100),
    assigned_date DATE,
    
    PRIMARY KEY (employee_id, project_id),
    CONSTRAINT fk_ep_employee FOREIGN KEY (employee_id) 
        REFERENCES employees(id) ON DELETE CASCADE,
    CONSTRAINT fk_ep_project FOREIGN KEY (project_id) 
        REFERENCES projects(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==============================================================================
-- Database Statistics View
-- ==============================================================================

CREATE VIEW v_database_stats AS
SELECT 
    'departments' as table_name, COUNT(*) as record_count FROM departments
UNION ALL
SELECT 'employees', COUNT(*) FROM employees
UNION ALL
SELECT 'employee_profiles', COUNT(*) FROM employee_profiles
UNION ALL
SELECT 'projects', COUNT(*) FROM projects;

-- ==============================================================================
-- Verification Query
-- ==============================================================================

-- Run this to verify tables created successfully
SELECT 
    table_name,
    table_rows
FROM 
    information_schema.tables
WHERE 
    table_schema = 'employee_db'
ORDER BY 
    table_name;

-- ==============================================================================
-- END OF SCRIPT
-- ==============================================================================
