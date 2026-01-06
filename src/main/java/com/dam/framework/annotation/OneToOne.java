package com.dam.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a one-to-one relationship between entities.
 * Can be used on either side of the relationship.
 * 
 * Example:
 * <pre>
 * {@literal @}Entity
 * public class User {
 *     {@literal @}Id
 *     private Long id;
 *     
 *     {@literal @}OneToOne(fetch = FetchType.LAZY)
 *     {@literal @}JoinColumn(name = "profile_id")
 *     private UserProfile profile;
 * }
 * </pre>
 * 
 * @author Dev 1
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToOne {
    
    /**
     * The field in the target entity that owns the relationship.
     * Used on the non-owning side of the relationship.
     */
    String mappedBy() default "";
    
    /**
     * The target entity class.
     * Default is void.class which means it will be inferred from the field type.
     */
    Class<?> targetEntity() default void.class;
    
    /**
     * The fetch strategy for loading the related entity.
     * Default is EAGER.
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
    
    /**
     * Whether to remove the related entity when this entity is removed.
     */
    boolean orphanRemoval() default false;
}
