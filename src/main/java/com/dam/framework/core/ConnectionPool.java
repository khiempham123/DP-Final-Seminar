package com.dam.framework.core;

import com.dam.framework.spi.ConnectionPoolConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connection pool management using HikariCP.
 * Manages database connections efficiently by reusing connections.
 * Supports customization through ConnectionPoolConfig.
 * 
 * @author Dev 1
 */
public class ConnectionPool implements AutoCloseable {
    
    private HikariDataSource dataSource;
    
    /**
     * Create a connection pool with the given configuration.
     * 
     * @param jdbcUrl Database URL
     * @param username Database username
     * @param password Database password
     * @param maxPoolSize Maximum number of connections in the pool
     * @deprecated Use constructor with ConnectionPoolConfig for better customization
     */
    @Deprecated
    public ConnectionPool(String jdbcUrl, String username, String password, int maxPoolSize) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(maxPoolSize);
        
        // Performance optimizations
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000); // 30 seconds
        config.setConnectionTimeout(20000); // 20 seconds
        config.setMaxLifetime(1800000); // 30 minutes
        
        // MySQL specific optimizations
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        
        this.dataSource = new HikariDataSource(config);
    }
    
    /**
     * Create a connection pool with custom pool configuration.
     * 
     * @param jdbcUrl Database URL
     * @param username Database username
     * @param password Database password
     * @param poolConfig Custom pool configuration
     */
    public ConnectionPool(String jdbcUrl, String username, String password, ConnectionPoolConfig poolConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        
        // Apply custom pool configuration
        config.setMaximumPoolSize(poolConfig.getMaximumPoolSize());
        config.setMinimumIdle(poolConfig.getMinimumIdle());
        config.setIdleTimeout(poolConfig.getIdleTimeout());
        config.setConnectionTimeout(poolConfig.getConnectionTimeout());
        config.setMaxLifetime(poolConfig.getMaxLifetime());
        
        // Enable metrics if configured
        if (poolConfig.isMetricsEnabled()) {
            config.setRegisterMbeans(true);
        }
        
        // MySQL specific optimizations
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        
        this.dataSource = new HikariDataSource(config);
    }
    
    /**
     * Get a connection from the pool.
     * 
     * @return A database connection
     * @throws SQLException if unable to get connection
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    /**
     * Get current active connections count.
     */
    public int getActiveConnections() {
        return dataSource.getHikariPoolMXBean().getActiveConnections();
    }
    
    /**
     * Get total connections in pool.
     */
    public int getTotalConnections() {
        return dataSource.getHikariPoolMXBean().getTotalConnections();
    }
    
    /**
     * Close the connection pool and release all resources.
     */
    @Override
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
    
    /**
     * Check if the pool is closed.
     */
    public boolean isClosed() {
        return dataSource == null || dataSource.isClosed();
    }
}
