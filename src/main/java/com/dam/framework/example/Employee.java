package com.dam.framework.example;

import com.dam.framework.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Example Employee entity demonstrating @ManyToOne relationship.
 * An employee belongs to one department.
 * 
 * @author Dev 1
 */
@Entity
@Table(name = "employees")
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GeneratedValue.GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "hire_date")
    private LocalDate hireDate;
    
    @Column(name = "salary")
    private BigDecimal salary;
    
    /**
     * Many-to-One relationship with Department.
     * Many employees belong to one department.
     * This is the owning side (has the foreign key).
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;
    
    /**
     * One-to-One relationship with EmployeeProfile.
     * An employee has one profile.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private EmployeeProfile profile;
    
    // Constructors
    
    public Employee() {
    }
    
    public Employee(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    public Employee(String firstName, String lastName, String email, Department department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    
    public BigDecimal getSalary() {
        return salary;
    }
    
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    
    public Department getDepartment() {
        return department;
    }
    
    public void setDepartment(Department department) {
        this.department = department;
    }
    
    public EmployeeProfile getProfile() {
        return profile;
    }
    
    public void setProfile(EmployeeProfile profile) {
        this.profile = profile;
    }
    
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", department=" + (department != null ? department.getName() : "null") +
                '}';
    }
}
