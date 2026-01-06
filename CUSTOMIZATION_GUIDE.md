# DAM Framework - Customization Guide

## Overview

The DAM Framework provides controlled customization points that allow developers to extend and configure the framework behavior without compromising stability. The framework follows a **strict rule-based customization model** where only specific, well-defined aspects can be customized.

## Customization Philosophy

**What can be customized:**
- ✅ Naming conventions (table/column naming)
- ✅ Connection pool settings (within safe limits)
- ✅ Entity lifecycle hooks (interceptors)
- ✅ Type conversion for custom data types
- ✅ Logging and metrics

**What cannot be customized:**
- ❌ Core CRUD operation logic
- ❌ SQL generation algorithms
- ❌ Transaction management behavior
- ❌ Metadata parsing logic
- ❌ Session lifecycle management

## 1. Naming Strategy Customization

### Purpose
Control how Java class/field names are converted to database table/column names.

### Built-in Strategies

#### DefaultNamingStrategy (Default)
Preserves original Java names without transformation.

```java
// Java: UserAccount → Table: UserAccount
// Java: firstName → Column: firstName
Configuration config = Configuration.getInstance();
// Default strategy is already active
```

#### SnakeCaseNamingStrategy
Converts camelCase to snake_case.

```java
// Java: UserAccount → Table: user_account
// Java: firstName → Column: first_name
Configuration config = Configuration.getInstance();
config.setNamingStrategy(new SnakeCaseNamingStrategy());
SessionFactory factory = config.buildSessionFactory();
```

### Custom Strategy

Implement `NamingStrategy` interface:

```java
public class PrefixNamingStrategy implements NamingStrategy {
    private String tablePrefix;
    
    public PrefixNamingStrategy(String prefix) {
        this.tablePrefix = prefix;
    }
    
    @Override
    public String classToTableName(String className) {
        return tablePrefix + className.toLowerCase();
    }
    
    @Override
    public String fieldToColumnName(String fieldName) {
        return fieldName; // Keep original
    }
    
    @Override
    public String joinColumnName(String entityName) {
        return entityName.toLowerCase() + "_id";
    }
    
    @Override
    public String joinTableName(String ownerEntity, String targetEntity) {
        return tablePrefix + ownerEntity.toLowerCase() + "_" + targetEntity.toLowerCase();
    }
}

// Usage
config.setNamingStrategy(new PrefixNamingStrategy("tbl_"));
```

### Rules & Constraints

1. **Must be set before `buildSessionFactory()`**
   ```java
   config.setNamingStrategy(new SnakeCaseNamingStrategy());
   SessionFactory factory = config.buildSessionFactory();
   // config.setNamingStrategy(...); // ❌ Throws IllegalStateException
   ```

2. **Cannot be null**
   ```java
   config.setNamingStrategy(null); // ❌ Throws IllegalArgumentException
   ```

3. **Applied consistently** - All tables and columns use the same strategy

## 2. Connection Pool Customization

### Purpose
Fine-tune connection pool behavior for different deployment scenarios.

### Built-in Configurations

#### Default Configuration
```java
ConnectionPoolConfig.defaultConfig()
// minimumIdle: 10
// maximumPoolSize: 20
// connectionTimeout: 30s
// maxLifetime: 30min
// idleTimeout: 10min
```

#### High-Load Configuration
```java
ConnectionPoolConfig.highLoadConfig()
// minimumIdle: 20
// maximumPoolSize: 50
// connectionTimeout: 10s
```

#### Low-Resource Configuration
```java
ConnectionPoolConfig.lowResourceConfig()
// minimumIdle: 2
// maximumPoolSize: 5
// connectionTimeout: 60s
```

### Custom Configuration

```java
ConnectionPoolConfig poolConfig = new ConnectionPoolConfig();

// Minimum idle connections (1-100)
poolConfig.setMinimumIdle(5);

// Maximum pool size (must be >= minimumIdle, max: 100)
poolConfig.setMaximumPoolSize(20);

// Connection timeout in milliseconds (min: 1000ms)
poolConfig.setConnectionTimeout(15000); // 15 seconds

// Maximum connection lifetime (min: 30000ms or 0 for infinite)
poolConfig.setMaxLifetime(1800000); // 30 minutes

// Idle timeout (min: 10000ms or 0 to disable)
poolConfig.setIdleTimeout(600000); // 10 minutes

// Enable JMX metrics
poolConfig.setMetricsEnabled(true);

// Apply configuration
config.setConnectionPoolConfig(poolConfig);
```

### Rules & Constraints

1. **Must be set before `buildSessionFactory()`**
2. **Value constraints enforced:**
   - `minimumIdle`: 1-100
   - `maximumPoolSize`: >= minimumIdle, ≤ 100 (framework limit)
   - `connectionTimeout`: >= 1000ms
   - `maxLifetime`: >= 30000ms or 0
   - `idleTimeout`: >= 10000ms or 0

