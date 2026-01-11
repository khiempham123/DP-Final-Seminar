# DAM Framework Demo Project - README

## Mô Tả
Đây là project demo để test DAM Framework trên một ứng dụng thực tế - **Hệ thống quản lý sản phẩm và đơn hàng**.

## Cấu Trúc Project

```
demo-project/
├── pom.xml                         # Maven config (depends on DAM Framework)
├── sql/
│   └── setup_database.sql          # Database setup script
├── src/main/
│   ├── java/com/demo/
│   │   ├── DemoApplication.java    # Main demo app với interactive menu
│   │   └── entity/
│   │       ├── Category.java       # Category entity
│   │       ├── Product.java        # Product entity
│   │       ├── Customer.java       # Customer entity
│   │       ├── Order.java          # Order entity
│   │       └── OrderItem.java      # OrderItem entity
│   └── resources/
│       └── application.properties  # Database config
└── README.md                       # This file
```

## Entity Relationships

```
Category (1) ←───────→ (N) Product
                            ↑
                            │
Customer (1) ←─→ (N) Order (1) ←─→ (N) OrderItem
```

## Cách Chạy Demo

### Bước 1: Setup Database
```sql
-- Chạy script SQL trong MySQL
source sql/setup_database.sql

-- Hoặc copy paste vào MySQL Workbench
```

### Bước 2: Cấu hình kết nối
Edit file `src/main/resources/application.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/dam_demo
db.username=root
db.password=YOUR_PASSWORD
```

### Bước 3: Install DAM Framework (nếu chưa)
```bash
cd ../   # Quay lại thư mục DP-Final-Seminar
mvn clean install
```

### Bước 4: Chạy Demo
```bash
cd demo-project
mvn compile exec:java -Dexec.mainClass="com.demo.DemoApplication"
```

## Demo Features

### 1. CRUD Operations
- **CREATE**: Thêm mới Product
- **READ**: Tìm Product theo ID
- **UPDATE**: Cập nhật Product
- **DELETE**: Xóa Product

### 2. QueryBuilder
- WHERE clause với nhiều conditions
- ORDER BY (ASC/DESC)
- LIMIT/OFFSET cho phân trang

### 3. Transaction Management
- COMMIT transaction
- ROLLBACK demo
- Transaction isolation

### 4. Entity Relationships
- OneToMany: Category → Products
- ManyToOne: Product → Category
- Lazy/Eager loading

## Test Cases

| Test | Mô tả | Expected Result |
|------|-------|-----------------|
| Create | Tạo Product mới | Product có ID được generate |
| Read | Tìm Product by ID | Trả về Product hoặc null |
| Update | Cập nhật price | Price được cập nhật |
| Delete | Xóa Product | Product không còn tồn tại |
| Query | WHERE price > 50 | Trả về list Products |
| Transaction | Commit/Rollback | Data được persist/rollback |

## Screenshots

Khi chạy demo, bạn sẽ thấy menu:

```
╔══════════════════════════════════════════════════════════╗
║       DAM FRAMEWORK - Database Access Management         ║
║              Demo Application v1.0                        ║
╚══════════════════════════════════════════════════════════╝

┌─────────────────── MENU ───────────────────┐
│  1. CREATE   - Add new product              │
│  2. READ     - Find product by ID           │
│  3. UPDATE   - Update product               │
│  4. DELETE   - Delete product               │
│  5. QUERY    - Demo QueryBuilder            │
│  6. TRANSACTION - Demo Transaction          │
│  7. FULL WORKFLOW - Complete demo           │
│  8. LIST ALL - Show all data                │
│  0. EXIT                                    │
└─────────────────────────────────────────────┘
```

## Troubleshooting

### Lỗi "DAM Framework not found"
```bash
# Đảm bảo đã install framework vào local Maven repo
cd ../
mvn clean install
```

### Lỗi "Connection refused"
- Kiểm tra MySQL đang chạy
- Kiểm tra username/password trong application.properties
- Kiểm tra database `dam_demo` đã được tạo

### Lỗi "Table not found"
```bash
# Chạy lại SQL script
mysql -u root -p < sql/setup_database.sql
```

## Author
DAM Framework Team - Design Pattern Course
