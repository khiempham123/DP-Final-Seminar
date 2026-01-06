package com.dam.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the foreign key column for relationship mapping.
 * Used together with @ManyToOne, @OneToOne to define the join column.
 * 
 * Example:
 * <pre>
 * {@literal @}ManyToOne
 * {@literal @}JoinColumn(name = "department_id", referencedColumnName = "id")
 * private Department department;
 * </pre>
 * 
 * @author Dev 1
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinColumn {
    
    /**
     * The name of the foreign key column in this table.
     */
    String name() default "";
    
    /**
     * The name of the column in the referenced table.
     * Default is the primary key column of the referenced entity.
     */
    String referencedColumnName() default "";
    
    /**
     * Whether the foreign key column is nullable.
     */
    boolean nullable() default true;
    
    /**
     * Whether the foreign key column is unique.
     */
    boolean unique() default false;
    
    /**
     * Whether the column is insertable.
     */
    boolean insertable() default true;
    
    /**
     * Whether the column is updatable.
     */
    boolean updatable() default true;
    
    /**
     * The column definition SQL fragment.
     */
    String columnDefinition() default "";
}
