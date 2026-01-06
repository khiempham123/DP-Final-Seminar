package com.dam.framework.exception;

/**
 * Base exception class for all DAM Framework exceptions.
 * All custom exceptions in the framework extend this class.
 * 
 * @author Dev 1
 */
public class DAMException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Create a new DAM exception with a message.
     * 
     * @param message The error message
     */
    public DAMException(String message) {
        super(message);
    }
    
    /**
     * Create a new DAM exception with a message and cause.
     * 
     * @param message The error message
     * @param cause The underlying cause
     */
    public DAMException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Create a new DAM exception with a cause.
     * 
     * @param cause The underlying cause
     */
    public DAMException(Throwable cause) {
        super(cause);
    }
}
