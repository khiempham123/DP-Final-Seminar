package com.dam.framework.example;

import com.dam.framework.annotation.*;
import java.time.LocalDate;

/**
 * Example EmployeeProfile entity demonstrating @OneToOne relationship.
 * Each employee has one profile with additional information.
 * 
 * @author Dev 1
 */
@Entity
@Table(name = "employee_profiles")
public class EmployeeProfile {
    
    @Id
    @GeneratedValue(strategy = GeneratedValue.GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "postal_code")
    private String postalCode;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Column(name = "bio")
    private String bio;
    
    @Column(name = "linkedin_url")
    private String linkedinUrl;
    
    @Column(name = "github_url")
    private String githubUrl;
    
    /**
     * One-to-One relationship back to Employee.
     * This is the inverse side (mappedBy).
     */
    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
    private Employee employee;
    
    // Constructors
    
    public EmployeeProfile() {
    }
    
    public EmployeeProfile(String address, String city, String country) {
        this.address = address;
        this.city = city;
        this.country = country;
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public String getLinkedinUrl() {
        return linkedinUrl;
    }
    
    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }
    
    public String getGithubUrl() {
        return githubUrl;
    }
    
    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (address != null) sb.append(address);
        if (city != null) sb.append(", ").append(city);
        if (postalCode != null) sb.append(" ").append(postalCode);
        if (country != null) sb.append(", ").append(country);
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "EmployeeProfile{" +
                "id=" + id +
                ", address='" + getFullAddress() + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
