-- =====================================================================
--  GaraMan - Co so du lieu (MySQL 8 / MariaDB 10.x)
--  Theo ERD trong tai lieu phan tich thiet ke (pha thiet ke, muc 2.2)
--  Chay:  mysql -u root -p < sql/garaman.sql
-- =====================================================================
-- Bat buoc: de du lieu tieng Viet luon duoc nap dung UTF-8 bat ke cau hinh client
SET NAMES utf8mb4;

DROP DATABASE IF EXISTS garaman;
CREATE DATABASE garaman CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE garaman;

CREATE TABLE tblAccount (
  id          VARCHAR(36)  NOT NULL,
  name        VARCHAR(100) NULL,
  username    VARCHAR(50)  NOT NULL,
  password    VARCHAR(255) NOT NULL,          -- SHA-256 hex (xem PasswordUtil)
  phoneNumber VARCHAR(15)  NULL,
  birthDay    DATE         NULL,
  address     VARCHAR(255) NULL,
  email       VARCHAR(100) NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_account_username (username)
) ENGINE=InnoDB;

CREATE TABLE tblStaff (
  tblAccountid   VARCHAR(36) NOT NULL,
  salary         DOUBLE      NULL,
  experienceYear INT         NULL,
  PRIMARY KEY (tblAccountid),
  CONSTRAINT fk_staff_account FOREIGN KEY (tblAccountid) REFERENCES tblAccount(id)
) ENGINE=InnoDB;

CREATE TABLE tblManager (
  tblStaffid VARCHAR(36) NOT NULL,
  PRIMARY KEY (tblStaffid),
  CONSTRAINT fk_manager_staff FOREIGN KEY (tblStaffid) REFERENCES tblStaff(tblAccountid)
) ENGINE=InnoDB;

CREATE TABLE tblWarehouseStaff (
  tblStaffid VARCHAR(36) NOT NULL,
  PRIMARY KEY (tblStaffid),
  CONSTRAINT fk_wh_staff FOREIGN KEY (tblStaffid) REFERENCES tblStaff(tblAccountid)
) ENGINE=InnoDB;

CREATE TABLE tblSalesStaff (
  tblStaffid VARCHAR(36) NOT NULL,
  PRIMARY KEY (tblStaffid),
  CONSTRAINT fk_sales_staff FOREIGN KEY (tblStaffid) REFERENCES tblStaff(tblAccountid)
) ENGINE=InnoDB;

CREATE TABLE tblTechniqueStaff (
  tblStaffid VARCHAR(36) NOT NULL,
  PRIMARY KEY (tblStaffid),
  CONSTRAINT fk_tech_staff FOREIGN KEY (tblStaffid) REFERENCES tblStaff(tblAccountid)
) ENGINE=InnoDB;

CREATE TABLE tblCustomer (
  tblAccountid VARCHAR(36) NOT NULL,
  point        INT         NOT NULL DEFAULT 0,
  PRIMARY KEY (tblAccountid),
  CONSTRAINT fk_customer_account FOREIGN KEY (tblAccountid) REFERENCES tblAccount(id)
) ENGINE=InnoDB;

CREATE TABLE tblCar (
  id            VARCHAR(36) NOT NULL,
  licensePlate  VARCHAR(20) NOT NULL,
  carType       VARCHAR(50) NULL,
  tblCustomerid VARCHAR(36) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_car_customer FOREIGN KEY (tblCustomerid) REFERENCES tblCustomer(tblAccountid)
) ENGINE=InnoDB;

CREATE TABLE tblServiceType (
  id          VARCHAR(36)  NOT NULL,
  name        VARCHAR(100) NOT NULL,
  description VARCHAR(255) NULL,
  price       DOUBLE       NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_servicetype_name (name)
) ENGINE=InnoDB;

CREATE TABLE tblPartType (
  id          VARCHAR(36)  NOT NULL,
  name        VARCHAR(100) NOT NULL,
  description VARCHAR(255) NULL,
  price       DOUBLE       NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_parttype_name (name)
) ENGINE=InnoDB;

