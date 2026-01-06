package com.dam.framework.annotation;

/**
 * Defines the fetch strategy for loading related entities.
 * 
 * @author Dev 1
 */
public enum FetchType {
    
    /**
     * LAZY: Related entities are loaded on-demand when accessed.
     * This is more efficient when the related data is not always needed.
     * Uses proxy objects to defer loading.
     */
    LAZY,
    
    /**
     * EAGER: Related entities are loaded immediately with the parent entity.
     * This is useful when the related data is always needed.
     * May cause performance issues with large datasets.
     */
    EAGER
}
