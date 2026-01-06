package com.dam.framework.example;

import com.dam.framework.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Example Department entity demonstrating @OneToMany relationship.
 * A department can have many employees.
 * 
 * @author Dev 1
 */
@Entity
@Table(name = "departments")
public class Department {
    
    @Id
    @GeneratedValue(strategy = GeneratedValue.GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "code")
    private String code;
    
    @Column(name = "description")
    private String description;
    
    /**
     * One-to-Many relationship with Employee.
     * A department has many employees.
     * Mapped by the "department" field in Employee class.
     */
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Employee> employees = new ArrayList<>();
    
    // Constructors
    
    public Department() {
    }
    
    public Department(String name, String code) {
        this.name = name;
        this.code = code;
    }
    
    public Department(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<Employee> getEmployees() {
        return employees;
    }
    
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    
    // Helper methods
    
    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setDepartment(this);
    }
    
    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        employee.setDepartment(null);
    }
    
    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", employeeCount=" + (employees != null ? employees.size() : 0) +
                '}';
    }
}
