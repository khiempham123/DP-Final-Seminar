package com.dam.framework.example;

import com.dam.framework.core.Configuration;
import com.dam.framework.core.Session;
import com.dam.framework.core.SessionFactory;
import com.dam.framework.core.Transaction;
import com.dam.framework.spi.*;

import java.time.LocalDateTime;

/**
 * Example demonstrating framework customization capabilities.
 * 
 * <p>This example shows how to:
 * <ul>
 *   <li>Use custom naming strategies (snake_case)</li>
 *   <li>Register entity interceptors for auditing</li>
 *   <li>Register custom type converters</li>
 *   <li>Configure connection pool settings</li>
 * </ul>
 * 
 * @author DAM Framework
 */
public class CustomizationExample {
    
    public static void main(String[] args) {
        // Example 1: Configure with custom naming strategy
        configureWithSnakeCaseNaming();
        
        // Example 2: Configure with interceptors
        configureWithInterceptors();
        
        // Example 3: Configure with custom pool settings
        configureWithCustomPool();
        
        // Example 4: Complete customization
        completeCustomization();
    }
    
    /**
     * Example 1: Use snake_case naming convention
     */
    private static void configureWithSnakeCaseNaming() {
        System.out.println("=== Example 1: Snake Case Naming ===");
        
        // Configure with snake_case naming
        Configuration config = Configuration.getInstance();
        config.setNamingStrategy(new SnakeCaseNamingStrategy());
        
        // Now all tables/columns will use snake_case
        // Employee class → employee table
        // firstName field → first_name column
        
        SessionFactory factory = config.buildSessionFactory();
        // Use factory...
        factory.close();
        
        Configuration.reset();
    }
    
    /**
     * Example 2: Use entity interceptors for auditing
     */
    private static void configureWithInterceptors() {
        System.out.println("\n=== Example 2: Entity Interceptors ===");
        
        Configuration config = Configuration.getInstance();
        
        // Register an audit interceptor
        config.registerInterceptor(new AuditInterceptor());
        
        SessionFactory factory = config.buildSessionFactory();
        
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            
            // Interceptor will automatically set createdAt/updatedAt
            Employee emp = new Employee();
            emp.setFirstName("John");
            emp.setLastName("Doe");
            emp.setEmail("john.doe@example.com");
            session.save(emp);
            
            tx.commit();
            
            System.out.println("Employee saved with audit fields populated");
        }
        
        factory.close();
        Configuration.reset();
    }
    
    /**
     * Example 3: Configure connection pool for high load
     */
    private static void configureWithCustomPool() {
        System.out.println("\n=== Example 3: Custom Connection Pool ===");
        
        Configuration config = Configuration.getInstance();
        
        // Use high-load configuration
        ConnectionPoolConfig poolConfig = ConnectionPoolConfig.highLoadConfig();
        poolConfig.setMetricsEnabled(true);
        
        config.setConnectionPoolConfig(poolConfig);
        
        SessionFactory factory = config.buildSessionFactory();
        // Pool now configured with: 20 min idle, 50 max connections
        
        factory.close();
        Configuration.reset();
    }
    
    /**
     * Example 4: Complete customization with all features
     */
    private static void completeCustomization() {
        System.out.println("\n=== Example 4: Complete Customization ===");
        
        Configuration config = Configuration.getInstance();
        
        // 1. Set naming strategy
        config.setNamingStrategy(new SnakeCaseNamingStrategy());
        
        // 2. Configure connection pool
        ConnectionPoolConfig poolConfig = new ConnectionPoolConfig();
        poolConfig.setMinimumIdle(5);
        poolConfig.setMaximumPoolSize(20);
        poolConfig.setConnectionTimeout(15000);
        config.setConnectionPoolConfig(poolConfig);
        
        // 3. Register interceptors (can register multiple)
        config.registerInterceptor(new AuditInterceptor());
        config.registerInterceptor(new ValidationInterceptor());
        
        // 4. Register custom type converters (if needed)
        // config.registerTypeConverter(new JsonTypeConverter());
        
        // Build SessionFactory (locks configuration)
        SessionFactory factory = config.buildSessionFactory();
        
        // Now all customizations are active
        System.out.println("Framework configured with all customizations");
        
        factory.close();
        Configuration.reset();
    }
    
    /**
     * Custom interceptor for automatic audit field population.
     */
    static class AuditInterceptor implements EntityInterceptor {
        
        @Override
        public boolean onPreSave(Object entity) {
            if (entity instanceof Auditable) {
                Auditable auditable = (Auditable) entity;
                auditable.setCreatedAt(LocalDateTime.now());
                auditable.setUpdatedAt(LocalDateTime.now());
            }
            return true;
        }
        
        @Override
        public boolean onPreUpdate(Object entity) {
            if (entity instanceof Auditable) {
                Auditable auditable = (Auditable) entity;
                auditable.setUpdatedAt(LocalDateTime.now());
            }
            return true;
        }
    }
    
    /**
     * Custom interceptor for entity validation.
     */
    static class ValidationInterceptor implements EntityInterceptor {
        
        @Override
        public boolean onPreSave(Object entity) {
            return validate(entity);
        }
        
        @Override
        public boolean onPreUpdate(Object entity) {
            return validate(entity);
        }
        
        private boolean validate(Object entity) {
            if (entity instanceof Employee) {
                Employee emp = (Employee) entity;
                if (emp.getFirstName() == null || emp.getFirstName().trim().isEmpty()) {
                    System.err.println("Validation failed: Employee first name is required");
                    return false; // Cancel operation
                }
            }
            return true;
        }
    }
    
    /**
     * Interface for auditable entities.
     */
    interface Auditable {
        void setCreatedAt(LocalDateTime createdAt);
        void setUpdatedAt(LocalDateTime updatedAt);
    }
}
