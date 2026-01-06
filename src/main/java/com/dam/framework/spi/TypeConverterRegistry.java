package com.dam.framework.spi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for custom type converters.
 * Allows users to register custom converters for specific Java types.
 * 
 * <p>Thread-safe singleton implementation.
 * 
 * @author DAM Framework
 */
public class TypeConverterRegistry {
    
    private static final TypeConverterRegistry INSTANCE = new TypeConverterRegistry();
    
    private final Map<Class<?>, TypeConverter<?>> converters = new ConcurrentHashMap<>();
    
    private TypeConverterRegistry() {
        // Private constructor for singleton
    }
    
    /**
     * Gets the singleton instance.
     * 
     * @return the registry instance
     */
    public static TypeConverterRegistry getInstance() {
        return INSTANCE;
    }
    
    /**
     * Registers a custom type converter.
     * 
     * @param converter the converter to register
     * @param <T> the Java type
     * @throws IllegalArgumentException if converter is null or type is already registered
     */
    public <T> void registerConverter(TypeConverter<T> converter) {
        if (converter == null) {
            throw new IllegalArgumentException("TypeConverter cannot be null");
        }
        
        Class<T> javaType = converter.getJavaType();
        if (javaType == null) {
            throw new IllegalArgumentException("TypeConverter.getJavaType() cannot return null");
        }
        
        if (converters.containsKey(javaType)) {
            throw new IllegalArgumentException("TypeConverter already registered for type: " + javaType.getName());
        }
        
        converters.put(javaType, converter);
    }
    
    /**
     * Gets the converter for a specific Java type.
     * 
     * @param javaType the Java class
     * @param <T> the type
     * @return the converter, or null if not registered
     */
    @SuppressWarnings("unchecked")
    public <T> TypeConverter<T> getConverter(Class<T> javaType) {
        return (TypeConverter<T>) converters.get(javaType);
    }
    
    /**
     * Checks if a converter is registered for a type.
     * 
     * @param javaType the Java class
     * @return true if converter exists
     */
    public boolean hasConverter(Class<?> javaType) {
        return converters.containsKey(javaType);
    }
    
    /**
     * Removes a registered converter.
     * 
     * @param javaType the Java class
     */
    public void unregisterConverter(Class<?> javaType) {
        converters.remove(javaType);
    }
    
    /**
     * Clears all registered converters.
     * Use with caution - typically only needed for testing.
     */
    public void clear() {
        converters.clear();
    }
}
