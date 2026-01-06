-- ==============================================================================
-- DAM Framework - Sample Data Insert Script
-- File: 02_insert_sample_data.sql
-- Purpose: Insert sample data for framework testing
-- Data Size: ~50 records (small scale for testing)
-- ==============================================================================

USE employee_db;

-- ==============================================================================
-- Insert Departments (5 departments)
-- ==============================================================================

INSERT INTO departments (name, code, location, budget) VALUES
('Engineering', 'ENG', 'Building A, Floor 3', 500000.00),
('Marketing', 'MKT', 'Building B, Floor 1', 200000.00),
('Human Resources', 'HR', 'Building A, Floor 1', 150000.00),
('Finance', 'FIN', 'Building B, Floor 2', 300000.00),
('Sales', 'SAL', 'Building C, Floor 1', 400000.00);

-- ==============================================================================
-- Insert Employee Profiles (20 profiles)
-- ==============================================================================

INSERT INTO employee_profiles (bio, address, phone, emergency_contact, emergency_phone) VALUES
('Experienced software engineer with 10+ years', '123 Main St, Hanoi', '0901234567', 'Nguyen Van A', '0912345678'),
('Senior backend developer', '456 Oak Ave, Hanoi', '0902234567', 'Tran Thi B', '0922345678'),
('Frontend specialist with React expertise', '789 Pine Rd, Hanoi', '0903234567', 'Le Van C', '0932345678'),
('Full-stack developer', '321 Elm St, HCMC', '0904234567', 'Pham Thi D', '0942345678'),
('DevOps engineer', '654 Maple Dr, Hanoi', '0905234567', 'Hoang Van E', '0952345678'),
('Marketing manager with 8 years experience', '111 Lake St, Hanoi', '0906234567', 'Vu Thi F', '0962345678'),
('Digital marketing specialist', '222 River Ave, HCMC', '0907234567', 'Dang Van G', '0972345678'),
('Content creator and strategist', '333 Hill Rd, Da Nang', '0908234567', 'Bui Thi H', '0982345678'),
('HR manager with talent acquisition focus', '444 Valley Dr, Hanoi', '0909234567', 'Do Van I', '0992345678'),
('HR specialist in employee relations', '555 Mountain St, Hanoi', '0910234567', 'Ngo Thi J', '0903345678'),
('Senior accountant', '666 Forest Ave, HCMC', '0911234567', 'Ly Van K', '0913345678'),
('Financial analyst', '777 Beach Rd, Da Nang', '0912234567', 'Cao Thi L', '0923345678'),
('Budget controller', '888 Park Dr, Hanoi', '0913234567', 'Truong Van M', '0933345678'),
('Senior sales executive', '999 Garden St, HCMC', '0914234567', 'Dinh Thi N', '0943345678'),
('Account manager', '1010 Plaza Ave, Hanoi', '0915234567', 'Vo Van O', '0953345678'),
('Junior developer', '1111 Square Rd, Hanoi', '0916234567', 'Phan Thi P', '0963345678'),
('QA Engineer', '1212 Circle Dr, HCMC', '0917234567', 'Duong Van Q', '0973345678'),
('Product manager', '1313 Star St, Da Nang', '0918234567', 'Lam Thi R', '0983345678'),
('UX Designer', '1414 Moon Ave, Hanoi', '0919234567', 'Tong Van S', '0993345678'),
('Data analyst', '1515 Sun Rd, HCMC', '0920234567', 'Ha Thi T', '0904345678');

-- ==============================================================================
-- Insert Employees (20 employees)
-- Test cases:
-- - Mix of departments (ENG: 8, MKT: 3, HR: 2, FIN: 3, SAL: 4)
-- - Salary range: 15M - 80M VND
-- - Hire dates: 2019-2024
-- ==============================================================================

INSERT INTO employees (first_name, last_name, email, phone, hire_date, salary, department_id, profile_id, is_active) VALUES
-- Engineering (8 employees)
('Nguyen', 'Van Anh', 'anh.nguyen@company.com', '0901234567', '2019-01-15', 80000.00, 1, 1, TRUE),
('Tran', 'Thi Binh', 'binh.tran@company.com', '0902234567', '2019-06-20', 75000.00, 1, 2, TRUE),
('Le', 'Van Cuong', 'cuong.le@company.com', '0903234567', '2020-03-10', 60000.00, 1, 3, TRUE),
('Pham', 'Thi Dung', 'dung.pham@company.com', '0904234567', '2020-08-15', 55000.00, 1, 4, TRUE),
('Hoang', 'Van En', 'en.hoang@company.com', '0905234567', '2021-02-01', 50000.00, 1, 5, TRUE),
('Vu', 'Thi Phuong', 'phuong.vu@company.com', '0916234567', '2022-05-10', 40000.00, 1, 16, TRUE),
('Duong', 'Van Quan', 'quan.duong@company.com', '0917234567', '2023-01-15', 35000.00, 1, 17, TRUE),
('Tong', 'Van Sang', 'sang.tong@company.com', '0919234567', '2023-08-20', 38000.00, 1, 19, TRUE),

-- Marketing (3 employees)
('Vu', 'Thi Giang', 'giang.vu@company.com', '0906234567', '2019-09-01', 55000.00, 2, 6, TRUE),
('Dang', 'Van Hao', 'hao.dang@company.com', '0907234567', '2020-11-15', 45000.00, 2, 7, TRUE),
('Bui', 'Thi Huong', 'huong.bui@company.com', '0908234567', '2021-04-20', 40000.00, 2, 8, TRUE),

-- Human Resources (2 employees)
('Do', 'Van Minh', 'minh.do@company.com', '0909234567', '2018-07-10', 60000.00, 3, 9, TRUE),
('Ngo', 'Thi Lan', 'lan.ngo@company.com', '0910234567', '2020-01-05', 45000.00, 3, 10, TRUE),

