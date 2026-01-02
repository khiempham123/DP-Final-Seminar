# HƯỚNG DẪN PHÁT TRIỂN - DAM FRAMEWORK

## 📋 Mục Lục

1. [Workflow Git](#workflow-git)
2. [Coding Standards](#coding-standards)
3. [Checklist cho Dev 1](#checklist-cho-dev-1)
4. [Checklist cho Dev 2](#checklist-cho-dev-2)
5. [Integration Points](#integration-points)
6. [Testing Strategy](#testing-strategy)

---

## 🌿 Workflow Git

### Branch Strategy

```
main (production-ready code)
├── dev (development branch)
    ├── dev1/feature-name (Dev 1's features)
    └── dev2/feature-name (Dev 2's features)
```

### Quy Trình Làm Việc

#### 1. Clone Repository
```bash
git clone <repository-url>
cd dam-framework
```

#### 2. Tạo Branch Cho Từng Feature
```bash
# Dev 1 làm Session
git checkout -b dev1/session-implementation

# Dev 2 làm Annotations
git checkout -b dev2/annotations
```

#### 3. Commit Changes
```bash
git add .
git commit -m "[Dev1] Implement SessionFactory interface"
# or
git commit -m "[Dev2] Add @Entity annotation"
```

#### 4. Push và Tạo Pull Request
```bash
git push origin dev1/session-implementation
# Tạo Pull Request trên GitHub
```

#### 5. Code Review và Merge
- Đồng đội review code
- Fix nếu có yêu cầu
- Merge vào branch `dev`

### Commit Message Format

```
[Dev1/Dev2] <type>: <description>

Types:
- feat: New feature
- fix: Bug fix
- refactor: Code refactoring
- docs: Documentation
- test: Testing

Examples:
[Dev1] feat: Implement MySQL dialect
[Dev2] feat: Add QueryBuilder WHERE clause
[Dev1] fix: Connection pool memory leak
[Dev2] docs: Add JavaDoc for annotations
```

---

## 💻 Coding Standards

### Java Conventions

1. **Package Naming**: All lowercase
   ```java
   package com.dam.framework.core;
   ```

2. **Class Naming**: PascalCase
   ```java
   public class SessionFactoryImpl { }
   ```

3. **Method Naming**: camelCase
   ```java
   public Session openSession() { }
   ```

4. **Constants**: UPPER_SNAKE_CASE
   ```java
   public static final int MAX_CONNECTIONS = 10;
   ```

### JavaDoc Requirements

Mỗi class và public method phải có JavaDoc:

```java
/**
 * Factory for creating Session instances.
 * 
 * Design Pattern: Factory Pattern (GoF Pattern #1)
 * 
 * @author Dev 1
 * @version 1.0
 */
public interface SessionFactory {
    
    /**
     * Open a new Session.
     * 
     * @return A new Session instance
     * @throws RuntimeException if unable to create session
     */
    Session openSession();
}
```

### Error Handling

```java
// DO: Specific exception types
public void save(Object entity) {
    if (entity == null) {
        throw new IllegalArgumentException("Entity cannot be null");
    }
    // ...
}

// DON'T: Generic exceptions
public void save(Object entity) throws Exception { }
```

---

## ✅ Checklist cho Dev 1

### Week 1-2: Foundation & Core

- [ ] **Connection Management**
  - [ ] Implement `ConnectionPool` class
  - [ ] Integrate HikariCP
  - [ ] Test connection acquisition and release
  - [ ] Add connection timeout handling

- [ ] **Session Implementation**
  - [ ] Create `SessionImpl` class implementing `Session` interface
  - [ ] Implement `save()` method
  - [ ] Implement `update()` method
  - [ ] Implement `delete()` method
  - [ ] Implement `find()` method
  - [ ] Test with real MySQL database

- [ ] **SessionFactory Implementation**
  - [ ] Create `SessionFactoryImpl` class
  - [ ] Implement `openSession()` method
  - [ ] Implement `getCurrentSession()` with ThreadLocal
  - [ ] Add proper resource cleanup

### Week 3-4: Transaction & MySQL Dialect

- [ ] **Transaction Management**
  - [ ] Create `TransactionImpl` class
  - [ ] Implement `begin()`, `commit()`, `rollback()`
  - [ ] Handle auto-commit mode
  - [ ] Test rollback on exception

- [ ] **MySQL Dialect**
  - [ ] Complete `getInsertSQL()` implementation
  - [ ] Complete `getUpdateSQL()` implementation
  - [ ] Complete `getDeleteSQL()` implementation
  - [ ] Complete `getSelectSQL()` implementation
  - [ ] Test all SQL generation

### Week 5: Multi-Database Support

- [ ] **PostgreSQL Dialect**
  - [ ] Implement all SQL methods
  - [ ] Handle RETURNING clause for INSERT
  - [ ] Test with PostgreSQL database

- [ ] **SQL Server Dialect**
  - [ ] Implement all SQL methods
  - [ ] Handle IDENTITY for auto-increment
  - [ ] Handle pagination with OFFSET/FETCH
  - [ ] Test with SQL Server database

### Week 6: Demo & Integration

- [ ] **Demo Application**
  - [ ] Create sample entities (User, Product, Order)
  - [ ] Demonstrate all CRUD operations
  - [ ] Show query builder usage
  - [ ] Add console menu interface

---

## ✅ Checklist cho Dev 2

### Week 1-2: Annotations & Metadata

- [ ] **Annotations** (Already completed ✓)
  - [x] Create `@Entity` annotation
  - [x] Create `@Table` annotation
  - [x] Create `@Column` annotation
  - [x] Create `@Id` annotation
  - [x] Create `@GeneratedValue` annotation

- [ ] **Configuration** (Partially completed)
  - [x] Implement Singleton pattern
  - [ ] Add more configuration options
  - [ ] Integrate with SessionFactory creation

- [ ] **Metadata Parser**
  - [ ] Complete `MetadataParser.parse()` method
  - [ ] Handle all annotation types
  - [ ] Add validation (ensure @Id exists)
  - [ ] Test with sample entities

### Week 3-4: SQL Generation & Query Builder

- [ ] **SQL Generator**
  - [ ] Complete `generateInsertSQL()`
  - [ ] Complete `generateUpdateSQL()`
  - [ ] Complete `generateDeleteSQL()`
  - [ ] Complete `generateSelectSQL()`
  - [ ] Complete `generateSelectByIdSQL()`
  - [ ] Add unit tests

- [ ] **Query Builder - Part 1**
  - [ ] Implement `where()` method
  - [ ] Implement `orWhere()` method
  - [ ] Support operators: =, !=, >, <, >=, <=, LIKE
  - [ ] Implement parameter binding
  - [ ] Test WHERE clause generation

### Week 5: Advanced Query Builder

- [ ] **Query Builder - Part 2**
  - [ ] Implement `groupBy()` method
  - [ ] Implement `having()` method
  - [ ] Implement `orderBy()` method
  - [ ] Implement `limit()` and `offset()` methods
  - [ ] Complete `buildSQL()` method
  - [ ] Test complex queries

- [ ] **ResultSet Mapper**
  - [ ] Create `ResultSetMapper` class
  - [ ] Implement generic mapping logic
  - [ ] Handle type conversion (SQL → Java)
  - [ ] Test with various entity types

### Week 6: Documentation

- [ ] **Class Diagrams**
  - [ ] Draw complete framework class diagram
  - [ ] Draw diagram for each design pattern
  - [ ] Add relationship descriptions

- [ ] **Documentation**
  - [ ] Write API reference
  - [ ] Write user guide with examples
  - [ ] Document all 4 design patterns
  - [ ] Create feature completion table

---

## 🔗 Integration Points

### Point 1: EntityMetadata (Week 1 End)

**Dev 2** creates `EntityMetadata` structure:
```java
public class EntityMetadata {
    private String tableName;
    private Field idField;
    private List<Field> fields;
    // ...
}
```

**Dev 1** uses it in Dialect:
```java
public String getInsertSQL(EntityMetadata metadata) {
    String tableName = metadata.getTableName();
    // ...
}
```

**Sync Point**: End of Week 1 - Dev 2 must complete EntityMetadata

---

### Point 2: SQL Generation (Week 2 End)

**Dev 2** provides `SQLGenerator`:
```java
String insertSQL = SQLGenerator.generateInsertSQL(metadata);
```

**Dev 1** uses it in `SessionImpl`:
```java
public <T> T save(T entity) {
    EntityMetadata metadata = MetadataParser.parse(entity.getClass());
    String sql = SQLGenerator.generateInsertSQL(metadata);
    // Execute via JDBC
}
```

**Sync Point**: End of Week 2 - Both devs test CRUD together

---

### Point 3: Query Builder (Week 4 End)

**Dev 2** provides `QueryBuilder`:
```java
QueryBuilder<User> builder = new QueryBuilder<>(User.class);
builder.where("age", ">", 18);
String sql = builder.buildSQL();
List<Object> params = builder.getParameters();
```

**Dev 1** executes query in `SessionImpl`:
```java
public <T> QueryBuilder<T> createQuery(Class<T> entityClass) {
    return new QueryBuilder<>(entityClass, this);
}
```

**Sync Point**: End of Week 4 - Test complex queries together

---

## 🧪 Testing Strategy

### Unit Tests

Mỗi developer test riêng component của mình:

**Dev 1 Tests:**
```java
@Test
public void testConnectionPool() {
    // Test connection acquisition/release
}

@Test
public void testSessionSave() {
    // Test save operation
}
```

**Dev 2 Tests:**
```java
@Test
public void testMetadataParser() {
    // Test annotation parsing
}

@Test
public void testSQLGenerator() {
    // Test SQL generation
}
```

### Integration Tests

Sau mỗi integration point, test cùng nhau:

```java
@Test
public void testFullCRUDFlow() {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    
    User user = new User("john", "john@example.com");
    session.save(user); // Dev 1 + Dev 2
    
    User found = session.find(User.class, user.getId());
    assertEquals("john", found.getUsername());
    
    tx.commit();
    session.close();
}
```

### Test Database Setup

```sql
-- Create test database
CREATE DATABASE dam_test;

-- Create test table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100),
    email VARCHAR(100),
    age INT
);
```

---

## 📞 Communication

### Daily Standup (Virtual)

Mỗi ngày update trên group chat:
- Hôm qua làm gì?
- Hôm nay làm gì?
- Có vấn đề gì cần hỗ trợ?

### Code Review Checklist

Khi review code của nhau:
- [ ] Code có compile không?
- [ ] Có test cases không?
- [ ] JavaDoc đầy đủ không?
- [ ] Tuân thủ coding standards không?
- [ ] Logic có hợp lý không?

---

## 🚨 Common Issues & Solutions

### Issue 1: Conflict khi merge

**Solution:**
```bash
git fetch origin
git rebase origin/dev
# Resolve conflicts
git add .
git rebase --continue
```

### Issue 2: Dependency giữa Dev 1 và Dev 2

**Solution:**
- Dev 2 tạo interface/stub class trước
- Dev 1 implement logic sau
- Sync ở integration points

### Issue 3: Test database khác nhau

**Solution:**
- Sử dụng `application.properties` riêng
- Thêm vào `.gitignore`: `application.properties.local`

---

**Good luck! 🚀**
