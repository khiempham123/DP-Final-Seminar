package com.dam.framework.spi;

/**
 * Default naming strategy that preserves original Java names without transformation.
 * This is the framework's default behavior when no custom strategy is configured.
 * 
 * @author DAM Framework
 */
public class DefaultNamingStrategy implements NamingStrategy {
    
    @Override
    public String classToTableName(String className) {
        return className;
    }
    
    @Override
    public String fieldToColumnName(String fieldName) {
        return fieldName;
    }
    
    @Override
    public String joinColumnName(String entityName) {
        return entityName.toLowerCase() + "_id";
    }
    
    @Override
    public String joinTableName(String ownerEntity, String targetEntity) {
        return ownerEntity.toLowerCase() + "_" + targetEntity.toLowerCase();
    }
}
