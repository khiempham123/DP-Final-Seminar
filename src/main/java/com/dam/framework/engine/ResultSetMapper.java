package com.dam.framework.engine;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Maps ResultSet rows to entity objects using reflection.
 * 
 * @author Dev 2
 */
public class ResultSetMapper {
    
    /**
     * Map a single ResultSet row to an entity object.
     * 
     * @param resultSet The ResultSet (cursor should be on the row to map)
     * @param metadata The entity metadata
     * @return The mapped entity object
     */
    public static <T> T mapRow(ResultSet resultSet, EntityMetadata metadata) throws SQLException {
        try {
            @SuppressWarnings("unchecked")
            Class<T> entityClass = (Class<T>) metadata.getEntityClass();
            T entity = entityClass.getDeclaredConstructor().newInstance();
            
            for (Field field : metadata.getFields()) {
                String columnName = metadata.getColumnName(field);
                Object value = getValueFromResultSet(resultSet, columnName, field.getType());
                
                if (value != null) {
                    metadata.setFieldValue(entity, field, value);
                }
            }
            
            return entity;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to create entity instance: " + metadata.getEntityClass().getName(), e);
        }
    }
    
    /**
     * Map all ResultSet rows to a list of entity objects.
     * 
     * @param resultSet The ResultSet
     * @param metadata The entity metadata
     * @return List of mapped entity objects
     */
    public static <T> List<T> mapRows(ResultSet resultSet, EntityMetadata metadata) throws SQLException {
        List<T> results = new ArrayList<>();
        
        while (resultSet.next()) {
            T entity = mapRow(resultSet, metadata);
            results.add(entity);
        }
        
        return results;
    }
    
    /**
     * Get value from ResultSet with proper type conversion.
     * 
     * @param resultSet The ResultSet
     * @param columnName The column name
     * @param targetType The Java type to convert to
     * @return The converted value
     */
    private static Object getValueFromResultSet(ResultSet resultSet, String columnName, Class<?> targetType) 
            throws SQLException {
        Object value = resultSet.getObject(columnName);
        
        if (value == null) {
            return null;
        }
        
        // Handle type conversions
        if (targetType == String.class) {
            return resultSet.getString(columnName);
        }
        if (targetType == Integer.class || targetType == int.class) {
            return resultSet.getInt(columnName);
        }
        if (targetType == Long.class || targetType == long.class) {
            return resultSet.getLong(columnName);
        }
        if (targetType == Double.class || targetType == double.class) {
            return resultSet.getDouble(columnName);
        }
        if (targetType == Float.class || targetType == float.class) {
            return resultSet.getFloat(columnName);
        }
        if (targetType == Boolean.class || targetType == boolean.class) {
            return resultSet.getBoolean(columnName);
        }
        if (targetType == Short.class || targetType == short.class) {
            return resultSet.getShort(columnName);
        }
        if (targetType == Byte.class || targetType == byte.class) {
            return resultSet.getByte(columnName);
        }
        if (targetType == BigDecimal.class) {
            return resultSet.getBigDecimal(columnName);
        }
        if (targetType == Date.class) {
            return resultSet.getDate(columnName);
        }
        if (targetType == Timestamp.class) {
            return resultSet.getTimestamp(columnName);
        }
        if (targetType == LocalDate.class) {
            java.sql.Date sqlDate = resultSet.getDate(columnName);
            return sqlDate != null ? sqlDate.toLocalDate() : null;
        }
        if (targetType == LocalDateTime.class) {
            Timestamp timestamp = resultSet.getTimestamp(columnName);
            return timestamp != null ? timestamp.toLocalDateTime() : null;
        }
        if (targetType == byte[].class) {
            return resultSet.getBytes(columnName);
        }
        
        // Default: return as-is
        return value;
    }
    
    /**
     * Map a single column value from ResultSet.
     * Useful for aggregate queries like COUNT, SUM, etc.
     */
    public static Object mapSingleValue(ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getObject(columnIndex);
    }
    
    /**
     * Map a single Long value (useful for COUNT queries).
     */
    public static Long mapLongValue(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return resultSet.getLong(1);
        }
        return 0L;
    }
}
