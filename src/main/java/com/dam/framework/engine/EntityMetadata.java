package com.dam.framework.engine;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Holds metadata about an entity class.
 * This includes table name, columns, primary key, etc.
 * 
 * @author Dev 2
 */
public class EntityMetadata {
    
    private Class<?> entityClass;
    private String tableName;
    private Field idField;
    private List<Field> fields;
    
    public EntityMetadata(Class<?> entityClass) {
        this.entityClass = entityClass;
        // TODO: Dev 2 - Parse annotations to populate metadata
    }
    
    public Class<?> getEntityClass() {
        return entityClass;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public Field getIdField() {
        return idField;
    }
    
    public void setIdField(Field idField) {
        this.idField = idField;
    }
    
    public List<Field> getFields() {
        return fields;
    }
    
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
    
    /**
     * Get column name for a field.
     * If @Column annotation is present, use that name, otherwise use field name.
     * 
     * @param field The field
     * @return Column name
     */
    public String getColumnName(Field field) {
        // TODO: Dev 2 - Check for @Column annotation and return appropriate name
        return field.getName();
    }
}
