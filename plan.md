Dưới đây là bảng phân chia công việc chi tiết cho 2 thành viên (Dev 1 và Dev 2) để xây dựng DAM Framework. Bảng này tập trung sâu vào kỹ thuật, các lớp (classes) cần xây dựng và các mẫu thiết kế (Design Patterns) cần áp dụng.

---

# PHÂN CHIA CÔNG VIỆC CHI TIẾT - DAM FRAMEWORK

## 1. DEV 1: CORE ENGINE & INFRASTRUCTURE (Cốt lõi & Hạ tầng)

**Trách nhiệm chính:** Quản lý kết nối Database, thực thi câu lệnh JDBC, quản lý Transaction và các Dialect (phương ngữ) của từng loại Database.

### A. Quản lý Kết nối & Session (Connection & Session Management)

* **Xây dựng Interfaces cốt lõi:**
* Tạo interface `Session` với các phương thức: `save()`, `update()`, `delete()`, `find()`, `createQuery()`, `beginTransaction()`, `close()`.
* Tạo interface `SessionFactory` với phương thức `openSession()`.
* Tạo interface `Transaction` với các phương thức `commit()`, `rollback()`.


* **Hiện thực Factory Pattern (Design Pattern #1):**
* Xây dựng class `SessionFactoryImpl` implements `SessionFactory`.
* Nhiệm vụ: Khởi tạo các `Session` mới, mỗi session giữ một kết nối JDBC riêng biệt.


* **Xây dựng Connection Pool:**
* Tạo class `ConnectionPool` hoặc tích hợp HikariCP.
* Chức năng: Quản lý danh sách các kết nối, tái sử dụng kết nối (reuse) thay vì mở mới liên tục để tối ưu hiệu năng.



### B. Xây dựng Chiến lược Đa Cơ Sở Dữ Liệu (Multi-DB Strategy)

* **Hiện thực Strategy Pattern (Design Pattern #2):**
* Tạo interface `Dialect` định nghĩa các hành vi khác nhau giữa các DB (ví dụ: cú pháp phân trang, kiểu dữ liệu).


* **Hiện thực các Dialect cụ thể:**
* `MySQLDialect`: Viết logic chuyển đổi kiểu dữ liệu Java sang MySQL (VD: `String` -> `VARCHAR`), cú pháp `LIMIT/OFFSET`.
* `PostgreSQLDialect`: Xử lý các khác biệt của Postgre (VD: `SERIAL` cho ID tự tăng).
* `SQLServerDialect`: Xử lý cú pháp phân trang đặc thù của MS SQL (`OFFSET FETCH NEXT`).



### C. Thực thi JDBC & Transaction (Execution Engine)

* **Xử lý JDBC PreparedStatement:**
* Trong class `SessionImpl`, viết logic nhận chuỗi SQL (từ Dev 2) và thực thi thông qua `PreparedStatement`.
* Đảm bảo map đúng các tham số (parameters) vào dấu `?` trong câu SQL để chống SQL Injection.


* **Quản lý Transaction:**
* Viết logic `setAutoCommit(false)` khi bắt đầu transaction.
* Viết logic `commit()` để lưu dữ liệu vĩnh viễn.
* Viết logic `rollback()` để hoàn tác nếu có Exception xảy ra trong quá trình thực thi.



### D. Demo & Testing Support

* **Xây dựng Demo Application:**
* Tạo một ứng dụng Java Console hoặc Swing đơn giản.
* Tạo các Entity mẫu (VD: `Student`, `Class`).
* Viết code demo các chức năng: Thêm, sửa, xóa, lọc dữ liệu để quay video.



---

## 2. DEV 2: LOGIC LAYER, MAPPING & API (Logic, Ánh xạ & Giao diện)

**Trách nhiệm chính:** Phân tích Metadata (Annotation), sinh mã SQL tự động (SQL Generation), Mapping dữ liệu từ DB lên Object, và xây dựng Query Builder.

### A. Cấu hình & Metadata (Configuration & Reflection)

* **Hiện thực Singleton Pattern (Design Pattern #3):**
* Xây dựng class `Configuration` đảm bảo chỉ có một instance duy nhất tồn tại trong vòng đời ứng dụng.
* Chức năng: Đọc file `application.properties` (URL, Username, Password, Driver class).


* **Định nghĩa Annotations:**
* Tạo các @interface: `@Entity` (đánh dấu class), `@Table` (tên bảng), `@Column` (tên cột), `@Id` (khóa chính), `@GeneratedValue` (tự tăng).


* **Xử lý Reflection (Mapping Engine):**
* Viết class `AnnotationParser` hoặc `Metamodel`: Dùng Java Reflection để quét class.
* Lưu trữ thông tin vào object `EntityMetadata`: Map field nào ứng với cột nào, kiểu dữ liệu là gì.



### B. Sinh mã SQL & Mapping (SQL Generation & Result Mapper)

* **Xây dựng SQL Generator:**
* Viết các hàm sinh chuỗi SQL động dựa trên `EntityMetadata`:
* `generateInsertSQL(Object entity)` -> trả về chuỗi `INSERT INTO...`
* `generateUpdateSQL(Object entity)` -> trả về chuỗi `UPDATE...`
* `generateDeleteSQL(Class clazz, Object id)` -> trả về chuỗi `DELETE...`
* `generateSelectSQL(Class clazz)` -> trả về chuỗi `SELECT...`.




* **Xây dựng ResultSet Mapper:**
* Viết generic method để hứng `java.sql.ResultSet` từ JDBC (do Dev 1 trả về).
* Dùng Reflection để khởi tạo Object Java và gán giá trị từ ResultSet vào các field tương ứng của Object (Hydration).



### C. Xây dựng Query Builder (Advanced Querying)

* **Hiện thực Builder Pattern (Design Pattern #4):**
* Tạo class `QueryBuilder` cho phép gọi chuỗi hàm liên tiếp (Fluent Interface).
* Cấu trúc: `.select()`, `.from()`, `.where()`, `.orderBy()`, `.limit()`.


* **Xử lý Logic lọc dữ liệu (Filter Logic):**
* `where(String field, String operator, Object value)`: Xây dựng mệnh đề WHERE động.
* Hỗ trợ các toán tử: `=`, `!=`, `>`, `<`, `LIKE`.
* Lưu trữ các tham số vào một `List<Object>` để chuyển sang cho Dev 1 binding vào PreparedStatement.


* **Xử lý Logic nâng cao (Aggregation):**
* `groupBy(String... columns)`: Thêm mệnh đề GROUP BY.
* `having(String condition)`: Thêm mệnh đề HAVING.
* Hỗ trợ các hàm tổng hợp trong `select()`: `COUNT`, `SUM`, `AVG`, `MAX`, `MIN`.



### D. Tài liệu & Báo cáo (Documentation)

* **Viết tài liệu kỹ thuật:**
* Vẽ sơ đồ UML Class Diagram chi tiết cho toàn bộ Framework.
* Viết giải thích chi tiết về 4 Design Patterns đã áp dụng (kèm ảnh chụp code minh họa).
* Viết hướng dẫn sử dụng (User Guide) cho các lập trình viên khác dùng Framework.