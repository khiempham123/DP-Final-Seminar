package com.dam.framework.exception;

/**
 * Exception thrown when a transaction operation fails.
 * This includes commit failures, rollback failures, and invalid transaction states.
 * 
 * @author Dev 1
 */
public class TransactionException extends DAMException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Create exception with a message.
     * 
     * @param message The error message
     */
    public TransactionException(String message) {
        super(message);
    }
    
    /**
     * Create exception with message and cause.
     * 
     * @param message The error message
     * @param cause The underlying cause
     */
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Create exception for commit failure.
     * 
     * @param cause The underlying cause
     * @return TransactionException
     */
    public static TransactionException commitFailed(Throwable cause) {
        return new TransactionException("Failed to commit transaction", cause);
    }
    
    /**
     * Create exception for rollback failure.
     * 
     * @param cause The underlying cause
     * @return TransactionException
     */
    public static TransactionException rollbackFailed(Throwable cause) {
        return new TransactionException("Failed to rollback transaction", cause);
    }
    
    /**
     * Create exception for inactive transaction.
     * 
     * @return TransactionException
     */
    public static TransactionException notActive() {
        return new TransactionException("Transaction is not active");
    }
    
    /**
     * Create exception for already active transaction.
     * 
     * @return TransactionException
     */
    public static TransactionException alreadyActive() {
        return new TransactionException("Transaction is already active");
    }
}
