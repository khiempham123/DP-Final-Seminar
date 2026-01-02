package com.dam.framework.engine;

import com.dam.framework.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Parses entity classes and builds metadata using reflection.
 * 
 * @author Dev 2
 */
public class MetadataParser {
    
    // Cache parsed metadata for performance
    private static final Map<Class<?>, EntityMetadata> metadataCache = new ConcurrentHashMap<>();
    
    /**
     * Parse an entity class and build its metadata.
     * Uses reflection to read annotations and field information.
     * 
     * @param entityClass The entity class to parse
     * @return EntityMetadata object
     */
    public static EntityMetadata parse(Class<?> entityClass) {
        // Check cache first
        if (metadataCache.containsKey(entityClass)) {
            return metadataCache.get(entityClass);
        }
        
        // TODO: Dev 2 - Implement full parsing logic
        // Steps:
        // 1. Check if class has @Entity annotation
        // 2. Get table name from @Table annotation (or use class name)
        // 3. Iterate through all fields
        // 4. Find field with @Id annotation (primary key)
        // 5. For each field, check for @Column annotation
        // 6. Build and return EntityMetadata
        
        EntityMetadata metadata = new EntityMetadata(entityClass);
        
        // Check @Entity annotation
        if (!entityClass.isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException(
                "Class " + entityClass.getName() + " is not annotated with @Entity");
        }
        
        // Get table name
        String tableName = entityClass.getSimpleName();
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = entityClass.getAnnotation(Table.class);
            tableName = tableAnnotation.name();
        }
        metadata.setTableName(tableName);
        
        // Parse fields
        List<Field> entityFields = new ArrayList<>();
        Field idField = null;
        
        for (Field field : entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            entityFields.add(field);
            
            // Check for @Id annotation
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
            }
        }
        
        if (idField == null) {
            throw new IllegalArgumentException(
                "Entity " + entityClass.getName() + " must have a field annotated with @Id");
        }
        
        metadata.setIdField(idField);
        metadata.setFields(entityFields);
        
        // Cache the metadata
        metadataCache.put(entityClass, metadata);
        
        return metadata;
    }
    
    /**
     * Clear the metadata cache.
     */
    public static void clearCache() {
        metadataCache.clear();
    }
}
