package com.dam.framework.spi;

/**
 * Configuration class for database connection pool settings.
 * Provides controlled customization of HikariCP connection pool parameters.
 * 
 * <p>Only essential pool parameters are exposed to maintain framework stability.
 * Advanced users needing more control should configure HikariCP directly.
 * 
 * @author DAM Framework
 */
public class ConnectionPoolConfig {
    
    /**
     * Minimum number of idle connections maintained in the pool.
     * Default: 10
     */
    private int minimumIdle = 10;
    
    /**
     * Maximum number of connections in the pool.
     * Default: 20
     */
    private int maximumPoolSize = 20;
    
    /**
     * Maximum time (milliseconds) to wait for a connection from the pool.
     * Default: 30000 (30 seconds)
     */
    private long connectionTimeout = 30000;
    
    /**
     * Maximum lifetime (milliseconds) of a connection in the pool.
     * Default: 1800000 (30 minutes)
     */
    private long maxLifetime = 1800000;
    
    /**
     * Maximum time (milliseconds) a connection can sit idle in the pool.
     * Default: 600000 (10 minutes)
     */
    private long idleTimeout = 600000;
    
    /**
     * Enable/disable connection pool metrics logging.
     * Default: false
     */
    private boolean metricsEnabled = false;
    
    public int getMinimumIdle() {
        return minimumIdle;
    }
    
    /**
     * Sets minimum idle connections.
     * 
     * @param minimumIdle must be >= 1 and <= maximumPoolSize
     * @throws IllegalArgumentException if value is invalid
     */
    public void setMinimumIdle(int minimumIdle) {
        if (minimumIdle < 1) {
            throw new IllegalArgumentException("minimumIdle must be >= 1");
        }
        if (minimumIdle > maximumPoolSize) {
            throw new IllegalArgumentException("minimumIdle cannot exceed maximumPoolSize");
        }
        this.minimumIdle = minimumIdle;
    }
    
    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }
    
    /**
     * Sets maximum pool size.
     * 
     * @param maximumPoolSize must be >= minimumIdle, recommended range: 10-100
     * @throws IllegalArgumentException if value is invalid
     */
    public void setMaximumPoolSize(int maximumPoolSize) {
        if (maximumPoolSize < minimumIdle) {
            throw new IllegalArgumentException("maximumPoolSize must be >= minimumIdle");
        }
        if (maximumPoolSize > 100) {
            throw new IllegalArgumentException("maximumPoolSize cannot exceed 100 (framework limit)");
        }
        this.maximumPoolSize = maximumPoolSize;
    }
    
    public long getConnectionTimeout() {
        return connectionTimeout;
    }
    
    /**
     * Sets connection timeout.
     * 
     * @param connectionTimeout must be >= 1000 (1 second)
     * @throws IllegalArgumentException if value is invalid
     */
    public void setConnectionTimeout(long connectionTimeout) {
        if (connectionTimeout < 1000) {
            throw new IllegalArgumentException("connectionTimeout must be >= 1000ms (1 second)");
        }
        this.connectionTimeout = connectionTimeout;
    }
    
    public long getMaxLifetime() {
        return maxLifetime;
    }
    
    /**
     * Sets maximum connection lifetime.
     * 
     * @param maxLifetime must be >= 30000 (30 seconds), or 0 for infinite
     * @throws IllegalArgumentException if value is invalid
     */
    public void setMaxLifetime(long maxLifetime) {
        if (maxLifetime != 0 && maxLifetime < 30000) {
            throw new IllegalArgumentException("maxLifetime must be >= 30000ms (30 seconds) or 0 for infinite");
        }
        this.maxLifetime = maxLifetime;
    }
    
    public long getIdleTimeout() {
        return idleTimeout;
    }
    
    /**
     * Sets idle timeout.
     * 
     * @param idleTimeout must be >= 10000 (10 seconds), or 0 to disable
     * @throws IllegalArgumentException if value is invalid
     */
    public void setIdleTimeout(long idleTimeout) {
        if (idleTimeout != 0 && idleTimeout < 10000) {
            throw new IllegalArgumentException("idleTimeout must be >= 10000ms (10 seconds) or 0 to disable");
        }
        this.idleTimeout = idleTimeout;
    }
    
    public boolean isMetricsEnabled() {
        return metricsEnabled;
    }
    
    public void setMetricsEnabled(boolean metricsEnabled) {
        this.metricsEnabled = metricsEnabled;
    }
    
    /**
     * Creates a default configuration with safe defaults.
     * 
     * @return default config instance
     */
    public static ConnectionPoolConfig defaultConfig() {
        return new ConnectionPoolConfig();
    }
    
    /**
     * Creates a configuration for high-load scenarios.
     * 
     * @return high-load config instance
     */
    public static ConnectionPoolConfig highLoadConfig() {
        ConnectionPoolConfig config = new ConnectionPoolConfig();
        config.setMinimumIdle(20);
        config.setMaximumPoolSize(50);
        config.setConnectionTimeout(10000);
        return config;
    }
    
    /**
     * Creates a configuration for low-resource scenarios.
     * 
     * @return low-resource config instance
     */
    public static ConnectionPoolConfig lowResourceConfig() {
        ConnectionPoolConfig config = new ConnectionPoolConfig();
        config.setMinimumIdle(2);
        config.setMaximumPoolSize(5);
        config.setConnectionTimeout(60000);
        return config;
    }
}
