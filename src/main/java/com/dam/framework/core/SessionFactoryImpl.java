package com.dam.framework.core;

import com.dam.framework.dialect.Dialect;

import java.sql.SQLException;

/**
 * Implementation of SessionFactory interface.
 * Creates and manages Session instances.
 * 
 * Design Pattern: Factory Pattern (GoF Pattern #1)
 * 
 * @author Dev 1
 */
public class SessionFactoryImpl implements SessionFactory {
    
    private ConnectionPool connectionPool;
    private Dialect dialect;
    private boolean closed;
    
    // ThreadLocal to store current session per thread
    private ThreadLocal<Session> currentSessionHolder = new ThreadLocal<>();
    
    /**
     * Create a new SessionFactory.
     * 
     * @param connectionPool The connection pool to use
     * @param dialect The database dialect
     */
    public SessionFactoryImpl(ConnectionPool connectionPool, Dialect dialect) {
        this.connectionPool = connectionPool;
        this.dialect = dialect;
        this.closed = false;
    }
    
    @Override
    public Session openSession() {
        checkClosed();
        
        try {
            return new SessionImpl(connectionPool.getConnection(), dialect);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to open session", e);
        }
    }
    
    @Override
    public Session getCurrentSession() {
        checkClosed();
        
        Session session = currentSessionHolder.get();
        
        if (session == null) {
            session = openSession();
            currentSessionHolder.set(session);
        }
        
        return session;
    }
    
    /**
     * Close the current session bound to this thread.
     */
    public void closeCurrentSession() {
        Session session = currentSessionHolder.get();
        if (session != null) {
            session.close();
            currentSessionHolder.remove();
        }
    }
    
    @Override
    public void close() {
        if (!closed) {
            // Close all ThreadLocal sessions
            closeCurrentSession();
            
            // Close connection pool
            if (connectionPool != null) {
                connectionPool.close();
            }
            
            closed = true;
        }
    }
    
    /**
     * Check if the factory is closed.
     */
    public boolean isClosed() {
        return closed;
    }
    
    /**
     * Get the dialect used by this factory.
     */
    public Dialect getDialect() {
        return dialect;
    }
    
    /**
     * Get the connection pool.
     */
    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }
    
    private void checkClosed() {
        if (closed) {
            throw new IllegalStateException("SessionFactory is closed");
        }
    }
}
