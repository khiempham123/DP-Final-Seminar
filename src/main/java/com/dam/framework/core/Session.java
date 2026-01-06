package com.dam.framework.core;

import com.dam.framework.query.QueryBuilder;

/**
 * Main interface for database operations.
 * A Session is a single-threaded, short-lived object representing a conversation between the application and the database.
 * 
 * Design Pattern: Part of Factory Pattern (created by SessionFactory)
 * 
 * @author Dev 1
 */
public interface Session extends AutoCloseable {
    
    /**
     * Persist a new entity to the database (INSERT operation).
     * 
     * @param entity The entity to save
     * @return The saved entity with generated ID (if applicable)
     */
    <T> T save(T entity);
    
    /**
     * Update an existing entity in the database (UPDATE operation).
     * 
     * @param entity The entity to update
     * @return The updated entity
     */
    <T> T update(T entity);
    
    /**
     * Remove an entity from the database (DELETE operation).
     * 
     * @param entity The entity to delete
     */
    <T> void delete(T entity);
    
    /**
     * Find an entity by its primary key (SELECT operation).
     * 
     * @param entityClass The class of the entity
     * @param id The primary key value
     * @return The found entity or null if not found
     */
    <T> T find(Class<T> entityClass, Object id);
    
    /**
     * Create a query builder for complex queries.
     * 
     * @param entityClass The class of the entity to query
     * @return A QueryBuilder instance
     */
    <T> QueryBuilder<T> createQuery(Class<T> entityClass);
    
    /**
     * Begin a new transaction.
     * 
     * @return The Transaction object
     */
    Transaction beginTransaction();
    
    /**
     * Get the current transaction.
     * 
     * @return The current Transaction or null if no active transaction
     */
    Transaction getTransaction();
    
    /**
     * Get the underlying JDBC connection.
     * Use with caution - for advanced operations only.
     * 
     * @return The JDBC Connection
     */
    java.sql.Connection getConnection();
    
    /**
     * Close this session and release the database connection.
     */
    @Override
    void close();
}
