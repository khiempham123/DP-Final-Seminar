package com.dam.framework.core;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Implementation of Transaction interface.
 * Manages database transaction lifecycle (begin, commit, rollback).
 * 
 * @author Dev 1
 */
public class TransactionImpl implements Transaction {
    
    private Connection connection;
    private boolean active;
    private boolean committed;
    private boolean rolledBack;
    
    /**
     * Create a new transaction for the given connection.
     * 
     * @param connection The JDBC connection
     */
    public TransactionImpl(Connection connection) {
        this.connection = connection;
        this.active = false;
        this.committed = false;
        this.rolledBack = false;
    }
    
    @Override
    public void begin() {
        if (active) {
            throw new IllegalStateException("Transaction is already active");
        }
        try {
            connection.setAutoCommit(false);
            this.active = true;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to begin transaction", e);
        }
    }
    
    @Override
    public void commit() {
        if (!active) {
            throw new IllegalStateException("Transaction is not active");
        }
        if (committed || rolledBack) {
            throw new IllegalStateException("Transaction has already been completed");
        }
        try {
            connection.commit();
            connection.setAutoCommit(true);
            this.committed = true;
            this.active = false;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to commit transaction", e);
        }
    }
    
    @Override
    public void rollback() {
        if (!active) {
            throw new IllegalStateException("Transaction is not active");
        }
        if (committed || rolledBack) {
            throw new IllegalStateException("Transaction has already been completed");
        }
        try {
            connection.rollback();
            connection.setAutoCommit(true);
            this.rolledBack = true;
            this.active = false;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to rollback transaction", e);
        }
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
    
    /**
     * Check if transaction was committed.
     */
    public boolean isCommitted() {
        return committed;
    }
    
    /**
     * Check if transaction was rolled back.
     */
    public boolean isRolledBack() {
        return rolledBack;
    }
}
