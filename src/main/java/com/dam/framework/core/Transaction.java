package com.dam.framework.core;

/**
 * Represents a database transaction.
 * 
 * @author Dev 1
 */
public interface Transaction {
    
    /**
     * Begin this transaction.
     */
    void begin();
    
    /**
     * Commit this transaction (make changes permanent).
     */
    void commit();
    
    /**
     * Rollback this transaction (undo changes).
     */
    void rollback();
    
    /**
     * Check if this transaction is active.
     * 
     * @return true if active, false otherwise
     */
    boolean isActive();
}
