package com.dam.framework.spi;

/**
 * Strategy Pattern for customizing entity and column naming conventions.
 * Allows users to define custom rules for converting Java class/field names to database table/column names.
 * 
 * <p>Framework provides two built-in implementations:
 * <ul>
 *   <li>{@link DefaultNamingStrategy} - preserves original names</li>
 *   <li>{@link SnakeCaseNamingStrategy} - converts camelCase to snake_case</li>
 * </ul>
 * 
 * <p>Users can implement this interface to provide custom naming logic.
 * 
 * @author DAM Framework
 */
public interface NamingStrategy {
    
    /**
     * Converts a class name to a table name.
     * 
     * @param className the Java class name (e.g., "UserAccount")
     * @return the database table name (e.g., "user_account")
     */
    String classToTableName(String className);
    
    /**
     * Converts a field name to a column name.
     * 
     * @param fieldName the Java field name (e.g., "firstName")
     * @return the database column name (e.g., "first_name")
     */
    String fieldToColumnName(String fieldName);
    
    /**
     * Generates a join column name for relationships.
     * Default convention: entity_id (e.g., "user_id" for User entity)
     * 
     * @param entityName the related entity name
     * @return the join column name
     */
    String joinColumnName(String entityName);
    
    /**
     * Generates a join table name for many-to-many relationships.
     * Default convention: entity1_entity2 (e.g., "user_role")
     * 
     * @param ownerEntity the owning entity name
     * @param targetEntity the target entity name
     * @return the join table name
     */
    String joinTableName(String ownerEntity, String targetEntity);
}
