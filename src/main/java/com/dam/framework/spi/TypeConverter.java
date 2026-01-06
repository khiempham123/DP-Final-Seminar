package com.dam.framework.spi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Strategy Pattern for custom type conversion between Java types and SQL types.
 * Allows users to define custom mapping logic for complex types not supported by default.
 * 
 * <p>Common use cases:
 * <ul>
 *   <li>JSON/XML column mapping to Java objects</li>
 *   <li>Enum to string/integer mapping</li>
 *   <li>Custom date/time format handling</li>
 *   <li>Encrypted field handling</li>
 * </ul>
 * 
 * <p>Example implementation for JSON:
 * <pre>
 * public class JsonTypeConverter implements TypeConverter&lt;MyObject&gt; {
 *     public void setParameter(PreparedStatement ps, int index, MyObject value) throws SQLException {
 *         ps.setString(index, toJson(value));
 *     }
 *     
 *     public MyObject getResult(ResultSet rs, String columnName) throws SQLException {
 *         return fromJson(rs.getString(columnName), MyObject.class);
 *     }
 * }
 * </pre>
 * 
 * @param <T> the Java type this converter handles
 * @author DAM Framework
 */
public interface TypeConverter<T> {
    
    /**
     * Returns the Java type this converter handles.
     * 
     * @return the Java class
     */
    Class<T> getJavaType();
    
    /**
     * Sets a parameter value in a PreparedStatement.
     * 
     * @param ps the PreparedStatement
     * @param index the parameter index (1-based)
     * @param value the Java value to set
     * @throws SQLException if a database error occurs
     */
    void setParameter(PreparedStatement ps, int index, T value) throws SQLException;
    
    /**
     * Retrieves a value from a ResultSet.
     * 
     * @param rs the ResultSet
     * @param columnName the column name
     * @return the Java value
     * @throws SQLException if a database error occurs
     */
    T getResult(ResultSet rs, String columnName) throws SQLException;
    
    /**
     * Returns the SQL type name for CREATE TABLE statements.
     * Examples: "VARCHAR(500)", "TEXT", "INTEGER"
     * 
     * @param dialect the database dialect (mysql, postgresql, sqlserver)
     * @return the SQL type definition
     */
    String getSqlType(String dialect);
}