-- Finance (3 employees)
('Ly', 'Van Khoa', 'khoa.ly@company.com', '0911234567', '2019-03-12', 65000.00, 4, 11, TRUE),
('Cao', 'Thi Linh', 'linh.cao@company.com', '0912234567', '2020-05-18', 50000.00, 4, 12, TRUE),
('Truong', 'Van Nam', 'nam.truong@company.com', '0913234567', '2021-09-25', 48000.00, 4, 13, TRUE),

-- Sales (4 employees)
('Dinh', 'Thi Nga', 'nga.dinh@company.com', '0914234567', '2019-02-14', 70000.00, 5, 14, TRUE),
('Vo', 'Van Oanh', 'oanh.vo@company.com', '0915234567', '2020-06-30', 60000.00, 5, 15, TRUE),
('Lam', 'Thi Quynh', 'quynh.lam@company.com', '0918234567', '2022-03-08', 45000.00, 5, 18, TRUE),
('Ha', 'Thi Thao', 'thao.ha@company.com', '0920234567', '2023-11-01', 42000.00, 5, 20, TRUE);

-- ==============================================================================
-- Insert Projects (8 projects)
-- ==============================================================================

INSERT INTO projects (name, description, start_date, end_date, budget, status) VALUES
('E-commerce Platform', 'Build new online shopping platform', '2023-01-01', '2023-12-31', 500000.00, 'IN_PROGRESS'),
('Mobile App Development', 'iOS and Android app for customers', '2023-03-15', '2024-03-15', 300000.00, 'IN_PROGRESS'),
('Data Warehouse Migration', 'Migrate legacy data to new system', '2023-06-01', '2024-01-31', 200000.00, 'IN_PROGRESS'),
('Marketing Campaign Q4', 'Year-end marketing initiatives', '2023-10-01', '2023-12-31', 150000.00, 'COMPLETED'),
('HR System Upgrade', 'Upgrade employee management system', '2023-02-01', '2023-08-31', 100000.00, 'COMPLETED'),
('Financial Audit 2023', 'Annual financial review', '2023-01-15', '2023-03-31', 50000.00, 'COMPLETED'),
('Sales CRM Implementation', 'Implement new CRM system', '2023-09-01', '2024-02-28', 250000.00, 'IN_PROGRESS'),
('Cloud Infrastructure Setup', 'Move to AWS cloud', '2024-01-01', '2024-06-30', 400000.00, 'PLANNING');

-- ==============================================================================
-- Insert Employee-Project Assignments
-- ==============================================================================

INSERT INTO employee_projects (employee_id, project_id, role, assigned_date) VALUES
-- E-commerce Platform (Project 1)
(1, 1, 'Tech Lead', '2023-01-01'),
(2, 1, 'Backend Developer', '2023-01-01'),
(3, 1, 'Frontend Developer', '2023-01-01'),
(4, 1, 'Full-stack Developer', '2023-01-15'),

-- Mobile App Development (Project 2)
(5, 2, 'DevOps Engineer', '2023-03-15'),
(6, 2, 'Junior Developer', '2023-03-15'),
(19, 2, 'UX Designer', '2023-03-15'),

-- Data Warehouse Migration (Project 3)
(1, 3, 'Senior Engineer', '2023-06-01'),
(7, 3, 'QA Engineer', '2023-06-01'),
(20, 3, 'Data Analyst', '2023-06-01'),

-- Marketing Campaign Q4 (Project 4)
(9, 4, 'Marketing Manager', '2023-10-01'),
(10, 4, 'Digital Marketer', '2023-10-01'),
(11, 4, 'Content Creator', '2023-10-01'),

-- HR System Upgrade (Project 5)
(12, 5, 'HR Manager', '2023-02-01'),
(13, 5, 'HR Specialist', '2023-02-01'),

-- Financial Audit 2023 (Project 6)
(14, 6, 'Senior Accountant', '2023-01-15'),
(15, 6, 'Financial Analyst', '2023-01-15'),
(16, 6, 'Budget Controller', '2023-01-15'),

-- Sales CRM Implementation (Project 7)
(17, 7, 'Sales Executive', '2023-09-01'),
(18, 7, 'Account Manager', '2023-09-01'),
(8, 7, 'Product Manager', '2023-09-01'),

-- Cloud Infrastructure Setup (Project 8)
(1, 8, 'Solution Architect', '2024-01-01'),
(5, 8, 'DevOps Lead', '2024-01-01');

-- ==============================================================================
-- Data Verification Queries
-- ==============================================================================

-- Verify all data inserted
SELECT 'Departments' as TableName, COUNT(*) as RecordCount FROM departments
UNION ALL
SELECT 'Employee Profiles', COUNT(*) FROM employee_profiles
UNION ALL
SELECT 'Employees', COUNT(*) FROM employees
UNION ALL
SELECT 'Projects', COUNT(*) FROM projects
UNION ALL
SELECT 'Employee-Project Assignments', COUNT(*) FROM employee_projects;

-- ==============================================================================
-- Summary Statistics
-- ==============================================================================

SELECT 
    d.name as Department,
    COUNT(e.id) as EmployeeCount,
    AVG(e.salary) as AvgSalary,
    MIN(e.salary) as MinSalary,
    MAX(e.salary) as MaxSalary
FROM departments d
LEFT JOIN employees e ON d.id = e.department_id
GROUP BY d.id, d.name
ORDER BY d.name;

-- ==============================================================================
-- END OF SCRIPT
-- Total Records:
-- - Departments: 5
-- - Employee Profiles: 20
-- - Employees: 20
-- - Projects: 8
-- - Employee-Project Assignments: 24
-- ==============================================================================
