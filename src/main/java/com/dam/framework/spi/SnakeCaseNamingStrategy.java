package com.dam.framework.spi;

/**
 * Naming strategy that converts camelCase Java names to snake_case database names.
 * 
 * <p>Examples:
 * <ul>
 *   <li>UserAccount → user_account</li>
 *   <li>firstName → first_name</li>
 *   <li>createdAt → created_at</li>
 * </ul>
 * 
 * @author DAM Framework
 */
public class SnakeCaseNamingStrategy implements NamingStrategy {
    
    @Override
    public String classToTableName(String className) {
        return camelToSnakeCase(className);
    }
    
    @Override
    public String fieldToColumnName(String fieldName) {
        return camelToSnakeCase(fieldName);
    }
    
    @Override
    public String joinColumnName(String entityName) {
        return camelToSnakeCase(entityName) + "_id";
    }
    
    @Override
    public String joinTableName(String ownerEntity, String targetEntity) {
        return camelToSnakeCase(ownerEntity) + "_" + camelToSnakeCase(targetEntity);
    }
    
    /**
     * Converts camelCase string to snake_case.
     * 
     * @param input the camelCase string
     * @return the snake_case string
     */
    private String camelToSnakeCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(input.charAt(0)));
        
        for (int i = 1; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append('_');
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        
        return result.toString();
    }
}
