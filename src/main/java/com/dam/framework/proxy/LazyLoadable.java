package com.dam.framework.proxy;

/**
 * Interface for lazy-loadable entities.
 * All proxied entities will implement this interface,
 * allowing users to check loading status and force loading.
 * 
 * @author Dev 1
 */
public interface LazyLoadable {
    
    /**
     * Check if the entity has been loaded from the database.
     * 
     * @return true if the entity is loaded
     */
    boolean isLoaded();
    
    /**
     * Force load the entity from the database.
     * 
     * @return The loaded entity
     */
    Object load();
    
    /**
     * Get the class of the proxied entity.
     * 
     * @return The entity class
     */
    Class<?> getEntityClass();
    
    /**
     * Get the ID of the proxied entity.
     * 
     * @return The entity ID
     */
    Object getEntityId();
}
