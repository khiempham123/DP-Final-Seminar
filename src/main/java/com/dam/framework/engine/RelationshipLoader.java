package com.dam.framework.engine;

import com.dam.framework.annotation.FetchType;
import com.dam.framework.core.Session;
import com.dam.framework.proxy.LazyCollection;
import com.dam.framework.proxy.LazyLoadProxy;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading of related entities based on relationship metadata.
 * Supports both eager and lazy loading strategies.
 * 
 * @author Dev 1
 */
public class RelationshipLoader {
    
    private final Session session;
    private final Connection connection;
    
    /**
     * Create a new relationship loader.
     * 
     * @param session The session for lazy loading
     * @param connection The database connection
     */
    public RelationshipLoader(Session session, Connection connection) {
        this.session = session;
        this.connection = connection;
    }
    
    /**
     * Load related entities for the given entity.
     * 
     * @param entity The entity to load relationships for
     * @param metadata The entity metadata
     */
    public void loadRelationships(Object entity, EntityMetadata metadata) {
        List<RelationshipMetadata> relationships = metadata.getRelationships();
        if (relationships == null || relationships.isEmpty()) {
            return;
        }
        
        for (RelationshipMetadata rel : relationships) {
            loadRelationship(entity, metadata, rel);
        }
    }
    
    /**
     * Load a single relationship.
     */
    private void loadRelationship(Object entity, EntityMetadata metadata, RelationshipMetadata rel) {
        switch (rel.getRelationType()) {
            case MANY_TO_ONE:
            case ONE_TO_ONE:
                loadSingleRelationship(entity, metadata, rel);
                break;
            case ONE_TO_MANY:
                loadCollectionRelationship(entity, metadata, rel);
                break;
        }
    }
    
    /**
     * Load a single entity relationship (ManyToOne, OneToOne).
     */
    private void loadSingleRelationship(Object entity, EntityMetadata metadata, RelationshipMetadata rel) {
        Field field = rel.getField();
        
        // Get the foreign key value from the entity
        Object foreignKeyValue = getForeignKeyValue(entity, rel);
        if (foreignKeyValue == null) {
            return;
        }
        
        if (rel.isEager()) {
            // Eager load: fetch immediately
            Object related = session.find(rel.getTargetEntity(), foreignKeyValue);
            setFieldValue(entity, field, related);
        } else {
            // Lazy load: create proxy
            Object proxy = LazyLoadProxy.createProxy(
                rel.getTargetEntity(), foreignKeyValue, session
            );
            setFieldValue(entity, field, proxy);
        }
    }
    
    /**
     * Load a collection relationship (OneToMany).
     */
    private void loadCollectionRelationship(Object entity, EntityMetadata metadata, RelationshipMetadata rel) {
        Field field = rel.getField();
        Object ownerId = metadata.getIdValue(entity);
        
        if (ownerId == null) {
            return;
        }
        
        if (rel.isEager()) {
            // Eager load: fetch all related entities immediately
            List<?> related = loadRelatedCollection(
                rel.getTargetEntity(), ownerId, rel.getMappedBy()
            );
            setFieldValue(entity, field, related);
        } else {
            // Lazy load: create lazy collection
            @SuppressWarnings("unchecked")
            LazyCollection<?> lazyCollection = new LazyCollection<>(
                (Class<Object>) rel.getTargetEntity(),
                ownerId,
                rel.getMappedBy(),
                this::loadRelatedCollection
            );
            setFieldValue(entity, field, lazyCollection);
        }
    }
    
    /**
     * Load a collection of related entities.
     */
    @SuppressWarnings("unchecked")
    private <E> List<E> loadRelatedCollection(Class<E> elementClass, Object ownerId, String mappedBy) {
        List<E> result = new ArrayList<>();
        
        EntityMetadata targetMetadata = MetadataParser.parse(elementClass);
        
        // Build SQL: SELECT * FROM target_table WHERE mappedBy_id = ?
        String sql = buildSelectByForeignKeySQL(targetMetadata, mappedBy);
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, ownerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    E element = ResultSetMapper.mapRow(rs, targetMetadata);
                    result.add(element);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load related collection", e);
        }
        
        return result;
    }
    
    /**
     * Get the foreign key value for a relationship.
     */
    private Object getForeignKeyValue(Object entity, RelationshipMetadata rel) {
        String joinColumn = rel.getActualJoinColumnName();
        
        // First try to find a field that stores the FK value directly
        Class<?> entityClass = entity.getClass();
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.getName().equals(joinColumn) || 
                (field.getName() + "_id").equals(joinColumn)) {
                field.setAccessible(true);
                try {
                    return field.get(entity);
                } catch (IllegalAccessException e) {
                    // Continue searching
                }
            }
        }
        
        // If we have the related object loaded, get its ID
        Field relField = rel.getField();
        relField.setAccessible(true);
        try {
            Object related = relField.get(entity);
            if (related != null) {
                EntityMetadata relMetadata = MetadataParser.parse(rel.getTargetEntity());
                return relMetadata.getIdValue(related);
            }
        } catch (IllegalAccessException e) {
            // Ignore
        }
        
        return null;
    }
    
    /**
     * Build SELECT SQL for fetching by foreign key.
     */
    private String buildSelectByForeignKeySQL(EntityMetadata metadata, String mappedBy) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append(metadata.getTableName());
        sql.append(" WHERE ");
        
        // The mappedBy field in the target entity holds the FK
        sql.append(mappedBy).append("_id");
        sql.append(" = ?");
        
        return sql.toString();
    }
    
    /**
     * Set field value using reflection.
     */
    private void setFieldValue(Object entity, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(entity, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot set field: " + field.getName(), e);
        }
    }
}
