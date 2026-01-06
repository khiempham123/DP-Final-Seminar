package com.dam.framework.proxy;

import com.dam.framework.core.Session;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Proxy handler for lazy loading of related entities.
 * 
 * Design Pattern: Proxy Pattern (GoF)
 * Defers the loading of related entities until they are actually accessed.
 * This improves performance by avoiding unnecessary database queries.
 * 
 * Example:
 * <pre>
 * // When accessing a lazy-loaded entity:
 * Department dept = employee.getDepartment(); // Returns proxy
 * dept.getName(); // Triggers actual database load
 * </pre>
 * 
 * @param <T> The entity type being proxied
 * @author Dev 1
 */
public class LazyLoadProxy<T> implements InvocationHandler {
    
    private final Class<T> entityClass;
    private final Object entityId;
    private final Session session;
    
    private T target;
    private boolean loaded;
    private boolean loading;
    
    /**
     * Create a new lazy load proxy.
     * 
     * @param entityClass The class of the entity to load
     * @param entityId The ID of the entity
     * @param session The session to use for loading
     */
    public LazyLoadProxy(Class<T> entityClass, Object entityId, Session session) {
        this.entityClass = entityClass;
        this.entityId = entityId;
        this.session = session;
        this.loaded = false;
        this.loading = false;
    }
    
    /**
     * Create a proxy instance for lazy loading.
     * 
     * @param <T> The entity type
     * @param entityClass The class of the entity
     * @param entityId The ID of the entity
     * @param session The session to use for loading
     * @return A proxy instance
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Class<T> entityClass, Object entityId, Session session) {
        if (entityClass.isInterface()) {
            // For interfaces, use JDK dynamic proxy
            return (T) Proxy.newProxyInstance(
                entityClass.getClassLoader(),
                new Class<?>[] { entityClass, LazyLoadable.class },
                new LazyLoadProxy<>(entityClass, entityId, session)
            );
        } else {
            // For classes, we need to use a different approach
            // Return a wrapper that delegates to the actual entity when loaded
            LazyLoadProxy<T> handler = new LazyLoadProxy<>(entityClass, entityId, session);
            return (T) Proxy.newProxyInstance(
                entityClass.getClassLoader(),
                new Class<?>[] { LazyLoadable.class },
                handler
            );
        }
    }
    
    /**
     * Create a proxy for a concrete class using interface-based proxy.
     * The proxy will implement LazyLoadable interface.
     * 
     * @param <T> The entity type
     * @param entityClass The class of the entity
     * @param entityId The ID of the entity
     * @param session The session to use for loading
     * @param interfaces Additional interfaces to implement
     * @return A proxy instance
     */
    @SuppressWarnings("unchecked")
    public static <T> Object createProxyWithInterfaces(Class<T> entityClass, Object entityId, 
            Session session, Class<?>... interfaces) {
        Class<?>[] allInterfaces = new Class<?>[interfaces.length + 1];
        System.arraycopy(interfaces, 0, allInterfaces, 0, interfaces.length);
        allInterfaces[interfaces.length] = LazyLoadable.class;
        
        return Proxy.newProxyInstance(
            entityClass.getClassLoader(),
            allInterfaces,
            new LazyLoadProxy<>(entityClass, entityId, session)
        );
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        
        // Handle LazyLoadable interface methods
        if (methodName.equals("isLoaded")) {
            return loaded;
        }
        if (methodName.equals("load")) {
            loadEntity();
            return target;
        }
        if (methodName.equals("getEntityClass")) {
            return entityClass;
        }
        if (methodName.equals("getEntityId")) {
            return entityId;
        }
        
        // Handle Object methods without loading
        if (methodName.equals("hashCode")) {
            return entityId != null ? entityId.hashCode() : 0;
        }
        if (methodName.equals("equals")) {
            if (args[0] == null) return false;
            if (Proxy.isProxyClass(args[0].getClass())) {
                InvocationHandler handler = Proxy.getInvocationHandler(args[0]);
                if (handler instanceof LazyLoadProxy) {
                    LazyLoadProxy<?> other = (LazyLoadProxy<?>) handler;
                    return entityClass.equals(other.entityClass) && 
                           (entityId != null && entityId.equals(other.entityId));
                }
            }
            // Load and compare
            loadEntity();
            return target != null && target.equals(args[0]);
        }
        if (methodName.equals("toString")) {
            if (!loaded) {
                return String.format("%s[id=%s, loaded=false]", entityClass.getSimpleName(), entityId);
            }
            return target != null ? target.toString() : "null";
        }
        
        // For any other method, load the entity first
        loadEntity();
        
        if (target == null) {
            throw new IllegalStateException(
                String.format("Entity %s with id %s not found", entityClass.getSimpleName(), entityId)
            );
        }
        
        // Invoke the method on the loaded entity
        return method.invoke(target, args);
    }
    
    /**
     * Load the entity from database if not already loaded.
     */
    private synchronized void loadEntity() {
        if (loaded || loading) {
            return;
        }
        
        loading = true;
        try {
            target = session.find(entityClass, entityId);
            loaded = true;
        } finally {
            loading = false;
        }
    }
    
    /**
     * Check if the entity is loaded.
     * 
     * @return true if loaded
     */
    public boolean isLoaded() {
        return loaded;
    }
    
    /**
     * Get the loaded entity.
     * 
     * @return The entity or null if not loaded
     */
    public T getTarget() {
        return target;
    }
    
    /**
     * Get the entity class.
     * 
     * @return The entity class
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }
    
    /**
     * Get the entity ID.
     * 
     * @return The entity ID
     */
    public Object getEntityId() {
        return entityId;
    }
}
