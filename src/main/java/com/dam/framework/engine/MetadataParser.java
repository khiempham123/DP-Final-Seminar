package com.dam.framework.engine;

import com.dam.framework.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Parses entity classes and builds metadata using reflection.
 * Supports entity annotations, column mappings, and relationship annotations.
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
        
        // Parse fields and relationships
        List<Field> entityFields = new ArrayList<>();
        List<RelationshipMetadata> relationships = new ArrayList<>();
        Field idField = null;
        
        for (Field field : entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            
            // Check for @Id annotation
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
            }
            
            // Check for relationship annotations
            RelationshipMetadata relMetadata = RelationshipMetadata.parse(field);
            if (relMetadata != null) {
                relationships.add(relMetadata);
                // Relationship fields are not regular columns
                continue;
            }
            
            // Regular field (column)
            entityFields.add(field);
        }
        
        if (idField == null) {
            throw new IllegalArgumentException(
                "Entity " + entityClass.getName() + " must have a field annotated with @Id");
        }
        
        metadata.setIdField(idField);
        metadata.setFields(entityFields);
        metadata.setRelationships(relationships);
        
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
    
    /**
     * Check if a class is an entity.
     * 
     * @param clazz The class to check
     * @return true if the class has @Entity annotation
     */
    public static boolean isEntity(Class<?> clazz) {
        return clazz.isAnnotationPresent(Entity.class);
    }
    
    /**
     * Get the table name for an entity class.
     * 
     * @param entityClass The entity class
     * @return The table name
     */
    public static String getTableName(Class<?> entityClass) {
        if (entityClass.isAnnotationPresent(Table.class)) {
            return entityClass.getAnnotation(Table.class).name();
        }
        return entityClass.getSimpleName();
    }
}