3. **Invalid values throw `IllegalArgumentException`**
   ```java
   poolConfig.setMaximumPoolSize(150); // ❌ Exceeds framework limit of 100
   poolConfig.setConnectionTimeout(500); // ❌ Below 1000ms minimum
   ```

## 3. Entity Interceptor Customization

### Purpose
Hook into entity lifecycle events for cross-cutting concerns:
- Auditing (createdAt, updatedAt fields)
- Validation before save/update
- Logging entity changes
- Business logic triggers

### Interceptor Interface

```java
public interface EntityInterceptor {
    // Called before INSERT - return false to cancel
    boolean onPreSave(Object entity);
    
    // Called after INSERT
    void onPostSave(Object entity);
    
    // Called before UPDATE - return false to cancel
    boolean onPreUpdate(Object entity);
    
    // Called after UPDATE
    void onPostUpdate(Object entity);
    
    // Called before DELETE - return false to cancel
    boolean onPreDelete(Object entity);
    
    // Called after DELETE
    void onPostDelete(Object entity);
    
    // Called after SELECT
    void onPostLoad(Object entity);
}
```

### Example: Audit Interceptor

```java
public class AuditInterceptor implements EntityInterceptor {
    
    @Override
    public boolean onPreSave(Object entity) {
        if (entity instanceof Auditable) {
            Auditable auditable = (Auditable) entity;
            auditable.setCreatedAt(LocalDateTime.now());
            auditable.setUpdatedAt(LocalDateTime.now());
            auditable.setCreatedBy(getCurrentUser());
        }
        return true; // Allow save to proceed
    }
    
    @Override
    public boolean onPreUpdate(Object entity) {
        if (entity instanceof Auditable) {
            Auditable auditable = (Auditable) entity;
            auditable.setUpdatedAt(LocalDateTime.now());
            auditable.setUpdatedBy(getCurrentUser());
        }
        return true;
    }
    
    private String getCurrentUser() {
        // Get from security context
        return "system";
    }
}

// Register interceptor
config.registerInterceptor(new AuditInterceptor());
```

### Example: Validation Interceptor

```java
public class ValidationInterceptor implements EntityInterceptor {
    
    @Override
    public boolean onPreSave(Object entity) {
        return validate(entity);
    }
    
    @Override
    public boolean onPreUpdate(Object entity) {
        return validate(entity);
    }
    
    private boolean validate(Object entity) {
        if (entity instanceof Employee) {
            Employee emp = (Employee) entity;
            if (emp.getName() == null || emp.getName().trim().isEmpty()) {
                System.err.println("Validation failed: Name is required");
                return false; // Cancel operation
            }
            if (emp.getSalary() != null && emp.getSalary() < 0) {
                System.err.println("Validation failed: Salary cannot be negative");
                return false;
            }
        }
        return true;
    }
}
```

### Rules & Constraints

1. **Multiple interceptors can be registered**
   ```java
   config.registerInterceptor(new AuditInterceptor());
   config.registerInterceptor(new ValidationInterceptor());
   config.registerInterceptor(new LoggingInterceptor());
   ```

2. **Invoked in registration order**
   - If any `onPreXxx()` returns false, operation is cancelled
   - All `onPostXxx()` methods are called (no short-circuit)

3. **Can be registered at any time** (before or after SessionFactory creation)

4. **Thread-safe** - Registry uses synchronization

5. **Performance impact** - Keep interceptor logic lightweight

## 4. Type Converter Customization

### Purpose
Handle custom data types not supported by default ResultSetMapper.

### Default Supported Types
- String, Integer, Long, Double, Float, Boolean
- BigDecimal, byte[]
- Date, LocalDate, LocalDateTime, Timestamp

### Custom Converter Example: JSON

```java
public class JsonTypeConverter implements TypeConverter<JsonNode> {
    
    private ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public Class<JsonNode> getJavaType() {
        return JsonNode.class;
    }
    
    @Override
    public void setParameter(PreparedStatement ps, int index, JsonNode value) 
            throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.VARCHAR);
        } else {
            ps.setString(index, value.toString());
        }
    }
    
    @Override
    public JsonNode getResult(ResultSet rs, String columnName) 
            throws SQLException {
        String json = rs.getString(columnName);
        if (json == null) {
            return null;
        }
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            throw new SQLException("Failed to parse JSON", e);
        }
    }
    
    @Override
    public String getSqlType(String dialect) {
        switch (dialect.toLowerCase()) {
            case "mysql":
                return "JSON";
            case "postgresql":
                return "JSONB";
            default:
                return "TEXT";
        }
    }
}

// Register converter
config.registerTypeConverter(new JsonTypeConverter());
```

### Example: Encrypted String

