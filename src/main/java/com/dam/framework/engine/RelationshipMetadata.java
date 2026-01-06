package com.dam.framework.engine;

import com.dam.framework.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Holds metadata about entity relationships.
 * Parsed from @OneToMany, @ManyToOne, @OneToOne annotations.
 * 
 * @author Dev 1
 */
public class RelationshipMetadata {
    
    /**
     * Type of relationship.
     */
    public enum RelationType {
        ONE_TO_ONE,
        ONE_TO_MANY,
        MANY_TO_ONE
    }
    
    private final Field field;
    private final RelationType relationType;
    private final Class<?> targetEntity;
    private final String mappedBy;
    private final FetchType fetchType;
    private final CascadeType[] cascadeTypes;
    private final boolean optional;
    private final boolean orphanRemoval;
    
    // JoinColumn info
    private final String joinColumnName;
    private final String referencedColumnName;
    
    private RelationshipMetadata(Builder builder) {
        this.field = builder.field;
        this.relationType = builder.relationType;
        this.targetEntity = builder.targetEntity;
        this.mappedBy = builder.mappedBy;
        this.fetchType = builder.fetchType;
        this.cascadeTypes = builder.cascadeTypes;
        this.optional = builder.optional;
        this.orphanRemoval = builder.orphanRemoval;
        this.joinColumnName = builder.joinColumnName;
        this.referencedColumnName = builder.referencedColumnName;
    }
    
    /**
     * Parse relationship metadata from a field.
     * 
     * @param field The field to parse
     * @return RelationshipMetadata or null if not a relationship field
     */
    public static RelationshipMetadata parse(Field field) {
        Builder builder = new Builder(field);
        
        if (field.isAnnotationPresent(OneToMany.class)) {
            OneToMany annotation = field.getAnnotation(OneToMany.class);
            builder.relationType(RelationType.ONE_TO_MANY)
                   .mappedBy(annotation.mappedBy())
                   .fetchType(annotation.fetch())
                   .cascadeTypes(annotation.cascade())
                   .orphanRemoval(annotation.orphanRemoval());
            
            // Get target entity from generic type
            Class<?> targetEntity = annotation.targetEntity();
            if (targetEntity == void.class) {
                targetEntity = getCollectionGenericType(field);
            }
            builder.targetEntity(targetEntity);
            
        } else if (field.isAnnotationPresent(ManyToOne.class)) {
            ManyToOne annotation = field.getAnnotation(ManyToOne.class);
            builder.relationType(RelationType.MANY_TO_ONE)
                   .fetchType(annotation.fetch())
                   .cascadeTypes(annotation.cascade())
                   .optional(annotation.optional());
            
            Class<?> targetEntity = annotation.targetEntity();
            if (targetEntity == void.class) {
                targetEntity = field.getType();
            }
            builder.targetEntity(targetEntity);
            
            // Parse JoinColumn
            if (field.isAnnotationPresent(JoinColumn.class)) {
                JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                builder.joinColumnName(joinColumn.name())
                       .referencedColumnName(joinColumn.referencedColumnName());
            }
            
        } else if (field.isAnnotationPresent(OneToOne.class)) {
            OneToOne annotation = field.getAnnotation(OneToOne.class);
            builder.relationType(RelationType.ONE_TO_ONE)
                   .mappedBy(annotation.mappedBy())
                   .fetchType(annotation.fetch())
                   .cascadeTypes(annotation.cascade())
                   .optional(annotation.optional())
                   .orphanRemoval(annotation.orphanRemoval());
            
            Class<?> targetEntity = annotation.targetEntity();
            if (targetEntity == void.class) {
                targetEntity = field.getType();
            }
            builder.targetEntity(targetEntity);
            
            // Parse JoinColumn
            if (field.isAnnotationPresent(JoinColumn.class)) {
                JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                builder.joinColumnName(joinColumn.name())
                       .referencedColumnName(joinColumn.referencedColumnName());
            }
            
        } else {
            return null;
        }
        
        return builder.build();
    }
    
