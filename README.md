# GaraMan — Java Servlet + JSP + JDBC (bản Jakarta EE 10 / Tomcat 10.1)

Cài đặt 2 module theo tài liệu phân tích thiết kế:

1. **Tìm kiếm thông tin dịch vụ/phụ tùng** (Khách hàng)
2. **Thanh toán** (Nhân viên bán hàng)

Cơ sở dữ liệu (`sql/garaman.sql`) được tạo cho **toàn bộ hệ thống** theo ERD, kèm dữ liệu mẫu.

## 1. Yêu cầu

| Thành phần | Phiên bản |
|---|---|
| JDK | 17 trở lên |
| Apache Tomcat | **10.1.x** (Servlet 6.0, dùng `jakarta.servlet.*`) — **không** chạy được trên Tomcat 9 |
| MySQL | 8.x (hoặc MariaDB 10.x) |
| Maven | 3.8+ (hoặc import vào NetBeans/Eclipse/IntelliJ như dự án Maven) |

## 2. Cài đặt

```bash
# 1) Tạo CSDL + dữ liệu mẫu
mysql -u root -p < sql/garaman.sql

# 2) Sửa user/mật khẩu MySQL trong src/main/resources/db.properties
#    (hoặc đặt biến môi trường GARAMAN_DB_URL, GARAMAN_DB_USER, GARAMAN_DB_PASSWORD)

# 3) Đóng gói
mvn clean package
# -> target/garaman.war

# 4) Copy target/garaman.war vào <TOMCAT>/webapps/ rồi chạy Tomcat
# 5) Mở http://localhost:8080/garaman/
```

> Muốn chạy trên Tomcat 9 thì dùng bản `javax` (garaman.zip gửi trước đó), vì Tomcat 9 và Tomcat 10 không dùng chung được một file .war.

### Chạy bằng NetBeans
1. File → Open Project → chọn thư mục `garaman` (biểu tượng Maven).
2. Services → Servers: đăng ký Apache Tomcat 10.1 (nếu chưa có).
3. Nhấp phải dự án → Run → chọn server Tomcat 10.1, tick *Remember Permanently*.
4. Sửa mật khẩu MySQL trong `db.properties`, rồi Clean and Build → Run.

## 3. Tài khoản mẫu (mật khẩu chung: `123456`)

| Vai trò | Tên đăng nhập |
|---|---|
| Khách hàng | `customer1` (Nguyễn Văn A, 250 điểm), `customer2`, `customer3`, `customer4` |
| Nhân viên bán hàng | `sales1` |
| Quản lý / Kho / Kỹ thuật | `manager1` / `kho1` / `kythuat1`, `kythuat2` (chưa có chức năng, chỉ đăng nhập được) |

Dữ liệu mẫu để thử thanh toán: khách **Nguyễn Văn A** có đơn `ord-129`, `ord-132` (chưa thanh toán) và `ord-123` (đã thanh toán).

## 4. Đối chiếu với thiết kế

**Lớp giao diện → JSP** (thư mục `src/main/webapp`)

| Thiết kế | File |
|---|---|
| CustomerHomeFrm | `customer/CustomerHomeFrm.jsp` |
| ChooseServiceOrPartFrm | `customer/ChooseServiceOrPartFrm.jsp` |
| ServiceOrPartFrm | `customer/ServiceOrPartFrm.jsp` |
| SalesStaffHomeFrm | `sales/SalesStaffHomeFrm.jsp` |
| SearchCustomerFrm | `sales/SearchCustomerFrm.jsp` |
| CustomerOrderListFrm | `sales/CustomerOrderListFrm.jsp` |
| OrderBillDetailFrm | `sales/OrderBillDetailFrm.jsp` |

Các thành phần giao diện (`inSearchTextField`, `inSearchButton`, `outServiceTableList`, `confirmButton`, ...) được đặt làm `id`/`class` của phần tử HTML tương ứng.

**Servlet / DAO / thực thể** (package `com.garaman`)

