package com.dam.framework.core;

import com.dam.framework.dialect.Dialect;
import com.dam.framework.dialect.MySQLDialect;
import com.dam.framework.dialect.PostgreSQLDialect;
import com.dam.framework.dialect.SQLServerDialect;
import com.dam.framework.spi.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration class for the DAM Framework.
 * 
 * Design Pattern: Singleton Pattern (GoF Pattern #3)
 * Ensures only one instance of configuration exists in the application lifecycle.
 * 
 * Supports customization through:
 * - NamingStrategy for table/column naming conventions
 * - TypeConverter for custom type mappings
 * - EntityInterceptor for lifecycle hooks
 * - ConnectionPoolConfig for connection pool tuning
 * 
 * @author Dev 2
 */
public class Configuration {
    
    private static volatile Configuration instance;
    private Properties properties;
    private SessionFactory sessionFactory;
    
    // Customization components
    private NamingStrategy namingStrategy = new DefaultNamingStrategy();
    private ConnectionPoolConfig poolConfig = ConnectionPoolConfig.defaultConfig();
    private boolean customizationLocked = false;
    
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
        String value = getProperty("db.pool.max_size");
        return value != null ? Integer.parseInt(value) : 10;
    }
    
    /**
     * Sets a custom naming strategy.
     * Must be called before buildSessionFactory().
     * 
     * @param namingStrategy the naming strategy
     * @return this Configuration for method chaining
     * @throws IllegalStateException if SessionFactory already built
     */
    public Configuration setNamingStrategy(NamingStrategy namingStrategy) {
        checkCustomizationAllowed();
        if (namingStrategy == null) {
            throw new IllegalArgumentException("NamingStrategy cannot be null");
        }
        this.namingStrategy = namingStrategy;
        return this;
    }
    
    /**
     * Gets the current naming strategy.
     * 
     * @return the naming strategy
     */
    public NamingStrategy getNamingStrategy() {
        return namingStrategy;
    }
    
    /**
     * Sets custom connection pool configuration.
     * Must be called before buildSessionFactory().
     * 
     * @param poolConfig the pool configuration
     * @return this Configuration for method chaining
     * @throws IllegalStateException if SessionFactory already built
     */
    public Configuration setConnectionPoolConfig(ConnectionPoolConfig poolConfig) {
        checkCustomizationAllowed();
        if (poolConfig == null) {
            throw new IllegalArgumentException("ConnectionPoolConfig cannot be null");
        }
        this.poolConfig = poolConfig;
        return this;
    }
    
    /**
     * Gets the current connection pool configuration.
     * 
     * @return the pool configuration
     */
    public ConnectionPoolConfig getConnectionPoolConfig() {
        return poolConfig;
    }
    
    /**
     * Registers a custom type converter.
     * Can be called at any time.
     * 
     * @param converter the type converter
     * @return this Configuration for method chaining
     */
    public Configuration registerTypeConverter(TypeConverter<?> converter) {
        TypeConverterRegistry.getInstance().registerConverter(converter);
        return this;
    }
    
    /**
     * Registers an entity interceptor.
     * Can be called at any time.
     * 
     * @param interceptor the entity interceptor
     * @return this Configuration for method chaining
     */
    public Configuration registerInterceptor(EntityInterceptor interceptor) {
        InterceptorRegistry.getInstance().registerInterceptor(interceptor);
        return this;
    }
    
    /**
     * Checks if customization is allowed.
     * Customization is locked after SessionFactory is built to ensure consistency.
     * 
     * @throws IllegalStateException if customization is locked
     */
    private void checkCustomizationAllowed() {
        if (customizationLocked) {
            throw new IllegalStateException(
                "Cannot modify configuration after SessionFactory is built. " +
                "Call setNamingStrategy() and setConnectionPoolConfig() before buildSessionFactory()."
            );
        }
    }
    
    /**
     * Create the appropriate Dialect based on configuration.
     */
    private Dialect createDialect() {
        String dialectClass = getDialectClass();
        
        if (dialectClass == null || dialectClass.isEmpty()) {
            // Auto-detect from URL
            String url = getDatabaseUrl();
            if (url.contains("mysql")) {
                return new MySQLDialect();
            } else if (url.contains("postgresql")) {
                return new PostgreSQLDialect();
            } else if (url.contains("sqlserver")) {
                return new SQLServerDialect();
            }
            return new MySQLDialect(); // Default
        }
        
        // Create dialect from class name
        if (dialectClass.contains("MySQL")) {
            return new MySQLDialect();
        } else if (dialectClass.contains("PostgreSQL")) {
            return new PostgreSQLDialect();
        } else if (dialectClass.contains("SQLServer")) {
            return new SQLServerDialect();
        }
        
        // Try to instantiate by reflection
        try {
            Class<?> clazz = Class.forName(dialectClass);
            return (Dialect) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create dialect: " + dialectClass, e);
        }
    }
    
    /**
     * Build a SessionFactory from this configuration.
     * After calling this method, customization is locked.
     * 
     * @return A new SessionFactory instance
     */
    public SessionFactory buildSessionFactory() {
        if (sessionFactory != null) {
            return sessionFactory;
        }
        
        // Lock customization after building SessionFactory
        customizationLocked = true;
        
        // Create connection pool with custom config
        ConnectionPool connectionPool = new ConnectionPool(
            getDatabaseUrl(),
            getDatabaseUsername(),
            getDatabasePassword(),
            poolConfig
        );
        
        // Create dialect
        Dialect dialect = createDialect();
        
        // Create SessionFactory
        sessionFactory = new SessionFactoryImpl(connectionPool, dialect);
        
        return sessionFactory;
    }
    
    /**
     * Reset the configuration (useful for testing).
     */
    public static void reset() {
        if (instance != null && instance.sessionFactory != null) {
            instance.sessionFactory.close();
        }
        // Clear registries
        TypeConverterRegistry.getInstance().clear();
        InterceptorRegistry.getInstance().clear();
        instance = null;
    }
}
