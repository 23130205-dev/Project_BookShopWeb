# 📚 BookShopWeb
**BookShopWeb** là ứng dụng **bán sách trực tuyến** được phát triển bằng **Java Web**, áp dụng mô hình **MVC (Model – View – Controller)**.  
Hệ thống hỗ trợ quản lý sản phẩm, danh mục, người dùng và đơn hàng, chạy ổn định trên **Apache Tomcat** và sử dụng **MySQL** làm cơ sở dữ liệu.
## 🚀 Tính năng chính
### 👤 Người dùng
- Xem danh sách sách
- Tìm kiếm sách theo tên
- Đăng ký / Đăng nhập
- Đặt hàng
- Xem lịch sử đơn hàng
### 🛠️ Quản trị viên
- Quản lý sản phẩm (Thêm / Sửa / Xóa)
- Quản lý danh mục
- Quản lý người dùng
- Quản lý đơn hàng
## 🧱 Kiến trúc hệ thống
Ứng dụng được xây dựng theo mô hình **MVC (Model – View – Controller)**:
- **Model**: Xử lý dữ liệu và làm việc với cơ sở dữ liệu (DAO, JDBC)
- **View**: Giao diện người dùng (JSP, HTML, CSS)
- **Controller**: Xử lý request/response (Servlet)
- <img width="451" height="276" alt="MVC Architecture" src="https://github.com/user-attachments/assets/706467fb-48b8-4ac0-8161-614717f8c4db" />
## 🛠️ Công nghệ sử dụng
**Backend**
- Java Servlet
- JSP
- JDBC
- MVC Pattern
**Frontend**
- HTML, CSS, JSP
**Database**
- MySQL
**Công cụ**
- JDK 17
- Apache Tomcat 9
- Eclipse IDE
- Git & GitHub
## ⚙️ Cài đặt & chạy dự án
### Yêu cầu hệ thống
- JDK 17
- Apache Tomcat 9
- MySQL Server
- Eclipse IDE (hoặc IDE hỗ trợ Java Web)
### Các bước chạy
1. Tải project BookShop.zip từ GitHub
2. Import project vào Eclipse dưới dạng **Dynamic Web Project**
3. Cấu hình **Apache Tomcat** trong Eclipse
4. Tạo database và import file `.sql`
5. Cấu hình kết nối database trong file `ConstantUtils.java`
6. Run project trên Tomcat
7. Truy cập ứng dụng tại: 
## 🗄️ Cơ sở dữ liệu
Ứng dụng sử dụng **MySQL**, kết nối thông qua **JDBC**.  
Thông tin cấu hình database được khai báo trong: src/utils/ConstantUtils.java
  ✅ Kết quả đạt được
- Hoàn thiện website bán sách trực tuyến
- Áp dụng đúng mô hình MVC
- Thực hiện đầy đủ các chức năng CRUD
- Quản lý sản phẩm, danh mục, người dùng và đơn hàng
- Ứng dụng chạy ổn định trên Apache Tomcat
🔮 Hướng phát triển
- Tích hợp thanh toán online (VNPay, MoMo, ZaloPay)
- Cải thiện giao diện Responsive
- Tăng cường bảo mật và phân quyền người dùng


