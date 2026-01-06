package com.dam.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a many-to-one relationship between entities.
 * Used on single object fields to indicate that this entity
 * is the "many" side of the relationship (owns the foreign key).
 * 
 * Example:
 * <pre>
 * {@literal @}Entity
 * public class Employee {
 *     {@literal @}Id
 *     private Long id;
 *     
 *     {@literal @}ManyToOne(fetch = FetchType.EAGER)
 *     {@literal @}JoinColumn(name = "department_id")
 *     private Department department;
 * }
 * </pre>
 * 
 * @author Dev 1
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToOne {
    
    /**
     * The target entity class.
     * Default is void.class which means it will be inferred from the field type.
     */
    Class<?> targetEntity() default void.class;
    
    /**
     * The fetch strategy for loading the related entity.
     * Default is EAGER since it's typically a single object.
     */
    FetchType fetch() default FetchType.EAGER;
    
    /**
     * Cascade operations to apply to the related entity.
     */
    CascadeType[] cascade() default {};
    
    /**
     * Whether the relationship is optional (nullable).
     */
    boolean optional() default true;
}
