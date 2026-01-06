package com.dam.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a one-to-many relationship between entities.
 * Used on collection fields (List, Set) to indicate that
 * this entity is the "one" side of the relationship.
 * 
 * Example:
 * <pre>
 * {@literal @}Entity
 * public class Department {
 *     {@literal @}Id
 *     private Long id;
 *     
 *     {@literal @}OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
 *     private List&lt;Employee&gt; employees;
 * }
 * </pre>
 * 
 * @author Dev 1
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {
    
    /**
     * The field in the target entity that owns the relationship.
     * This is the foreign key field in the "many" side.
     */
    String mappedBy() default "";
    
    /**
     * The target entity class.
     * Default is void.class which means it will be inferred from the generic type.
     */
    Class<?> targetEntity() default void.class;
    
    /**
     * The fetch strategy for loading related entities.
     * Default is LAZY for performance.
     */
    FetchType fetch() default FetchType.LAZY;
    
    /**
     * Cascade operations to apply to related entities.
     */
    CascadeType[] cascade() default {};
    
    /**
     * Whether to remove orphaned entities when they are removed from the collection.
     */
    boolean orphanRemoval() default false;
}
