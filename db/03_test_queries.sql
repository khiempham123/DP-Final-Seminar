-- ==============================================================================
-- DAM Framework - Test Queries
-- File: 03_test_queries.sql
-- Purpose: SQL queries to test and demonstrate framework functionality
-- ==============================================================================

USE employee_db;

-- ==============================================================================
-- 1. BASIC CRUD OPERATIONS TEST
-- ==============================================================================

-- Test: Read all employees
SELECT * FROM employees;

-- Test: Read employee by ID
SELECT * FROM employees WHERE id = 1;

-- Test: Read employee with profile (OneToOne)
SELECT 
    e.id,
    e.first_name,
    e.last_name,
    e.email,
    ep.bio,
    ep.address,
    ep.phone
FROM employees e
LEFT JOIN employee_profiles ep ON e.profile_id = ep.id
WHERE e.id = 1;

-- Test: Read department with employees (OneToMany)
SELECT 
    d.name as DepartmentName,
    e.first_name,
    e.last_name,
    e.email
FROM departments d
LEFT JOIN employees e ON d.id = e.department_id
WHERE d.id = 1;

-- ==============================================================================
-- 2. WHERE CLAUSE TESTS (Basic Requirement)
-- ==============================================================================

-- Test: WHERE with single condition
SELECT * FROM employees WHERE salary > 50000;

-- Test: WHERE with multiple conditions (AND)
SELECT * FROM employees WHERE salary > 50000 AND department_id = 1;

-- Test: WHERE with OR condition
SELECT * FROM employees WHERE department_id = 1 OR department_id = 2;

-- Test: WHERE with LIKE
SELECT * FROM employees WHERE email LIKE '%nguyen%';

-- Test: WHERE with IN
SELECT * FROM employees WHERE department_id IN (1, 2, 3);

-- Test: WHERE with BETWEEN
SELECT * FROM employees WHERE salary BETWEEN 40000 AND 60000;

-- Test: WHERE with NULL check
SELECT * FROM employees WHERE profile_id IS NOT NULL;

-- ==============================================================================
-- 3. GROUP BY TESTS (Basic Requirement)
-- ==============================================================================

-- Test: GROUP BY with COUNT
SELECT 
    department_id,
    COUNT(*) as employee_count
FROM employees
GROUP BY department_id;

-- Test: GROUP BY with AVG
SELECT 
    department_id,
    AVG(salary) as avg_salary
FROM employees
GROUP BY department_id;

-- Test: GROUP BY with multiple aggregations
SELECT 
    department_id,
    COUNT(*) as count,
    AVG(salary) as avg_salary,
    MIN(salary) as min_salary,
    MAX(salary) as max_salary,
    SUM(salary) as total_salary
FROM employees
GROUP BY department_id;

-- Test: GROUP BY with JOIN
SELECT 
    d.name as department_name,
    COUNT(e.id) as employee_count,
    AVG(e.salary) as avg_salary
FROM departments d
LEFT JOIN employees e ON d.id = e.department_id
GROUP BY d.id, d.name;

-- ==============================================================================
-- 4. HAVING CLAUSE TESTS (Basic Requirement)
-- ==============================================================================

-- Test: HAVING with COUNT
SELECT 
    department_id,
    COUNT(*) as employee_count
FROM employees
GROUP BY department_id
HAVING COUNT(*) > 2;

-- Test: HAVING with AVG
SELECT 
    department_id,
    AVG(salary) as avg_salary
FROM employees
GROUP BY department_id
HAVING AVG(salary) > 50000;

-- Test: HAVING with multiple conditions
SELECT 
    department_id,
    COUNT(*) as employee_count,
    AVG(salary) as avg_salary
FROM employees
GROUP BY department_id
HAVING COUNT(*) > 2 AND AVG(salary) > 45000;

-- ==============================================================================
-- 5. ORDER BY TESTS
-- ==============================================================================

-- Test: ORDER BY single column ASC
SELECT * FROM employees ORDER BY salary ASC;

-- Test: ORDER BY single column DESC
SELECT * FROM employees ORDER BY salary DESC;

-- Test: ORDER BY multiple columns
SELECT * FROM employees ORDER BY department_id ASC, salary DESC;

-- Test: ORDER BY with WHERE
SELECT * FROM employees 
WHERE salary > 40000 
ORDER BY salary DESC;

-- ==============================================================================
-- 6. LIMIT/OFFSET TESTS (Pagination)
-- ==============================================================================

-- Test: LIMIT only
SELECT * FROM employees ORDER BY id LIMIT 5;

-- Test: LIMIT with OFFSET (page 1: records 1-5)
SELECT * FROM employees ORDER BY id LIMIT 5 OFFSET 0;

-- Test: LIMIT with OFFSET (page 2: records 6-10)
SELECT * FROM employees ORDER BY id LIMIT 5 OFFSET 5;

-- Test: LIMIT with OFFSET (page 3: records 11-15)
SELECT * FROM employees ORDER BY id LIMIT 5 OFFSET 10;

-- ==============================================================================
-- 7. COMPLEX QUERIES (Combining Multiple Clauses)
-- ==============================================================================

-- Test: WHERE + GROUP BY + HAVING + ORDER BY
SELECT 
    department_id,
    COUNT(*) as employee_count,
    AVG(salary) as avg_salary
FROM employees
WHERE is_active = TRUE
GROUP BY department_id
HAVING COUNT(*) >= 2
ORDER BY avg_salary DESC;

-- Test: WHERE + GROUP BY + HAVING + ORDER BY + LIMIT
SELECT 
    d.name as department_name,
    COUNT(e.id) as employee_count,
    AVG(e.salary) as avg_salary