    /**
     * Get the generic type parameter from a collection field.
     */
    private static Class<?> getCollectionGenericType(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericType;
            Type[] typeArgs = pt.getActualTypeArguments();
            if (typeArgs.length > 0 && typeArgs[0] instanceof Class) {
                return (Class<?>) typeArgs[0];
            }
        }
        return Object.class;
    }
    
    // Getters
    
    public Field getField() {
        return field;
    }
    
    public RelationType getRelationType() {
        return relationType;
    }
    
    public Class<?> getTargetEntity() {
        return targetEntity;
    }
    
    public String getMappedBy() {
        return mappedBy;
    }
    
    public FetchType getFetchType() {
        return fetchType;
    }
    
    public CascadeType[] getCascadeTypes() {
        return cascadeTypes;
    }
    
    public boolean isOptional() {
        return optional;
    }
    
    public boolean isOrphanRemoval() {
        return orphanRemoval;
    }
    
    public String getJoinColumnName() {
        return joinColumnName;
    }
    
    public String getReferencedColumnName() {
        return referencedColumnName;
    }
    
    public boolean isLazy() {
        return fetchType == FetchType.LAZY;
    }
    
    public boolean isEager() {
        return fetchType == FetchType.EAGER;
    }
    
    public boolean hasCascade(CascadeType type) {
        if (cascadeTypes == null) return false;
        for (CascadeType ct : cascadeTypes) {
            if (ct == CascadeType.ALL || ct == type) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if this is the owning side of the relationship.
     * The owning side has the foreign key (JoinColumn).
     */
    public boolean isOwningSide() {
        return mappedBy == null || mappedBy.isEmpty();
    }
    
    /**
     * Get the actual join column name to use.
     * If not specified, generates a default name.
     */
    public String getActualJoinColumnName() {
        if (joinColumnName != null && !joinColumnName.isEmpty()) {
            return joinColumnName;
        }
        // Default: field_name + "_id"
        return field.getName() + "_id";
    }
    
    @Override
    public String toString() {
        return String.format("RelationshipMetadata{field=%s, type=%s, target=%s, fetch=%s}", 
            field.getName(), relationType, targetEntity.getSimpleName(), fetchType);
    }
    
    /**
     * Builder for RelationshipMetadata.
     */
    private static class Builder {
        private final Field field;
        private RelationType relationType;
        private Class<?> targetEntity;
        private String mappedBy;
        private FetchType fetchType = FetchType.LAZY;
        private CascadeType[] cascadeTypes = new CascadeType[0];
        private boolean optional = true;
        private boolean orphanRemoval = false;
        private String joinColumnName;
        private String referencedColumnName;
        
        Builder(Field field) {
            this.field = field;
        }
        
        Builder relationType(RelationType relationType) {
            this.relationType = relationType;
            return this;
        }
        
        Builder targetEntity(Class<?> targetEntity) {
            this.targetEntity = targetEntity;
            return this;
        }
        
        Builder mappedBy(String mappedBy) {
            this.mappedBy = mappedBy;
            return this;
        }
        
        Builder fetchType(FetchType fetchType) {
            this.fetchType = fetchType;
            return this;
        }
        
        Builder cascadeTypes(CascadeType[] cascadeTypes) {
            this.cascadeTypes = cascadeTypes;
            return this;
        }
        
        Builder optional(boolean optional) {
            this.optional = optional;
            return this;
        }
        
        Builder orphanRemoval(boolean orphanRemoval) {
            this.orphanRemoval = orphanRemoval;
            return this;
        }
        
        Builder joinColumnName(String joinColumnName) {
            this.joinColumnName = joinColumnName;
            return this;
        }
        
        Builder referencedColumnName(String referencedColumnName) {
            this.referencedColumnName = referencedColumnName;
            return this;
        }
        
        RelationshipMetadata build() {
            return new RelationshipMetadata(this);
        }
    }
}
