# DAM Framework - Database Access Management Framework

![Java](https://img.shields.io/badge/Java-11-blue)
![Status](https://img.shields.io/badge/Status-In%20Development-yellow)

## 📋 Giới Thiệu

DAM Framework là một ORM (Object-Relational Mapping) Framework được xây dựng bằng Java, tương tự như Hibernate nhưng nhẹ hơn. Framework này giúp ánh xạ dữ liệu quan hệ (Relational Database) thành các đối tượng Java (Object-Oriented).

## 🎯 Mục Tiêu Dự Án

- ✅ Hỗ trợ các thao tác CRUD cơ bản (Create, Read, Update, Delete)
- ✅ Query Builder với WHERE, GROUP BY, HAVING, ORDER BY
- ✅ Hỗ trợ đa cơ sở dữ liệu (MySQL, PostgreSQL, SQL Server)
- ✅ Áp dụng ít nhất 4 Design Patterns từ GoF
- ✅ Quản lý Transaction và Connection Pool
- ✅ Sử dụng Annotations và Reflection

## 🏗️ Cấu Trúc Dự Án

```
dam-framework/
├── src/
│   ├── main/
│   │   ├── java/com/dam/framework/
│   │   │   ├── annotation/       # Annotations (@Entity, @Table, @Column, etc.)
│   │   │   ├── core/             # Core interfaces (Session, SessionFactory, Transaction)
│   │   │   ├── dialect/          # Database dialects (MySQL, PostgreSQL, SQL Server)
│   │   │   ├── engine/           # Metadata parser, SQL generator
│   │   │   └── query/            # Query builder
│   │   └── resources/
│   │       └── application.properties
│   └── test/                     # Unit tests
├── pom.xml
└── README.md
```

## 🎨 Design Patterns Được Sử Dụng

### 1. Factory Pattern (SessionFactory)
- **Vị trí**: `com.dam.framework.core.SessionFactory`
- **Mục đích**: Tạo Session instances mà không expose logic khởi tạo
- **Ai làm**: Dev 1

### 2. Strategy Pattern (Dialect)
- **Vị trí**: `com.dam.framework.dialect.Dialect`
- **Mục đích**: Hỗ trợ nhiều loại database với các chiến lược SQL khác nhau
- **Ai làm**: Dev 1

### 3. Singleton Pattern (Configuration)
- **Vị trí**: `com.dam.framework.core.Configuration`
- **Mục đích**: Đảm bảo chỉ có một instance duy nhất của configuration
- **Ai làm**: Dev 2

### 4. Builder Pattern (QueryBuilder)
- **Vị trí**: `com.dam.framework.query.QueryBuilder`
- **Mục đích**: Xây dựng query phức tạp theo từng bước với fluent interface
- **Ai làm**: Dev 2

## 👥 Phân Chia Công Việc

### Dev 1: Core Engine & Infrastructure
**Trách nhiệm chính:**
- ✅ Quản lý kết nối Database & Connection Pool
- ✅ Implement SessionFactory, Session, Transaction
- ✅ Xây dựng các Dialect (MySQL, PostgreSQL, SQL Server)
- ✅ Thực thi JDBC với PreparedStatement
- ✅ Tạo Demo Application

**Packages:**
- `core/` (implementation classes)
- `dialect/` (all dialects)

---

### Dev 2: Logic Layer, Mapping & API
**Trách nhiệm chính:**
- ✅ Tạo Annotations (@Entity, @Table, @Column, @Id, @GeneratedValue)
- ✅ Xây dựng MetadataParser (Reflection)
- ✅ SQLGenerator (generate INSERT, UPDATE, DELETE, SELECT)
- ✅ QueryBuilder với WHERE, GROUP BY, HAVING
- ✅ ResultSet Mapper (map từ ResultSet sang Object)
- ✅ Viết Documentation & Class Diagrams

**Packages:**
- `annotation/` (all annotations)
- `engine/` (metadata & SQL generation)
- `query/` (query builder)

## 🚀 Bắt Đầu

### 1. Cài Đặt Requirements

- Java 11 hoặc cao hơn
- Maven 3.6+
- MySQL 8.0+ (hoặc PostgreSQL/SQL Server)

### 2. Clone Repository

```bash
git clone <repository-url>
cd dam-framework
```

### 3. Cấu Hình Database

Sửa file `src/main/resources/application.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/your_database
db.username=your_username
db.password=your_password
db.dialect=com.dam.framework.dialect.MySQLDialect
db.pool.max_size=10
```

### 4. Build Project

```bash
mvn clean install
```

## 📝 Cách Sử Dụng (Dự Kiến)

### Bước 1: Tạo Entity Class

```java
import com.dam.framework.annotation.*;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "email")
    private String email;
    
    // Constructors, getters, setters...
}
```

### Bước 2: Sử Dụng Session

```java
// Initialize framework
Configuration config = Configuration.getInstance();
SessionFactory sessionFactory = config.buildSessionFactory();

// Open session
try (Session session = sessionFactory.openSession()) {
    
    // Begin transaction
    Transaction tx = session.beginTransaction();
    
    try {
        // CREATE
        User user = new User("john_doe", "john@example.com");
        session.save(user);
        
        // READ
        User foundUser = session.find(User.class, 1L);
        
        // UPDATE
        foundUser.setEmail("newemail@example.com");
        session.update(foundUser);
        
        // DELETE
        session.delete(foundUser);
        
        // Commit transaction
        tx.commit();
        
    } catch (Exception e) {
        tx.rollback();
        throw e;
    }
}
```

### Bước 3: Query Builder

```java
List<User> users = session.createQuery(User.class)
    .where("age", ">", 18)
    .where("status", "=", "ACTIVE")
    .orderBy("username ASC")
    .limit(10)
    .execute();
```

## 📊 Timeline

| Tuần | Dev 1 | Dev 2 |
|------|-------|-------|
| 1 | Setup Connection & Interfaces | Annotations & Configuration |
| 2 | Session Implementation & MySQL Dialect | Metadata Parser & SQL Generator |
| 3 | Transaction & Connection Pool | Query Builder (WHERE) |
| 4 | PostgreSQL & SQL Server Dialects | Query Builder (GROUP BY, HAVING) |
| 5 | Integration & Testing | ResultSet Mapper & Testing |
| 6 | Demo Application | Documentation & Class Diagrams |

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=SessionTest
```

## 📚 Tài Liệu Tham Khảo

- [Martin Fowler - Enterprise Patterns](http://www.martinfowler.com/eaaCatalog/)
- [Java Reflection Tutorial](https://www.baeldung.com/java-reflection)
- [JDBC Best Practices](https://www.baeldung.com/java-jdbc)
- [Design Patterns - Gang of Four](https://refactoring.guru/design-patterns)

## 📞 Liên Hệ & Hỗ Trợ

- **Dev 1**: [Tên & Email]
- **Dev 2**: [Tên & Email]

## 📄 License

Dự án này được phát triển cho mục đích học tập tại trường Đại học [Tên Trường].

---

**Note**: Đây là skeleton code để bắt đầu dự án. Các TODO comments đã được đánh dấu rõ ràng cho từng developer.
