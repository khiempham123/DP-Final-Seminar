package com.dam.framework.query;

import com.dam.framework.core.SessionImpl;
import com.dam.framework.engine.EntityMetadata;
import com.dam.framework.engine.MetadataParser;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

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
    private SessionImpl session;
    private EntityMetadata metadata;
    
    private List<String> selectColumns;
    private List<WhereClause> whereClauses;
    private List<String> groupByColumns;
    private String havingClause;
    private List<Object> havingParameters;
    private List<String> orderByColumns;
    private Integer limit;
    private Integer offset;
    
    /**
     * Constructor for standalone QueryBuilder (without session).
     */
    public QueryBuilder(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.metadata = MetadataParser.parse(entityClass);
        initCollections();
    }
    
    /**
     * Constructor with session for execution.
     */
    public QueryBuilder(Class<T> entityClass, SessionImpl session) {
        this.entityClass = entityClass;
        this.session = session;
        this.metadata = MetadataParser.parse(entityClass);
        initCollections();
    }
    
    private void initCollections() {
        this.selectColumns = new ArrayList<>();
        this.whereClauses = new ArrayList<>();
        this.groupByColumns = new ArrayList<>();
        this.orderByColumns = new ArrayList<>();
        this.havingParameters = new ArrayList<>();
    }
    
    /**
     * Specify columns to select.
     * If not called, all columns are selected.
     * 
     * @param columns Column names or aggregate functions
     * @return This QueryBuilder
     */
    public QueryBuilder<T> select(String... columns) {
        for (String column : columns) {
            this.selectColumns.add(column);
        }
        return this;
    }
    
    /**
     * Add a WHERE clause with AND conjunction.
     * 
     * @param field Field name
     * @param operator Comparison operator (=, !=, >, <, >=, <=, LIKE, IN)
     * @param value Value to compare
     * @return This QueryBuilder
     */
    public QueryBuilder<T> where(String field, String operator, Object value) {
        this.whereClauses.add(new WhereClause(field, operator, value, "AND"));
        return this;
    }
    
    /**
     * Add a WHERE clause with OR conjunction.
     * 
     * @param field Field name
     * @param operator Comparison operator
     * @param value Value to compare
     * @return This QueryBuilder
     */
    public QueryBuilder<T> orWhere(String field, String operator, Object value) {
        this.whereClauses.add(new WhereClause(field, operator, value, "OR"));
        return this;
    }
    
    /**
     * Add WHERE IS NULL clause.
     */
    public QueryBuilder<T> whereNull(String field) {
        this.whereClauses.add(new WhereClause(field, "IS NULL", null, "AND"));
        return this;
    }
    
    /**
     * Add WHERE IS NOT NULL clause.
     */
    public QueryBuilder<T> whereNotNull(String field) {
        this.whereClauses.add(new WhereClause(field, "IS NOT NULL", null, "AND"));
        return this;
    }
    
    /**
     * Add WHERE BETWEEN clause.
     */
    public QueryBuilder<T> whereBetween(String field, Object start, Object end) {
        this.whereClauses.add(new WhereClause(field, "BETWEEN", new Object[]{start, end}, "AND"));
        return this;
    }
    
    /**
     * Add GROUP BY clause.
     * 
     * @param columns Columns to group by
     * @return This QueryBuilder
     */
    public QueryBuilder<T> groupBy(String... columns) {
        for (String column : columns) {
            this.groupByColumns.add(column);
        }
        return this;
    }
    
    /**
     * Add HAVING clause (must be used with GROUP BY).
     * 
     * @param condition HAVING condition (e.g., "COUNT(*) > 5")
     * @return This QueryBuilder
     */
    public QueryBuilder<T> having(String condition) {
        this.havingClause = condition;
        return this;
    }
    
    /**
     * Add HAVING clause with parameter.
     */
    public QueryBuilder<T> having(String condition, Object... params) {
        this.havingClause = condition;
        for (Object param : params) {
            this.havingParameters.add(param);
        }
        return this;
    }
    
    /**
     * Add ORDER BY clause.
     * 
     * @param columns Columns to order by (e.g., "name ASC", "age DESC")
     * @return This QueryBuilder
     */
    public QueryBuilder<T> orderBy(String... columns) {
        for (String column : columns) {
            this.orderByColumns.add(column);
        }
        return this;
    }
    
    /**
     * Set LIMIT for pagination.
     * 
     * @param limit Number of rows to return
     * @return This QueryBuilder
     */
    public QueryBuilder<T> limit(int limit) {
        this.limit = limit;
        return this;
    }
    
    /**
     * Set OFFSET for pagination.
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
        if (session == null) {
            throw new IllegalStateException("QueryBuilder requires a Session to execute. Use Session.createQuery().");
        }
        
        String sql = buildSQL();
        List<Object> params = getParameters();
        
        return session.executeQuery(sql, params, entityClass);
    }
    
    /**
     * Execute and return single result or null.
     */
    public T executeFirst() {
        this.limit = 1;
        List<T> results = execute();
        return results.isEmpty() ? null : results.get(0);
    }
    
    /**
     * Execute COUNT query.
     */
    public long count() {
        if (session == null) {
            throw new IllegalStateException("QueryBuilder requires a Session to execute count.");
        }
        
        String sql = buildCountSQL();
        List<Object> params = getParameters();
        
        return session.executeCount(sql, params);
    }
    
    /**
     * Build the SQL query string.
     * 
     * @return SQL query string
     */
    public String buildSQL() {
        StringBuilder sql = new StringBuilder();
        
        // SELECT clause
        sql.append("SELECT ");
        if (selectColumns.isEmpty()) {
            // Select all columns from metadata
            StringJoiner columns = new StringJoiner(", ");
            for (var field : metadata.getFields()) {
                columns.add(metadata.getColumnName(field));
            }
            sql.append(columns);
        } else {
            sql.append(String.join(", ", selectColumns));
        }
        
        // FROM clause
        sql.append(" FROM ").append(metadata.getTableName());
        
        // WHERE clause
        if (!whereClauses.isEmpty()) {
            sql.append(" WHERE ");
            buildWhereClause(sql);
        }
        
        // GROUP BY clause
        if (!groupByColumns.isEmpty()) {
            sql.append(" GROUP BY ").append(String.join(", ", groupByColumns));
        }
        
        // HAVING clause
        if (havingClause != null && !havingClause.isEmpty()) {
            sql.append(" HAVING ").append(havingClause);
        }
        
        // ORDER BY clause
        if (!orderByColumns.isEmpty()) {
            sql.append(" ORDER BY ").append(String.join(", ", orderByColumns));
        }
        
        // LIMIT and OFFSET
        if (limit != null) {
            sql.append(" LIMIT ").append(limit);
        }
        if (offset != null) {
            sql.append(" OFFSET ").append(offset);
        }
        
        return sql.toString();
    }
    
    /**
     * Build COUNT SQL.
     */
    private String buildCountSQL() {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ");
        sql.append(metadata.getTableName());
        
        if (!whereClauses.isEmpty()) {
            sql.append(" WHERE ");
            buildWhereClause(sql);
        }
        
        return sql.toString();
    }
    
    /**
     * Build WHERE clause.
     */
    private void buildWhereClause(StringBuilder sql) {
        for (int i = 0; i < whereClauses.size(); i++) {
            WhereClause clause = whereClauses.get(i);
            
            if (i > 0) {
                sql.append(" ").append(clause.getConjunction()).append(" ");
            }
            
            sql.append(clause.getField());
            
            if (clause.getOperator().equals("IS NULL") || clause.getOperator().equals("IS NOT NULL")) {
                sql.append(" ").append(clause.getOperator());
            } else if (clause.getOperator().equals("BETWEEN")) {
                sql.append(" BETWEEN ? AND ?");
            } else if (clause.getOperator().equalsIgnoreCase("IN")) {
                Object[] values = (Object[]) clause.getValue();
                StringJoiner placeholders = new StringJoiner(", ", "(", ")");
                for (int j = 0; j < values.length; j++) {
                    placeholders.add("?");
                }
                sql.append(" IN ").append(placeholders);
            } else {
                sql.append(" ").append(clause.getOperator()).append(" ?");
            }
        }
    }
    
    /**
     * Get parameters for PreparedStatement binding.
     * 
     * @return List of parameter values
     */
    public List<Object> getParameters() {
        List<Object> params = new ArrayList<>();
        
        for (WhereClause clause : whereClauses) {
            if (clause.getValue() == null) {
                continue; // IS NULL / IS NOT NULL don't need parameters
            }
            
            if (clause.getOperator().equals("BETWEEN")) {
                Object[] range = (Object[]) clause.getValue();
                params.add(range[0]);
                params.add(range[1]);
            } else if (clause.getOperator().equalsIgnoreCase("IN")) {
                Object[] values = (Object[]) clause.getValue();
                for (Object value : values) {
                    params.add(value);
                }
            } else {
                params.add(clause.getValue());
            }
        }
        
        // Add HAVING parameters
        params.addAll(havingParameters);
        
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
