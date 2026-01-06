package com.dam.framework.dialect;

/**
 * H2 Database Dialect - In-memory database for testing
 * No installation required - perfect for demos and testing
 * 
 * Design Pattern: Strategy Pattern
 */
public class H2Dialect implements Dialect {
    
    @Override
    public String applyPagination(String sql, int limit, int offset) {
        // H2 uses same syntax as MySQL/PostgreSQL
        return sql + " LIMIT " + limit + " OFFSET " + offset;
    }
    
    @Override
    public String getIdentityQuery() {
        return "CALL IDENTITY()";
    }
    
    @Override
    public String getCurrentTimestampFunction() {
        return "CURRENT_TIMESTAMP()";
    }
    
    @Override
    public boolean supportsSequences() {
        return true;
    }
    
    @Override
    public String getSequenceNextValString(String sequenceName) {
        return "NEXT VALUE FOR " + sequenceName;
    }
    
    @Override
    public String getDriverClassName() {
        return "org.h2.Driver";
    }
    
    @Override
    public String getValidationQuery() {
        return "SELECT 1";
    }
}
