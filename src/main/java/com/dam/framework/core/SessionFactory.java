package com.dam.framework.core;

/**
 * Factory for creating Session instances.
 * A SessionFactory is an expensive-to-create, thread-safe object intended to be shared by all application threads.
 * 
 * Design Pattern: Factory Pattern (GoF Pattern #1)
 * 
 * @author Dev 1
 */
public interface SessionFactory extends AutoCloseable {
    
    /**
     * Open a new Session.
     * 
     * @return A new Session instance
     */
    Session openSession();
    
    /**
     * Get the current session bound to the current thread.
     * 
     * @return The current Session
     */
    Session getCurrentSession();
    
    /**
     * Close this SessionFactory and release all resources.
     */
    @Override
    void close();
}