FROM departments d
LEFT JOIN employees e ON d.id = e.department_id
WHERE e.is_active = TRUE
GROUP BY d.id, d.name
HAVING COUNT(e.id) > 0
ORDER BY avg_salary DESC
LIMIT 3;

-- ==============================================================================
-- 8. RELATIONSHIP TESTS
-- ==============================================================================

-- Test: OneToMany (Department -> Employees)
SELECT 
    d.id as dept_id,
    d.name as dept_name,
    e.id as emp_id,
    e.first_name,
    e.last_name
FROM departments d
LEFT JOIN employees e ON d.id = e.department_id
ORDER BY d.id, e.id;

-- Test: ManyToOne (Employee -> Department)
SELECT 
    e.id as emp_id,
    e.first_name,
    e.last_name,
    d.id as dept_id,
    d.name as dept_name
FROM employees e
LEFT JOIN departments d ON e.department_id = d.id
ORDER BY e.id;

-- Test: OneToOne (Employee -> EmployeeProfile)
SELECT 
    e.id as emp_id,
    e.first_name,
    e.last_name,
    ep.bio,
    ep.address,
    ep.phone
FROM employees e
LEFT JOIN employee_profiles ep ON e.profile_id = ep.id
ORDER BY e.id;

-- Test: ManyToMany (Employee <-> Project)
SELECT 
    e.first_name,
    e.last_name,
    p.name as project_name,
    ep.role,
    ep.assigned_date
FROM employees e
JOIN employee_projects ep ON e.id = ep.employee_id
JOIN projects p ON ep.project_id = p.id
ORDER BY e.id, p.id;

-- ==============================================================================
-- 9. STATISTICS AND REPORTING QUERIES
-- ==============================================================================

-- Test: Employee distribution by department
SELECT 
    d.name as Department,
    COUNT(e.id) as TotalEmployees,
    SUM(CASE WHEN e.is_active = TRUE THEN 1 ELSE 0 END) as ActiveEmployees,
    AVG(e.salary) as AvgSalary,
    MIN(e.salary) as MinSalary,
    MAX(e.salary) as MaxSalary
FROM departments d
LEFT JOIN employees e ON d.id = e.department_id
GROUP BY d.id, d.name
ORDER BY TotalEmployees DESC;

-- Test: Project assignments summary
SELECT 
    p.name as ProjectName,
    p.status,
    COUNT(ep.employee_id) as AssignedEmployees,
    p.budget,
    p.start_date,
    p.end_date
FROM projects p
LEFT JOIN employee_projects ep ON p.id = ep.project_id
GROUP BY p.id, p.name, p.status, p.budget, p.start_date, p.end_date
ORDER BY p.id;

-- Test: Employee project workload
SELECT 
    e.first_name,
    e.last_name,
    d.name as Department,
    COUNT(ep.project_id) as ProjectCount
FROM employees e
LEFT JOIN departments d ON e.department_id = d.id
LEFT JOIN employee_projects ep ON e.id = ep.employee_id
GROUP BY e.id, e.first_name, e.last_name, d.name
ORDER BY ProjectCount DESC, e.last_name;

-- Test: Salary statistics
SELECT 
    'Overall' as Category,
    COUNT(*) as EmployeeCount,
    AVG(salary) as AvgSalary,
    MIN(salary) as MinSalary,
    MAX(salary) as MaxSalary,
    SUM(salary) as TotalSalary
FROM employees
UNION ALL
SELECT 
    'Active Only' as Category,
    COUNT(*) as EmployeeCount,
    AVG(salary) as AvgSalary,
    MIN(salary) as MinSalary,
    MAX(salary) as MaxSalary,
    SUM(salary) as TotalSalary
FROM employees
WHERE is_active = TRUE;

-- ==============================================================================
-- 10. TRANSACTION TEST QUERIES
-- ==============================================================================

-- Test: Update salary (for transaction test)
-- This will be used in Java code with transaction management
SELECT id, first_name, last_name, salary 
FROM employees 
WHERE id = 1;

-- After update:
-- UPDATE employees SET salary = salary * 1.10 WHERE id = 1;

-- Test: Delete and rollback (for transaction test)
SELECT COUNT(*) FROM employees WHERE id = 20;

-- After delete:
-- DELETE FROM employees WHERE id = 20;
-- ROLLBACK;

-- ==============================================================================
-- 11. LAZY LOADING TEST QUERIES
-- ==============================================================================

-- Test: Get employee without loading department (lazy)
SELECT * FROM employees WHERE id = 1;

-- Test: Get employee and explicitly load department
SELECT 
    e.*,
    d.*
FROM employees e
LEFT JOIN departments d ON e.department_id = d.id
WHERE e.id = 1;

-- Test: Get department without loading employees (lazy)
SELECT * FROM departments WHERE id = 1;

-- Test: Get department and explicitly load employees
SELECT 
    d.*,
    e.id as emp_id,
    e.first_name,
    e.last_name
FROM departments d
LEFT JOIN employees e ON d.id = e.department_id
WHERE d.id = 1;

-- ==============================================================================
-- 12. PERFORMANCE TEST QUERIES
-- ==============================================================================

-- Test: Query with index (department_id has index)
EXPLAIN SELECT * FROM employees WHERE department_id = 1;

-- Test: Query with index (email has unique index)
EXPLAIN SELECT * FROM employees WHERE email = 'anh.nguyen@company.com';

-- Test: Full table scan (no index on first_name)
EXPLAIN SELECT * FROM employees WHERE first_name = 'Nguyen';

-- ==============================================================================
-- END OF TEST QUERIES
-- ==============================================================================
