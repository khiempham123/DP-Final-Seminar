package com.dam.framework.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration class for the DAM Framework.
 * 
 * Design Pattern: Singleton Pattern (GoF Pattern #3)
 * Ensures only one instance of configuration exists in the application lifecycle.
 * 
 * @author Dev 2
 */
public class Configuration {
    
    private static volatile Configuration instance;
    private Properties properties;
    
    // Private constructor to prevent instantiation
    private Configuration() {
        this.properties = new Properties();
        loadProperties();
    }
    
    /**
     * Get the singleton instance of Configuration.
     * Thread-safe double-checked locking.
     * 
     * @return The Configuration instance
     */
    public static Configuration getInstance() {
        if (instance == null) {
            synchronized (Configuration.class) {
                if (instance == null) {
                    instance = new Configuration();
                }
            }
        }
        return instance;
    }
    
    /**
     * Load properties from application.properties file.
     */
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find application.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading configuration", ex);
        }
    }
    
    /**
     * Get a configuration property.
     * 
     * @param key The property key
     * @return The property value
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get database URL.
     */
    public String getDatabaseUrl() {
        return getProperty("db.url");
    }
    
    /**
     * Get database username.
     */
    public String getDatabaseUsername() {
        return getProperty("db.username");
    }
    
    /**
     * Get database password.
     */
    public String getDatabasePassword() {
        return getProperty("db.password");
    }
    
    /**
     * Get database dialect class name.
     */
    public String getDialectClass() {
        return getProperty("db.dialect");
    }
    
    /**
     * Get connection pool max size.
     */
    public int getPoolMaxSize() {
        return Integer.parseInt(getProperty("db.pool.max_size"));
    }
    
    /**
     * Build a SessionFactory from this configuration.
     * TODO: Dev 1 - Implement this method
     * 
     * @return A new SessionFactory instance
     */
    public SessionFactory buildSessionFactory() {
        // TODO: Dev 1 - Create SessionFactoryImpl here
        throw new UnsupportedOperationException("Not implemented yet - Dev 1 task");
    }
}
