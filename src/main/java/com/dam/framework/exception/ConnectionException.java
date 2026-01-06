package com.dam.framework.exception;

/**
 * Exception thrown when there is an error with database connections.
 * This includes connection pool exhaustion, connection timeout,
 * and connection configuration errors.
 * 
 * @author Dev 1
 */
public class ConnectionException extends DAMException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Create exception with a message.
     * 
     * @param message The error message
     */
    public ConnectionException(String message) {
        super(message);
    }
    
    /**
     * Create exception with message and cause.
     * 
     * @param message The error message
     * @param cause The underlying cause
     */
    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Create exception for connection pool exhaustion.
     * 
     * @return ConnectionException
     */
    public static ConnectionException poolExhausted() {
        return new ConnectionException("Connection pool exhausted - no available connections");
    }
    
    /**
     * Create exception for connection timeout.
     * 
     * @param timeoutMs The timeout in milliseconds
     * @return ConnectionException
     */
    public static ConnectionException timeout(long timeoutMs) {
        return new ConnectionException(
            String.format("Connection timeout after %d ms", timeoutMs)
        );
    }
    
    /**
     * Create exception for invalid connection URL.
     * 
     * @param url The invalid URL
     * @return ConnectionException
     */
    public static ConnectionException invalidUrl(String url) {
        return new ConnectionException(
            String.format("Invalid database URL: %s", url)
        );
    }
    
    /**
     * Create exception for authentication failure.
     * 
     * @param username The username that failed
     * @return ConnectionException
     */
    public static ConnectionException authenticationFailed(String username) {
        return new ConnectionException(
            String.format("Authentication failed for user: %s", username)
        );
    }
    
    /**
     * Create exception for connection closed.
     * 
     * @return ConnectionException
     */
    public static ConnectionException connectionClosed() {
        return new ConnectionException("Connection is closed");
    }
    
    /**
     * Create exception for driver not found.
     * 
     * @param driverClass The driver class that was not found
     * @return ConnectionException
     */
    public static ConnectionException driverNotFound(String driverClass) {
        return new ConnectionException(
            String.format("JDBC driver not found: %s", driverClass)
        );
    }
}