```java
public class EncryptedStringConverter implements TypeConverter<String> {
    
    private Cipher cipher;
    
    @Override
    public Class<String> getJavaType() {
        return String.class;
    }
    
    @Override
    public void setParameter(PreparedStatement ps, int index, String value) 
            throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.VARCHAR);
        } else {
            String encrypted = encrypt(value);
            ps.setString(index, encrypted);
        }
    }
    
    @Override
    public String getResult(ResultSet rs, String columnName) 
            throws SQLException {
        String encrypted = rs.getString(columnName);
        if (encrypted == null) {
            return null;
        }
        return decrypt(encrypted);
    }
    
    @Override
    public String getSqlType(String dialect) {
        return "VARCHAR(500)";
    }
    
    private String encrypt(String plain) { /* encryption logic */ }
    private String decrypt(String encrypted) { /* decryption logic */ }
}
```

### Rules & Constraints

1. **One converter per Java type**
   ```java
   config.registerTypeConverter(new JsonTypeConverter());
   // config.registerTypeConverter(new AnotherJsonConverter()); 
   // ❌ Throws IllegalArgumentException
   ```

2. **Cannot be null**
   ```java
   config.registerTypeConverter(null); // ❌ Throws IllegalArgumentException
   ```

3. **Can be registered at any time**

4. **Used by ResultSetMapper and SQLGenerator**

## Complete Example

```java
public class FrameworkSetup {
    
    public static SessionFactory createCustomizedFactory() {
        Configuration config = Configuration.getInstance();
        
        // 1. Set naming strategy
        config.setNamingStrategy(new SnakeCaseNamingStrategy());
        
        // 2. Configure connection pool
        ConnectionPoolConfig poolConfig = new ConnectionPoolConfig();
        poolConfig.setMinimumIdle(10);
        poolConfig.setMaximumPoolSize(30);
        poolConfig.setConnectionTimeout(20000);
        poolConfig.setMetricsEnabled(true);
        config.setConnectionPoolConfig(poolConfig);
        
        // 3. Register interceptors
        config.registerInterceptor(new AuditInterceptor());
        config.registerInterceptor(new ValidationInterceptor());
        
        // 4. Register type converters (if needed)
        // config.registerTypeConverter(new JsonTypeConverter());
        
        // 5. Build SessionFactory (locks naming/pool config)
        SessionFactory factory = config.buildSessionFactory();
        
        return factory;
    }
}
```

## Validation & Error Handling

### Framework ensures correctness through:

1. **Compile-time type safety** - Interfaces enforce correct signatures
2. **Runtime validation** - Invalid configurations throw exceptions immediately
3. **State management** - Configuration locked after SessionFactory creation
4. **Thread safety** - Registries use proper synchronization
5. **Clear error messages** - Exceptions explain what went wrong

### Common Errors

```java
// ❌ Modifying config after factory creation
SessionFactory factory = config.buildSessionFactory();
config.setNamingStrategy(new SnakeCaseNamingStrategy()); 
// Throws: IllegalStateException: Cannot modify configuration after SessionFactory is built

// ❌ Invalid pool size
poolConfig.setMaximumPoolSize(150);
// Throws: IllegalArgumentException: maximumPoolSize cannot exceed 100 (framework limit)

// ❌ Duplicate type converter
config.registerTypeConverter(new JsonTypeConverter());
config.registerTypeConverter(new AnotherJsonConverter()); 
// Throws: IllegalArgumentException: TypeConverter already registered for type: JsonNode

// ❌ Null values
config.setNamingStrategy(null);
// Throws: IllegalArgumentException: NamingStrategy cannot be null
```

## Best Practices

1. **Configure once at startup** - Set all customizations before creating SessionFactory
2. **Use built-in configurations** - Prefer `ConnectionPoolConfig.highLoadConfig()` over manual tuning
3. **Keep interceptors lightweight** - Avoid heavy computation or blocking I/O
4. **Test custom converters thoroughly** - Ensure bidirectional conversion is lossless
5. **Document custom strategies** - Explain naming conventions to your team
6. **Monitor pool metrics** - Enable `metricsEnabled` in production
7. **Validate early** - Use validation interceptors to catch errors before database operations

## Framework Guarantees

✅ **Customizations are applied consistently** across all operations  
✅ **Thread-safe** - All registries and configurations are thread-safe  
✅ **No breaking changes** - Core behavior remains stable regardless of customizations  
✅ **Performance** - Minimal overhead from customization layer  
✅ **Type safety** - Generic types preserved throughout  
✅ **Fail-fast** - Invalid configurations detected at registration time  

## Support & Troubleshooting

For issues or questions about customization:
1. Check this documentation
2. Review example code in `CustomizationExample.java`
3. Examine exception messages (they explain the problem)
4. Ensure configuration order is correct (before `buildSessionFactory()`)

---

**Framework Version:** 1.0  
**Last Updated:** January 2026  
**Authors:** DAM Framework Team
