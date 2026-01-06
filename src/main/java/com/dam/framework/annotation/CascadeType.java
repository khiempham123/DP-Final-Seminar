package com.dam.framework.annotation;

/**
 * Defines the cascade operations for relationships.
 * When an operation is performed on an entity, it can be
 * cascaded to related entities.
 * 
 * @author Dev 1
 */
public enum CascadeType {
    
    /**
     * Cascade all operations.
     */
    ALL,
    
    /**
     * Cascade persist (insert) operations.
     * When the parent entity is saved, related entities are also saved.
     */
    PERSIST,
    
    /**
     * Cascade merge (update) operations.
     * When the parent entity is merged, related entities are also merged.
     */
    MERGE,
    
    /**
     * Cascade remove (delete) operations.
     * When the parent entity is deleted, related entities are also deleted.
     */
    REMOVE,
    
    /**
     * Cascade refresh operations.
     * When the parent entity is refreshed from DB, related entities are also refreshed.
     */
    REFRESH,
    
    /**
     * Cascade detach operations.
     * When the parent entity is detached from session, related entities are also detached.
     */
    DETACH
}
