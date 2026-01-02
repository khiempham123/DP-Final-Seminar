package com.dam.framework;

import com.dam.framework.annotation.*;

/**
 * Example entity class to demonstrate framework usage.
 * This class represents a User table in the database.
 * 
 * @author Example
 */
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GeneratedValue.GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "age")
    private Integer age;
    
    // Default constructor (required by framework)
    public User() {
    }
    
    // Constructor for creating new users
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
    
    public User(String username, String email, Integer age) {
        this.username = username;
        this.email = email;
        this.age = age;
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
