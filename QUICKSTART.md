# QUICK START GUIDE

## Hướng dẫn Setup Nhanh cho Đồng Đội

### Bước 1: Clone Repository

```bash
git clone <repository-url>
cd dam-framework
```

### Bước 2: Setup Database

#### MySQL (Recommended cho bắt đầu)

1. Install MySQL 8.0+
2. Chạy script:
```bash
mysql -u root -p < database-setup.sql
```

3. Update `src/main/resources/application.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/dam_test
db.username=root
db.password=your_password
db.dialect=com.dam.framework.dialect.MySQLDialect
db.pool.max_size=10
```

### Bước 3: Build Project

```bash
mvn clean install
```

Nếu có lỗi compilation (do các method chưa implement), đây là điều bình thường. Bạn sẽ implement từng phần theo plan.

### Bước 4: Run Tests

```bash
mvn test
```

### Bước 5: Phân Chia Công Việc

#### **Dev 1 (Infrastructure):**
Bắt đầu từ:
1. `src/main/java/com/dam/framework/core/` - Implement Session và SessionFactory
2. `src/main/java/com/dam/framework/dialect/MySQLDialect.java` - Complete SQL generation

#### **Dev 2 (Logic Layer):**
Bắt đầu từ:
1. `src/main/java/com/dam/framework/engine/MetadataParser.java` - Complete parsing logic
2. `src/main/java/com/dam/framework/engine/SQLGenerator.java` - Complete SQL generation
3. `src/main/java/com/dam/framework/query/QueryBuilder.java` - Complete query building

### Các File Quan Trọng

- **README.md** - Tổng quan dự án
- **DEVELOPMENT_GUIDE.md** - Chi tiết workflow và checklist
- **plan.md** - Phân chia công việc gốc
- **project_breakdown.md** - Kiến thức nền tảng và patterns
- **project_plan.md** - Timeline chi tiết

### Structure Overview

```
src/main/java/com/dam/framework/
├── annotation/      ✅ COMPLETED - Sẵn sàng sử dụng
├── core/           🔨 TODO (Dev 1) - Interfaces sẵn, cần implement
├── dialect/        🔨 TODO (Dev 1) - Skeletons sẵn, cần complete
├── engine/         🔨 TODO (Dev 2) - Partially done, cần complete
└── query/          🔨 TODO (Dev 2) - Structure sẵn, cần implement

src/test/java/      🧪 Test templates sẵn sàng
```

### Kiểm Tra Setup

Chạy lệnh sau để verify:

```bash
# Check Java version (should be 11+)
java -version

# Check Maven
mvn -version

# Compile project (có thể có warnings là bình thường)
mvn compile

# Check database connection (nếu MySQL đang chạy)
mysql -u root -p -e "USE dam_test; SELECT COUNT(*) FROM users;"
```

Nếu thấy "4" từ command cuối, database đã setup thành công!

### Git Workflow

```bash
# Tạo branch riêng cho feature của bạn
git checkout -b dev1/session-implementation
# hoặc
git checkout -b dev2/metadata-parser

# Sau khi code xong
git add .
git commit -m "[Dev1] Implement SessionFactory"
git push origin dev1/session-implementation

# Tạo Pull Request trên GitHub
```

### Liên Hệ

Nếu có vấn đề:
1. Check DEVELOPMENT_GUIDE.md
2. Check Issues trên GitHub
3. Hỏi đồng đội qua group chat

---

**Good Luck! 🚀**

Remember: Đây là skeleton code. Nhiệm vụ của các bạn là implement các TODO comments!
