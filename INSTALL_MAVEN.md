# CÀI ĐẶT MAVEN CHO WINDOWS

## Cách 1: Sử dụng Chocolatey (Recommended)

### Bước 1: Cài đặt Chocolatey (nếu chưa có)

Mở PowerShell với quyền Administrator và chạy:

```powershell
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

### Bước 2: Cài đặt Maven

```powershell
choco install maven -y
```

### Bước 3: Kiểm tra

```powershell
mvn -version
```

---

## Cách 2: Cài đặt thủ công

### Bước 1: Download Maven

1. Truy cập: https://maven.apache.org/download.cgi
2. Download file `apache-maven-3.9.6-bin.zip`

### Bước 2: Giải nén

Giải nén vào `C:\Program Files\Apache\maven`

### Bước 3: Cấu hình biến môi trường

1. Mở System Properties (Win + Pause/Break)
2. Click "Advanced system settings"
3. Click "Environment Variables"
4. Thêm biến mới:
   - Variable name: `MAVEN_HOME`
   - Variable value: `C:\Program Files\Apache\maven`
5. Edit biến `Path`, thêm: `%MAVEN_HOME%\bin`

### Bước 4: Kiểm tra

Mở Command Prompt mới và chạy:

```cmd
mvn -version
```

---

## Cách 3: Sử dụng IDE (Easiest)

### IntelliJ IDEA

1. Open Project → chọn folder `dam-framework`
2. IDEA sẽ tự động detect `pom.xml`
3. Click "Load Maven Project"
4. Để IDEA tự động download Maven

### Eclipse

1. File → Import → Maven → Existing Maven Projects
2. Chọn folder `dam-framework`
3. Eclipse sẽ tự động download Maven

### VS Code

1. Cài extension: "Extension Pack for Java" từ Microsoft
2. Cài extension: "Maven for Java"
3. Open folder `dam-framework`
4. Extension sẽ tự động detect Maven project

---

## Sau khi cài đặt Maven

### Build project:

```bash
cd e:\dam-framework
mvn clean install
```

### Run tests:

```bash
mvn test
```

### Compile only:

```bash
mvn compile
```

---

## Troubleshooting

### Lỗi: "mvn is not recognized"

- **Solution**: Restart terminal/PowerShell sau khi cài Maven
- Hoặc logout/login lại Windows

### Lỗi: "JAVA_HOME is not set"

Set JAVA_HOME environment variable:

```powershell
# Check Java location
where java

# Set JAVA_HOME (replace with your Java path)
setx JAVA_HOME "C:\Program Files\Java\jdk-11"
```

### Maven download quá chậm

Thêm mirror vào `C:\Users\<YourUsername>\.m2\settings.xml`:

```xml
<settings>
  <mirrors>
    <mirror>
      <id>aliyun</id>
      <mirrorOf>central</mirrorOf>
      <name>Aliyun Maven</name>
      <url>https://maven.aliyun.com/repository/public</url>
    </mirror>
  </mirrors>
</settings>
```

---

## Alternative: Sử dụng Maven Wrapper (đã có trong project)

Nếu không muốn cài Maven global, có thể dùng Maven Wrapper:

```bash
# Windows
mvnw.cmd clean install

# Linux/Mac
./mvnw clean install
```

Tuy nhiên hiện tại project chưa có Maven Wrapper, cần generate:

```bash
mvn wrapper:wrapper
```