CREATE TABLE tblSupplier (
  id          VARCHAR(36)  NOT NULL,
  name        VARCHAR(100) NOT NULL,
  phoneNumber VARCHAR(15)  NULL,
  address     VARCHAR(255) NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE tblImportBill (
  id                  VARCHAR(36) NOT NULL,
  importDate          DATE        NULL,
  tblSupplierid       VARCHAR(36) NOT NULL,
  tblWarehouseStaffid VARCHAR(36) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_ib_supplier FOREIGN KEY (tblSupplierid)       REFERENCES tblSupplier(id),
  CONSTRAINT fk_ib_wstaff   FOREIGN KEY (tblWarehouseStaffid) REFERENCES tblWarehouseStaff(tblStaffid)
) ENGINE=InnoDB;

CREATE TABLE tblBatch (
  id              VARCHAR(36) NOT NULL,
  quantity        INT         NOT NULL DEFAULT 0,
  expiryDate      DATE        NULL,
  price           DOUBLE      NOT NULL DEFAULT 0,   -- gia nhap
  tblImportBillid VARCHAR(36) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_batch_ib FOREIGN KEY (tblImportBillid) REFERENCES tblImportBill(id)
) ENGINE=InnoDB;

CREATE TABLE tblOrder (
  id              VARCHAR(36)  NOT NULL,
  createdDate     DATE         NULL,
  status          TINYINT(1)   NOT NULL DEFAULT 0,   -- 0: chua thanh toan, 1: da thanh toan
  description     VARCHAR(255) NULL,
  note            VARCHAR(255) NULL,
  tblSalesStaffid VARCHAR(36)  NULL,
  tblCustomerid   VARCHAR(36)  NOT NULL,
  PRIMARY KEY (id),
  KEY idx_order_customer (tblCustomerid),
  CONSTRAINT fk_order_sales    FOREIGN KEY (tblSalesStaffid) REFERENCES tblSalesStaff(tblStaffid),
  CONSTRAINT fk_order_customer FOREIGN KEY (tblCustomerid)   REFERENCES tblCustomer(tblAccountid)
) ENGINE=InnoDB;

CREATE TABLE tblCarsList (
  id         VARCHAR(36) NOT NULL,
  tblCarid   VARCHAR(36) NOT NULL,
  tblOrderid VARCHAR(36) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_cl_car   FOREIGN KEY (tblCarid)   REFERENCES tblCar(id),
  CONSTRAINT fk_cl_order FOREIGN KEY (tblOrderid) REFERENCES tblOrder(id)
) ENGINE=InnoDB;

CREATE TABLE tblServiceDetail (
  id                VARCHAR(36) NOT NULL,
  tblServiceTypeid  VARCHAR(36) NOT NULL,
  tblOrderid        VARCHAR(36) NOT NULL,
  tblTechniqueid    VARCHAR(36) NULL,
  PRIMARY KEY (id),
  KEY idx_sd_order (tblOrderid),
  CONSTRAINT fk_sd_type  FOREIGN KEY (tblServiceTypeid) REFERENCES tblServiceType(id),
  CONSTRAINT fk_sd_order FOREIGN KEY (tblOrderid)       REFERENCES tblOrder(id),
  CONSTRAINT fk_sd_tech  FOREIGN KEY (tblTechniqueid)   REFERENCES tblTechniqueStaff(tblStaffid)
) ENGINE=InnoDB;

CREATE TABLE tblPartDetail (
  id             VARCHAR(36) NOT NULL,
  tblBatchid     VARCHAR(36) NULL,
  tblPartTypeid  VARCHAR(36) NOT NULL,
  tblOrderid     VARCHAR(36) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_pd_order (tblOrderid),
  CONSTRAINT fk_pd_batch FOREIGN KEY (tblBatchid)    REFERENCES tblBatch(id),
  CONSTRAINT fk_pd_type  FOREIGN KEY (tblPartTypeid) REFERENCES tblPartType(id),
  CONSTRAINT fk_pd_order FOREIGN KEY (tblOrderid)    REFERENCES tblOrder(id)
) ENGINE=InnoDB;

CREATE TABLE tblBill (
  id            VARCHAR(36) NOT NULL,
  servicesPrice DOUBLE      NOT NULL DEFAULT 0,
  partsPrice    DOUBLE      NOT NULL DEFAULT 0,
  discount      DOUBLE      NOT NULL DEFAULT 0,
  total         DOUBLE      NOT NULL DEFAULT 0,
  paymentDate   DATE        NULL,
  tblOrderid    VARCHAR(36) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_bill_order (tblOrderid),           -- 1 don hang <-> 1 hoa don
  CONSTRAINT fk_bill_order FOREIGN KEY (tblOrderid) REFERENCES tblOrder(id)
) ENGINE=InnoDB;

-- Bang bo sung (khong co trong ERD): lich hen truc tuyen cua khach hang
CREATE TABLE tblAppointment (
  id               VARCHAR(36)  NOT NULL,
  appointmentTime  DATETIME     NOT NULL,
  note             VARCHAR(255) NULL,
  tblCustomerid    VARCHAR(36)  NOT NULL,
  tblServiceTypeid VARCHAR(36)  NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_ap_customer FOREIGN KEY (tblCustomerid)    REFERENCES tblCustomer(tblAccountid),
  CONSTRAINT fk_ap_service  FOREIGN KEY (tblServiceTypeid) REFERENCES tblServiceType(id)
) ENGINE=InnoDB;

-- =====================================================================
--  DU LIEU MAU   (mat khau tat ca tai khoan: 123456)
-- =====================================================================
SET @pw = '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92';

INSERT INTO tblAccount (id,name,username,password,phoneNumber,birthDay,address,email) VALUES
 ('acc-mgr-1','Trần Quản Lý',   'manager1',  @pw,'0900000001','1985-03-10','Hà Nội','manager1@garaman.vn'),
 ('acc-wh-1', 'Lê Thị Kho',     'kho1',      @pw,'0900000002','1990-07-21','Hà Nội','kho1@garaman.vn'),
 ('acc-sal-1','Phạm Bán Hàng',  'sales1',    @pw,'0900000003','1992-11-05','Hà Nội','sales1@garaman.vn'),
 ('acc-tec-1','Hoàng Kỹ Thuật', 'kythuat1',  @pw,'0900000004','1988-01-15','Hà Nội','kythuat1@garaman.vn'),
 ('acc-tec-2','Vũ Sửa Xe',      'kythuat2',  @pw,'0900000005','1991-05-30','Hà Nội','kythuat2@garaman.vn'),
 ('acc-cus-1','Nguyễn Văn A',   'customer1', @pw,'0934823487','1995-02-14','Hà Nội','a@example.com'),
 ('acc-cus-2','Nguyễn Đình B',  'customer2', @pw,'0943534545','1994-09-09','Hà Nội','b@example.com'),
 ('acc-cus-3','Nguyễn Chí C',   'customer3', @pw,'0323234456','1990-12-12','Hà Nội','c@example.com'),
 ('acc-cus-4','Nguyễn Bình D',  'customer4', @pw,'0324887645','1998-06-01','Hà Nội','d@example.com');

INSERT INTO tblStaff (tblAccountid,salary,experienceYear) VALUES
 ('acc-mgr-1',30000000,10),('acc-wh-1',12000000,5),('acc-sal-1',10000000,3),
 ('acc-tec-1',14000000,6),('acc-tec-2',13000000,4);
INSERT INTO tblManager        VALUES ('acc-mgr-1');
INSERT INTO tblWarehouseStaff VALUES ('acc-wh-1');
INSERT INTO tblSalesStaff     VALUES ('acc-sal-1');
INSERT INTO tblTechniqueStaff VALUES ('acc-tec-1'),('acc-tec-2');

INSERT INTO tblCustomer (tblAccountid,point) VALUES
 ('acc-cus-1',250),('acc-cus-2',40),('acc-cus-3',0),('acc-cus-4',1200);

INSERT INTO tblCar (id,licensePlate,carType,tblCustomerid) VALUES
 ('car-1','30A-123.45','VinFast VF4','acc-cus-1'),
 ('car-2','29B-678.90','Toyota Vios','acc-cus-1'),
 ('car-3','30F-111.22','Honda City','acc-cus-2');

INSERT INTO tblServiceType (id,name,description,price) VALUES
 ('svc-1','Thay lốp xe',          'Tháo lốp cũ, lắp lốp mới, cân bằng động và đảo lốp.',2000000),
 ('svc-2','Thay kính xe',         'Thay kính chắn gió/kính cửa chính hãng, có bảo hành 12 tháng.',1500000),
 ('svc-3','Kiểm tra tổng thể xe', 'Kiểm tra động cơ, phanh, hệ thống treo, điện và đưa ra báo cáo tình trạng xe.',10000000),
 ('svc-4','Thay dầu nhớt',        'Thay dầu động cơ và lọc dầu, kiểm tra các mức dung dịch.',300000),
 ('svc-5','Bảo dưỡng định kỳ',    'Gói bảo dưỡng theo quãng đường: vệ sinh, bôi trơn, kiểm tra an toàn.',1200000),
 ('svc-6','Sơn lại thân xe',      'Sơn dặm hoặc sơn toàn bộ thân xe, bảo hành màu sơn 24 tháng.',8000000);

INSERT INTO tblPartType (id,name,description,price) VALUES
 ('prt-1','Lốp xe 205/55R16', 'Lốp không săm, phù hợp xe hạng B/C.',1800000),
 ('prt-2','Kính xe phụ',      'Kính phụ (kính cửa sau/kính hậu) chính hãng.',300000),
 ('prt-3','Bugi Iridium',     'Bugi tuổi thọ cao, tiết kiệm nhiên liệu.',150000),
 ('prt-4','Dầu nhớt 5W-30',   'Dầu tổng hợp 5W-30, can 4 lít.',650000),
 ('prt-5','Ắc quy 12V-60Ah',  'Ắc quy khô, bảo hành 12 tháng.',2200000),
 ('prt-6','Má phanh trước',   'Bộ má phanh đĩa trước.',900000);

INSERT INTO tblSupplier (id,name,phoneNumber,address) VALUES
 ('sup-1','Công ty Phụ tùng Á Châu','0241234567','Hà Nội'),
 ('sup-2','Đại lý Lốp Miền Bắc',     '0247654321','Hải Phòng');

INSERT INTO tblImportBill (id,importDate,tblSupplierid,tblWarehouseStaffid) VALUES
 ('imp-1','2026-10-01','sup-1','acc-wh-1'),
 ('imp-2','2026-10-15','sup-2','acc-wh-1');

INSERT INTO tblBatch (id,quantity,expiryDate,price,tblImportBillid) VALUES
 ('bat-1',20,'2029-10-01',1500000,'imp-2'),
 ('bat-2',50,'2028-10-01', 200000,'imp-1'),
 ('bat-3',30,'2028-10-01', 500000,'imp-1'),
 ('bat-4',10,'2028-12-31',1900000,'imp-1');

INSERT INTO tblOrder (id,createdDate,status,description,note,tblSalesStaffid,tblCustomerid) VALUES
 ('ord-123','2026-11-01',1,'Thay lốp xe VF4','Khách đã nhận xe',                'acc-sal-1','acc-cus-1'),
 ('ord-129','2026-11-10',0,'Bảo dưỡng xe Toyota','Kèm thay dầu và bugi',        'acc-sal-1','acc-cus-1'),
 ('ord-132','2026-11-12',0,'Thay kính xe Honda','Thay 3 kính phụ, mỗi kính giá 300000','acc-sal-1','acc-cus-1'),
 ('ord-140','2026-11-15',0,'Thay dầu nhớt','',                                   'acc-sal-1','acc-cus-2');

INSERT INTO tblCarsList (id,tblCarid,tblOrderid) VALUES
 ('cl-1','car-1','ord-123'),('cl-2','car-2','ord-129'),('cl-3','car-2','ord-132'),('cl-4','car-3','ord-140');

INSERT INTO tblServiceDetail (id,tblServiceTypeid,tblOrderid,tblTechniqueid) VALUES
 ('sd-1','svc-1','ord-123','acc-tec-1'),
 ('sd-2','svc-5','ord-129','acc-tec-1'),
 ('sd-3','svc-4','ord-129','acc-tec-2'),
 ('sd-4','svc-2','ord-132','acc-tec-2'),
 ('sd-5','svc-4','ord-140','acc-tec-1');

INSERT INTO tblPartDetail (id,tblBatchid,tblPartTypeid,tblOrderid) VALUES
 ('pd-1','bat-1','prt-1','ord-123'),('pd-2','bat-1','prt-1','ord-123'),
 ('pd-3','bat-2','prt-3','ord-129'),('pd-4','bat-3','prt-4','ord-129'),
 ('pd-5','bat-4','prt-2','ord-132'),('pd-6','bat-4','prt-2','ord-132'),('pd-7','bat-4','prt-2','ord-132'),
 ('pd-8','bat-3','prt-4','ord-140');

-- Hoa don da luu cho don hang da thanh toan (ord-123): giam gia 2% (250 diem)
INSERT INTO tblBill (id,servicesPrice,partsPrice,discount,total,paymentDate,tblOrderid) VALUES
 ('bill-1',2000000,3600000,112000,5488000,'2026-11-02','ord-123');

INSERT INTO tblAppointment (id,appointmentTime,note,tblCustomerid,tblServiceTypeid) VALUES
 ('ap-1','2026-11-25 09:00:00','Đặt lịch thay dầu','acc-cus-2','svc-4');
