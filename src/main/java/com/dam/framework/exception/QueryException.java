package com.dam.framework.exception;

/**
 * Exception thrown when there is an error executing a query.
 * This includes SQL syntax errors, constraint violations,
 * and query parameter errors.
 * 
 * @author Dev 1
 */
public class QueryException extends DAMException {
    
    private static final long serialVersionUID = 1L;
    
    private String sql;
    private Object[] parameters;
    
    /**
     * Create exception with a message.
     * 
     * @param message The error message
     */
    public QueryException(String message) {
        super(message);
    }
    
    /**
     * Create exception with message and cause.
     * 
     * @param message The error message
     * @param cause The underlying cause
     */
    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Create exception with SQL details.
     * 
     * @param message The error message
     * @param sql The SQL that failed
     * @param cause The underlying cause
     */
    public QueryException(String message, String sql, Throwable cause) {
        super(String.format("%s [SQL: %s]", message, sql), cause);
        this.sql = sql;
    }
    
    /**
     * Create exception with SQL and parameters.
     * 
     * @param message The error message
     * @param sql The SQL that failed
     * @param parameters The parameters used
     * @param cause The underlying cause
     */
    public QueryException(String message, String sql, Object[] parameters, Throwable cause) {
        super(String.format("%s [SQL: %s]", message, sql), cause);
        this.sql = sql;
        this.parameters = parameters;
    }
    
    /**
     * Create exception for SQL syntax error.
     * 
     * @param sql The invalid SQL
     * @param cause The underlying cause
     * @return QueryException
     */
    public static QueryException syntaxError(String sql, Throwable cause) {
        return new QueryException("SQL syntax error", sql, cause);
    }
    
    /**
     * Create exception for constraint violation.
     * 
     * @param constraintName The violated constraint
     * @param cause The underlying cause
     * @return QueryException
     */
    public static QueryException constraintViolation(String constraintName, Throwable cause) {
        return new QueryException(
            String.format("Constraint violation: %s", constraintName),
            cause
        );
    }
    
    /**
     * Create exception for unique constraint violation.
     * 
     * @param fieldName The field with duplicate value
     * @param cause The underlying cause
     * @return QueryException
     */
    public static QueryException uniqueViolation(String fieldName, Throwable cause) {
        return new QueryException(
            String.format("Unique constraint violation on field: %s", fieldName),
            cause
        );
    }
    
    /**
     * Create exception for foreign key violation.
     * 
     * @param cause The underlying cause
     * @return QueryException
     */
    public static QueryException foreignKeyViolation(Throwable cause) {
        return new QueryException("Foreign key constraint violation", cause);
    }
    
    /**
     * Create exception for parameter binding error.
     * 
     * @param paramIndex The parameter index
     * @param paramValue The parameter value
     * @param cause The underlying cause
     * @return QueryException
     */
    public static QueryException parameterError(int paramIndex, Object paramValue, Throwable cause) {
        return new QueryException(
            String.format("Error binding parameter at index %d with value: %s", 
                paramIndex, paramValue),
            cause
        );
    }
    
    /**
     * Get the SQL that failed.
     * 
     * @return The SQL string
     */
    public String getSql() {
        return sql;
    }
    
    /**
     * Get the parameters used.
     * 
     * @return The parameters array
     */
    public Object[] getParameters() {
        return parameters;
    }
}
