package com.dam.framework.exception;

/**
 * Exception thrown when an entity is not found in the database.
 * 
 * @author Dev 1
 */
public class EntityNotFoundException extends DAMException {
    
    private static final long serialVersionUID = 1L;
    
    private Class<?> entityClass;
    private Object id;
    
    /**
     * Create exception with entity class and ID.
     * 
     * @param entityClass The entity class that was not found
     * @param id The ID that was searched for
     */
    public EntityNotFoundException(Class<?> entityClass, Object id) {
        super(String.format("Entity '%s' with id '%s' not found", 
            entityClass.getSimpleName(), id));
        this.entityClass = entityClass;
        this.id = id;
    }
    
    /**
     * Create exception with a custom message.
     * 
     * @param message The error message
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Create exception with message and cause.
     * 
     * @param message The error message
     * @param cause The underlying cause
     */
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Get the entity class that was not found.
     * 
     * @return The entity class
     */
    public Class<?> getEntityClass() {
        return entityClass;
    }
    
    /**
     * Get the ID that was searched for.
     * 
     * @return The ID
     */
    public Object getId() {
        return id;
    }
}
