package com.dam.framework.dialect;

import com.dam.framework.engine.EntityMetadata;

/**
 * Interface for database-specific SQL generation.
 * 
 * Design Pattern: Strategy Pattern (GoF Pattern #2)
 * Different implementations handle different database systems (MySQL, PostgreSQL, SQL Server).
 * 
 * @author Dev 1
 */
public interface Dialect {
    
    /**
     * Generate INSERT SQL statement.
     * 
     * @param metadata Entity metadata
     * @return SQL INSERT statement
     */
    String getInsertSQL(EntityMetadata metadata);
    
    /**
     * Generate UPDATE SQL statement.
     * 
     * @param metadata Entity metadata
     * @return SQL UPDATE statement
     */
    String getUpdateSQL(EntityMetadata metadata);
    
    /**
     * Generate DELETE SQL statement.
     * 
     * @param metadata Entity metadata
     * @return SQL DELETE statement
     */
    String getDeleteSQL(EntityMetadata metadata);
    
    /**
     * Generate SELECT SQL statement.
     * 
     * @param metadata Entity metadata
     * @return SQL SELECT statement
     */
    String getSelectSQL(EntityMetadata metadata);
    
    /**
     * Generate SELECT by ID SQL statement.
     * 
     * @param metadata Entity metadata
     * @return SQL SELECT statement with WHERE id = ?
     */
    String getSelectByIdSQL(EntityMetadata metadata);
    
    /**
     * Get SQL for pagination (LIMIT/OFFSET).
     * 
     * @param sql The base SQL query
     * @param limit Number of rows to return
     * @param offset Number of rows to skip
     * @return SQL with pagination
     */
    String getPaginationSQL(String sql, int limit, int offset);
    
    /**
     * Map Java type to database column type.
     * 
     * @param javaType Java class type
     * @return Database column type
     */
    String getColumnType(Class<?> javaType);
    
    /**
     * Get the identity column definition for auto-increment.
     * 
     * @return SQL for identity column (e.g., "AUTO_INCREMENT", "SERIAL")
     */
    String getIdentityColumnString();
}
