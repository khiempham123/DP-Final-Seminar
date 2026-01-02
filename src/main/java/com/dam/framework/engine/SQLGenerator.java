package com.dam.framework.engine;

import com.dam.framework.annotation.Column;
import java.lang.reflect.Field;
import java.util.StringJoiner;

/**
 * Generates SQL statements from entity metadata.
 * 
 * @author Dev 2
 */
public class SQLGenerator {
    
    /**
     * Generate INSERT SQL statement.
     * 
     * @param metadata Entity metadata
     * @return SQL INSERT statement
     */
    public static String generateInsertSQL(EntityMetadata metadata) {
        // TODO: Dev 2 - Generate INSERT statement
        // Example: INSERT INTO users (name, email) VALUES (?, ?)
        
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(metadata.getTableName()).append(" (");
        
        StringJoiner columns = new StringJoiner(", ");
        StringJoiner values = new StringJoiner(", ");
        
        for (Field field : metadata.getFields()) {
            // Skip ID field if it's auto-generated
            if (field.equals(metadata.getIdField())) {
                // TODO: Check if it's auto-generated
                continue;
            }
            columns.add(metadata.getColumnName(field));
            values.add("?");
        }
        
        sql.append(columns).append(") VALUES (").append(values).append(")");
        return sql.toString();
    }
    
    /**
     * Generate UPDATE SQL statement.
     * 
     * @param metadata Entity metadata
     * @return SQL UPDATE statement
     */
    public static String generateUpdateSQL(EntityMetadata metadata) {
        // TODO: Dev 2 - Generate UPDATE statement
        // Example: UPDATE users SET name=?, email=? WHERE id=?
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    /**
     * Generate DELETE SQL statement.
     * 
     * @param metadata Entity metadata
     * @return SQL DELETE statement
     */
    public static String generateDeleteSQL(EntityMetadata metadata) {
        // TODO: Dev 2 - Generate DELETE statement
        // Example: DELETE FROM users WHERE id=?
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    /**
     * Generate SELECT SQL statement.
     * 
     * @param metadata Entity metadata
     * @return SQL SELECT statement
     */
    public static String generateSelectSQL(EntityMetadata metadata) {
        // TODO: Dev 2 - Generate SELECT statement
        // Example: SELECT id, name, email FROM users
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    /**
     * Generate SELECT by ID SQL statement.
     * 
     * @param metadata Entity metadata
     * @return SQL SELECT statement with WHERE clause
     */
    public static String generateSelectByIdSQL(EntityMetadata metadata) {
        // TODO: Dev 2 - Generate SELECT by ID statement
        // Example: SELECT id, name, email FROM users WHERE id=?
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
