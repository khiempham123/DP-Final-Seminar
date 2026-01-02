package com.dam.framework.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for constructing complex SQL queries.
 * 
 * Design Pattern: Builder Pattern (GoF Pattern #4)
 * Provides a fluent interface for step-by-step query construction.
 * 
 * @author Dev 2
 */
public class QueryBuilder<T> {
    
    private Class<T> entityClass;
    private List<String> selectColumns;
    private List<WhereClause> whereClauses;
    private List<String> groupByColumns;
    private String havingClause;
    private List<String> orderByColumns;
    private Integer limit;
    private Integer offset;
    
    public QueryBuilder(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.selectColumns = new ArrayList<>();
        this.whereClauses = new ArrayList<>();
        this.groupByColumns = new ArrayList<>();
        this.orderByColumns = new ArrayList<>();
    }
    
    /**
     * Specify columns to select.
     * If not called, SELECT * is used.
     * 
     * @param columns Column names
     * @return This QueryBuilder
     */
    public QueryBuilder<T> select(String... columns) {
        for (String column : columns) {
            this.selectColumns.add(column);
        }
        return this;
    }
    
    /**
     * Add a WHERE clause.
     * 
     * @param field Field name
     * @param operator Comparison operator (=, !=, >, <, >=, <=, LIKE)
     * @param value Value to compare
     * @return This QueryBuilder
     */
    public QueryBuilder<T> where(String field, String operator, Object value) {
        // TODO: Dev 2 - Add WHERE clause
        this.whereClauses.add(new WhereClause(field, operator, value, "AND"));
        return this;
    }
    
    /**
     * Add an OR WHERE clause.
     * 
     * @param field Field name
     * @param operator Comparison operator
     * @param value Value to compare
     * @return This QueryBuilder
     */
    public QueryBuilder<T> orWhere(String field, String operator, Object value) {
        // TODO: Dev 2 - Add OR WHERE clause
        this.whereClauses.add(new WhereClause(field, operator, value, "OR"));
        return this;
    }
    
    /**
     * Add GROUP BY clause.
     * 
     * @param columns Columns to group by
     * @return This QueryBuilder
     */
    public QueryBuilder<T> groupBy(String... columns) {
        // TODO: Dev 2 - Add GROUP BY
        for (String column : columns) {
            this.groupByColumns.add(column);
        }
        return this;
    }
    
    /**
     * Add HAVING clause (must be used with GROUP BY).
     * 
     * @param condition HAVING condition
     * @return This QueryBuilder
     */
    public QueryBuilder<T> having(String condition) {
        // TODO: Dev 2 - Add HAVING clause
        this.havingClause = condition;
        return this;
    }
    
    /**
     * Add ORDER BY clause.
     * 
     * @param columns Columns to order by (append " DESC" for descending)
     * @return This QueryBuilder
     */
    public QueryBuilder<T> orderBy(String... columns) {
        // TODO: Dev 2 - Add ORDER BY
        for (String column : columns) {
            this.orderByColumns.add(column);
        }
        return this;
    }
    
    /**
     * Set limit for result set (pagination).
     * 
     * @param limit Number of rows to return
     * @return This QueryBuilder
     */
    public QueryBuilder<T> limit(int limit) {
        this.limit = limit;
        return this;
    }
    
    /**
     * Set offset for result set (pagination).
     * 
     * @param offset Number of rows to skip
     * @return This QueryBuilder
     */
    public QueryBuilder<T> offset(int offset) {
        this.offset = offset;
        return this;
    }
    
    /**
     * Execute the query and return results.
     * 
     * @return List of entities
     */
    public List<T> execute() {
        // TODO: Dev 2 - Build SQL and execute through Session
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    /**
     * Build the SQL query string.
     * 
     * @return SQL query string
     */
    public String buildSQL() {
        // TODO: Dev 2 - Implement SQL building logic
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    /**
     * Get parameters for PreparedStatement binding.
     * 
     * @return List of parameter values
     */
    public List<Object> getParameters() {
        List<Object> params = new ArrayList<>();
        for (WhereClause clause : whereClauses) {
            params.add(clause.getValue());
        }
        return params;
    }
    
    /**
     * Internal class to represent a WHERE clause.
     */
    private static class WhereClause {
        private String field;
        private String operator;
        private Object value;
        private String conjunction; // AND or OR
        
        public WhereClause(String field, String operator, Object value, String conjunction) {
            this.field = field;
            this.operator = operator;
            this.value = value;
            this.conjunction = conjunction;
        }
        
        public String getField() { return field; }
        public String getOperator() { return operator; }
        public Object getValue() { return value; }
        public String getConjunction() { return conjunction; }
    }
}