| Thiết kế | Cài đặt |
|---|---|
| ChooseServiceOrPartServlet: `init, doGet, doPost, searchServiceOrPart, showDetail` | `servlet/ChooseServiceOrPartServlet.java` (URL `/choose-service-or-part`) |
| PayServlet: `init, doGet, doPost, searchCustomer, searchOrder, showBill, confirm` | `servlet/PayServlet.java` (URL `/pay`) |
| ServiceTypeDAO: `getServiceByName, getServiceById` | `dao/ServiceTypeDAO.java` |
| PartTypeDAO: `getPartTypeByName, getPartTypeById` | `dao/PartTypeDAO.java` |
| CustomerDAO: `getCustomerByName` | `dao/CustomerDAO.java` |
| OrderDAO: `getOrderByCustomer, getOrderById, updateOrderStatus` | `dao/OrderDAO.java` |
| BillDAO: `createBill` | `dao/BillDAO.java` |
| Account, Customer, Staff, SalesStaff, TechniqueStaff, ServiceType, PartType, Batch, ServiceDetail, PartDetail, Order, Bill | `model/*.java` |

## 5. Các điểm khác/bổ sung so với tài liệu (cần biết khi bảo vệ)

- **Đăng nhập + phân quyền** (`LoginServlet`, `LogoutServlet`, `AuthFilter`, `AccountDAO`): tài liệu coi đăng nhập là tiền điều kiện nên mình bổ sung tối thiểu để chạy được.
- **Kiểu dữ liệu tiền**: ERD ghi `float`, mình dùng `DOUBLE` (DB) / `double` (Java) vì `float` mất chính xác với số tiền lớn.
- **Kiểu id**: id là `String` (UUID/chuỗi) theo ERD; sơ đồ lớp thiết kế ghi `customerId : int`, `orderId : int` nên không khớp ERD → code dùng `String`.
- **Tên lớp**: sơ đồ lớp module tìm kiếm ghi `ServiceDAO` nhưng kịch bản/constructor dùng `ServiceTypeDAO` → code dùng `ServiceTypeDAO`.
- **Công thức giảm giá** (`Customer.getDiscountPrice`): tài liệu chưa nêu công thức. Mình giả định cứ 100 điểm tích lũy giảm 1% tổng tiền, tối đa 10%. Sửa ở một chỗ duy nhất nếu thầy/cô quy định khác.
- **Tiền hóa đơn**: tiền dịch vụ = tổng giá `ServiceType` của các `ServiceDetail`; tiền phụ tùng = tổng giá `PartType` của các `PartDetail`.
- **Bước 44 (`confirm`)**: tính lại hóa đơn hoàn toàn ở máy chủ, không tin số tiền gửi lên từ trình duyệt.
- **Ngoại lệ E2/E4**: `confirm` chạy trong **một giao dịch** (đổi trạng thái đơn → lưu hóa đơn). Câu `UPDATE ... WHERE status <> ?` và ràng buộc `UNIQUE(tblOrderid)` của `tblBill` đảm bảo hai nhân viên cùng bấm xác nhận thì chỉ một người thành công, người còn lại nhận thông báo "Hóa đơn vừa được xử lý bởi nhân viên khác".
- **In hóa đơn**: sau khi thanh toán thành công trang tự mở hộp thoại in của trình duyệt (`window.print()`), có CSS `@media print`.
- **Bổ sung dịch vụ/phụ tùng vào hóa đơn** (bước 6–7 trong kịch bản chính của use case Thanh toán): chưa cài đặt vì không có trong kịch bản version 3 và sơ đồ lớp thiết kế.
- **Bảng bổ sung `tblAppointment`**: dùng cho use case "Đặt lịch hẹn" ở phân tích (không có trong ERD, chưa có chức năng).
- **Bảo mật**: câu lệnh SQL dùng `PreparedStatement`; escape `% _ \` trong từ khóa LIKE; chống XSS bằng `<c:out>`; token CSRF cho mọi form POST; đổi session id khi đăng nhập; mật khẩu băm SHA-256 (chỉ phù hợp bài học, hệ thống thật nên dùng BCrypt/Argon2).

## 6. Cấu trúc

```
garaman/
├── pom.xml
├── sql/garaman.sql
└── src/main
    ├── java/com/garaman/{util,model,dao,servlet,filter}
    ├── resources/db.properties
    └── webapp/{WEB-INF,css,customer,sales,index.jsp,other-home.jsp}
```
