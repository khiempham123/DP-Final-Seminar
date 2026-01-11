package com.demo.entity;

import com.dam.framework.annotation.*;
import java.util.List;

/**
 * Category entity - Demo OneToMany relationship
 */
@Entity
@Table(name = "categories")
public class Category {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;
    
    // Constructors
    public Category() {}
    
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
    
    @Override
    public String toString() {
        return String.format("Category{id=%d, name='%s', description='%s'}", 
            id, name, description);
    }
}
