package com.dam.framework.dialect;

import com.dam.framework.engine.EntityMetadata;

/**
 * SQL Server-specific SQL dialect implementation.
 * 
 * @author Dev 1
 */
public class SQLServerDialect implements Dialect {
    
    @Override
    public String getInsertSQL(EntityMetadata metadata) {
        // TODO: Dev 1 - Implement SQL Server INSERT
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public String getUpdateSQL(EntityMetadata metadata) {
        // TODO: Dev 1 - Implement SQL Server UPDATE
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public String getDeleteSQL(EntityMetadata metadata) {
        // TODO: Dev 1 - Implement SQL Server DELETE
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public String getSelectSQL(EntityMetadata metadata) {
        // TODO: Dev 1 - Implement SQL Server SELECT
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public String getSelectByIdSQL(EntityMetadata metadata) {
        // TODO: Dev 1 - Implement SQL Server SELECT by ID
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public String getPaginationSQL(String sql, int limit, int offset) {
        // TODO: Dev 1 - Implement SQL Server pagination
        // SQL Server uses: OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
        return sql + " OFFSET " + offset + " ROWS FETCH NEXT " + limit + " ROWS ONLY";
    }
    
    @Override
    public String getColumnType(Class<?> javaType) {
        // TODO: Dev 1 - Map Java types to SQL Server column types
        if (javaType == String.class) return "NVARCHAR(255)";
        if (javaType == Integer.class || javaType == int.class) return "INT";
        if (javaType == Long.class || javaType == long.class) return "BIGINT";
        return "NVARCHAR(255)";
    }
    
    @Override
    public String getIdentityColumnString() {
        return "IDENTITY"; // SQL Server uses IDENTITY
    }
}
