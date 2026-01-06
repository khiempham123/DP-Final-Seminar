package com.dam.framework.exception;

/**
 * Exception thrown when session operations fail.
 * This includes operations on closed sessions and invalid session states.
 * 
 * @author Dev 1
 */
public class SessionException extends DAMException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Create exception with a message.
     * 
     * @param message The error message
     */
    public SessionException(String message) {
        super(message);
    }
    
    /**
     * Create exception with message and cause.
     * 
     * @param message The error message
     * @param cause The underlying cause
     */
    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Create exception for closed session.
     * 
     * @return SessionException
     */
    public static SessionException sessionClosed() {
        return new SessionException("Session is closed");
    }
    
    /**
     * Create exception for null entity.
     * 
     * @return SessionException
     */
    public static SessionException nullEntity() {
        return new SessionException("Entity cannot be null");
    }
    
    /**
     * Create exception for null ID.
     * 
     * @return SessionException
     */
    public static SessionException nullId() {
        return new SessionException("Entity ID cannot be null");
    }
    
    /**
     * Create exception for detached entity.
     * 
     * @param entityClass The entity class
     * @return SessionException
     */
    public static SessionException detachedEntity(Class<?> entityClass) {
        return new SessionException(
            String.format("Entity '%s' is detached from session", entityClass.getSimpleName())
        );
    }
    
    /**
     * Create exception for transient entity (not yet saved).
     * 
     * @param entityClass The entity class
     * @return SessionException
     */
    public static SessionException transientEntity(Class<?> entityClass) {
        return new SessionException(
            String.format("Entity '%s' is transient (not yet saved)", entityClass.getSimpleName())
        );
    }
}
