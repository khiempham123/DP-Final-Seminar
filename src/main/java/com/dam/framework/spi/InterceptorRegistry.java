package com.dam.framework.spi;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry for entity interceptors.
 * Manages a chain of interceptors that are invoked during entity lifecycle events.
 * 
 * <p>Thread-safe singleton implementation.
 * 
 * @author DAM Framework
 */
public class InterceptorRegistry {
    
    private static final InterceptorRegistry INSTANCE = new InterceptorRegistry();
    
    private final List<EntityInterceptor> interceptors = new ArrayList<>();
    
    private InterceptorRegistry() {
        // Private constructor for singleton
    }
    
    /**
     * Gets the singleton instance.
     * 
     * @return the registry instance
     */
    public static InterceptorRegistry getInstance() {
        return INSTANCE;
    }
    
    /**
     * Registers an interceptor.
     * Interceptors are invoked in registration order.
     * 
     * @param interceptor the interceptor to register
     * @throws IllegalArgumentException if interceptor is null
     */
    public synchronized void registerInterceptor(EntityInterceptor interceptor) {
        if (interceptor == null) {
            throw new IllegalArgumentException("EntityInterceptor cannot be null");
        }
        interceptors.add(interceptor);
    }
    
    /**
     * Unregisters an interceptor.
     * 
     * @param interceptor the interceptor to remove
     */
    public synchronized void unregisterInterceptor(EntityInterceptor interceptor) {
        interceptors.remove(interceptor);
    }
    
    /**
     * Invokes onPreSave for all registered interceptors.
     * 
     * @param entity the entity
     * @return false if any interceptor cancels the operation
     */
    public synchronized boolean firePreSave(Object entity) {
        for (EntityInterceptor interceptor : interceptors) {
            if (!interceptor.onPreSave(entity)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Invokes onPostSave for all registered interceptors.
     * 
     * @param entity the entity
     */
    public synchronized void firePostSave(Object entity) {
        for (EntityInterceptor interceptor : interceptors) {
            interceptor.onPostSave(entity);
        }
    }
    
    /**
     * Invokes onPreUpdate for all registered interceptors.
     * 
     * @param entity the entity
     * @return false if any interceptor cancels the operation
     */
    public synchronized boolean firePreUpdate(Object entity) {
        for (EntityInterceptor interceptor : interceptors) {
            if (!interceptor.onPreUpdate(entity)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Invokes onPostUpdate for all registered interceptors.
     * 
     * @param entity the entity
     */
    public synchronized void firePostUpdate(Object entity) {
        for (EntityInterceptor interceptor : interceptors) {
            interceptor.onPostUpdate(entity);
        }
    }
    
    /**
     * Invokes onPreDelete for all registered interceptors.
     * 
     * @param entity the entity
     * @return false if any interceptor cancels the operation
     */
    public synchronized boolean firePreDelete(Object entity) {
        for (EntityInterceptor interceptor : interceptors) {
            if (!interceptor.onPreDelete(entity)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Invokes onPostDelete for all registered interceptors.
     * 
     * @param entity the entity
     */
    public synchronized void firePostDelete(Object entity) {
        for (EntityInterceptor interceptor : interceptors) {
            interceptor.onPostDelete(entity);
        }
    }
    
    /**
     * Invokes onPostLoad for all registered interceptors.
     * 
     * @param entity the entity
     */
    public synchronized void firePostLoad(Object entity) {
        for (EntityInterceptor interceptor : interceptors) {
            interceptor.onPostLoad(entity);
        }
    }
    
    /**
     * Clears all registered interceptors.
     * Use with caution - typically only needed for testing.
     */
    public synchronized void clear() {
        interceptors.clear();
    }
}
