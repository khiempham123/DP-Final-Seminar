package com.dam.framework.dialect;

import com.dam.framework.engine.EntityMetadata;

/**
 * PostgreSQL-specific SQL dialect implementation.
 * 
 * @author Dev 1
 */
public class PostgreSQLDialect implements Dialect {
    
    @Override
    public String getInsertSQL(EntityMetadata metadata) {
        // TODO: Dev 1 - Implement PostgreSQL INSERT (may need RETURNING clause)
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public String getUpdateSQL(EntityMetadata metadata) {
        // TODO: Dev 1 - Implement PostgreSQL UPDATE
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public String getDeleteSQL(EntityMetadata metadata) {
        // TODO: Dev 1 - Implement PostgreSQL DELETE
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public String getSelectSQL(EntityMetadata metadata) {
        // TODO: Dev 1 - Implement PostgreSQL SELECT
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public String getSelectByIdSQL(EntityMetadata metadata) {
        // TODO: Dev 1 - Implement PostgreSQL SELECT by ID
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public String getPaginationSQL(String sql, int limit, int offset) {
        // PostgreSQL uses: LIMIT ? OFFSET ?
        return sql + " LIMIT " + limit + " OFFSET " + offset;
    }
    
    @Override
    public String getColumnType(Class<?> javaType) {
        // TODO: Dev 1 - Map Java types to PostgreSQL column types
        if (javaType == String.class) return "VARCHAR(255)";
        if (javaType == Integer.class || javaType == int.class) return "INTEGER";
        if (javaType == Long.class || javaType == long.class) return "BIGINT";
        return "VARCHAR(255)";
    }
    
    @Override
    public String getIdentityColumnString() {
        return "SERIAL"; // PostgreSQL uses SERIAL for auto-increment
    }
}
