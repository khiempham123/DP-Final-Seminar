package com.dam.framework.exception;

/**
 * Exception thrown when there is an error mapping entities.
 * This includes annotation parsing errors, type conversion errors,
 * and reflection access errors.
 * 
 * @author Dev 1
 */
public class MappingException extends DAMException {
    
    private static final long serialVersionUID = 1L;
    
    private Class<?> entityClass;
    private String fieldName;
    
    /**
     * Create exception with a message.
     * 
     * @param message The error message
     */
    public MappingException(String message) {
        super(message);
    }
    
    /**
     * Create exception with message and cause.
     * 
     * @param message The error message
     * @param cause The underlying cause
     */
    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Create exception for entity class issues.
     * 
     * @param message The error message
     * @param entityClass The entity class with mapping issues
     */
    public MappingException(String message, Class<?> entityClass) {
        super(String.format("%s [Entity: %s]", message, entityClass.getName()));
        this.entityClass = entityClass;
    }
    
    /**
     * Create exception for field mapping issues.
     * 
     * @param message The error message
     * @param entityClass The entity class
     * @param fieldName The field with mapping issues
     */
    public MappingException(String message, Class<?> entityClass, String fieldName) {
        super(String.format("%s [Entity: %s, Field: %s]", message, entityClass.getName(), fieldName));
        this.entityClass = entityClass;
        this.fieldName = fieldName;
    }
    
    /**
     * Create exception for missing @Entity annotation.
     * 
     * @param clazz The class missing the annotation
     * @return MappingException
     */
    public static MappingException missingEntityAnnotation(Class<?> clazz) {
        return new MappingException("Class is not annotated with @Entity", clazz);
    }
    
    /**
     * Create exception for missing @Id annotation.
     * 
     * @param clazz The class missing the ID field
     * @return MappingException
     */
    public static MappingException missingIdField(Class<?> clazz) {
        return new MappingException("No field annotated with @Id found", clazz);
    }
    
    /**
     * Create exception for field access errors.
     * 
     * @param clazz The entity class
     * @param fieldName The field name
     * @param cause The underlying cause
     * @return MappingException
     */
    public static MappingException fieldAccessError(Class<?> clazz, String fieldName, Throwable cause) {
        return new MappingException(
            String.format("Cannot access field '%s' in entity '%s'", fieldName, clazz.getName()),
            cause
        );
    }
    
    /**
     * Create exception for type conversion errors.
     * 
     * @param fromType The source type
     * @param toType The target type
     * @param cause The underlying cause
     * @return MappingException
     */
    public static MappingException typeConversionError(Class<?> fromType, Class<?> toType, Throwable cause) {
        return new MappingException(
            String.format("Cannot convert from '%s' to '%s'", fromType.getName(), toType.getName()),
            cause
        );
    }
    
    /**
     * Get the entity class with mapping issues.
     * 
     * @return The entity class
     */
    public Class<?> getEntityClass() {
        return entityClass;
    }
    
    /**
     * Get the field name with mapping issues.
     * 
     * @return The field name
     */
    public String getFieldName() {
        return fieldName;
    }
}
