package com.dam.framework.spi;

/**
 * Interceptor Pattern for entity lifecycle events.
 * Allows users to hook into entity operations for cross-cutting concerns like:
 * <ul>
 *   <li>Auditing (createdAt, updatedAt fields)</li>
 *   <li>Validation before save/update</li>
 *   <li>Logging entity changes</li>
 *   <li>Triggering business logic</li>
 * </ul>
 * 
 * <p>Usage example:
 * <pre>
 * public class AuditInterceptor implements EntityInterceptor {
 *     public boolean onPreSave(Object entity) {
 *         if (entity instanceof Auditable) {
 *             ((Auditable) entity).setCreatedAt(LocalDateTime.now());
 *         }
 *         return true; // allow operation to proceed
 *     }
 *     
 *     public void onPostSave(Object entity) {
 *         logger.info("Entity saved: " + entity);
 *     }
 * }
 * </pre>
 * 
 * @author DAM Framework
 */
public interface EntityInterceptor {
    
    /**
     * Called before an entity is saved (INSERT).
     * 
     * @param entity the entity about to be saved
     * @return true to allow save, false to cancel operation
     */
    default boolean onPreSave(Object entity) {
        return true;
    }
    
    /**
     * Called after an entity is saved (INSERT).
     * 
     * @param entity the saved entity
     */
    default void onPostSave(Object entity) {
        // no-op by default
    }
    
    /**
     * Called before an entity is updated (UPDATE).
     * 
     * @param entity the entity about to be updated
     * @return true to allow update, false to cancel operation
     */
    default boolean onPreUpdate(Object entity) {
        return true;
    }
    
    /**
     * Called after an entity is updated (UPDATE).
     * 
     * @param entity the updated entity
     */
    default void onPostUpdate(Object entity) {
        // no-op by default
    }
    
    /**
     * Called before an entity is deleted (DELETE).
     * 
     * @param entity the entity about to be deleted
     * @return true to allow delete, false to cancel operation
     */
    default boolean onPreDelete(Object entity) {
        return true;
    }
    
    /**
     * Called after an entity is deleted (DELETE).
     * 
     * @param entity the deleted entity
     */
    default void onPostDelete(Object entity) {
        // no-op by default
    }
    
    /**
     * Called after an entity is loaded from database (SELECT).
     * 
     * @param entity the loaded entity
     */
    default void onPostLoad(Object entity) {
        // no-op by default
    }
}
