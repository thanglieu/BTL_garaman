package com.garaman.dao;

import com.garaman.model.Batch;
import com.garaman.model.Customer;
import com.garaman.model.Order;
import com.garaman.model.PartDetail;
import com.garaman.model.PartType;
import com.garaman.model.SalesStaff;
import com.garaman.model.ServiceDetail;
import com.garaman.model.ServiceType;
import com.garaman.model.TechniqueStaff;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends DAO {
    private static final long serialVersionUID = 1L;


    public OrderDAO() {
    }

    /** Danh sach don hang cua mot khach hang (moi nhat truoc), chua nap chi tiet dich vu/phu tung. */
    public List<Order> getOrderByCustomer(String customerId) {
        String sql = "SELECT id, createdDate, status, description, note, tblCustomerid "
                + "FROM tblOrder WHERE tblCustomerid = ? ORDER BY createdDate DESC, id DESC";
        List<Order> list = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = new Order();
                    o.setId(rs.getString("id"));
                    o.setCreatedDate(rs.getDate("createdDate"));
                    o.setStatus(rs.getBoolean("status"));
                    o.setDescription(rs.getString("description"));
                    o.setNote(rs.getString("note"));
                    Customer g = new Customer();
                    g.setId(rs.getString("tblCustomerid"));
                    o.setGuest(g);
                    list.add(o);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Loi lay don hang cua khach hang", e);
        }
        return list;
    }

    /** Lay don hang day du: khach hang, nhan vien ban hang, danh sach dich vu va phu tung. Null neu khong co. */
    public Order getOrderById(String orderId) {
        String sqlOrder = "SELECT o.id, o.createdDate, o.status, o.description, o.note, "
                + "ca.id AS gid, ca.name AS gname, ca.phoneNumber AS gphone, cu.point AS gpoint, "
                + "sa.id AS sid, sa.name AS sname "
                + "FROM tblOrder o "
                + "JOIN tblCustomer cu ON cu.tblAccountid = o.tblCustomerid "
                + "JOIN tblAccount ca ON ca.id = cu.tblAccountid "
                + "LEFT JOIN tblAccount sa ON sa.id = o.tblSalesStaffid "
                + "WHERE o.id = ?";
        String sqlService = "SELECT sd.id AS sdid, st.id AS stid, st.name AS stname, st.description AS stdesc, "
                + "st.price AS stprice, ta.id AS taid, ta.name AS taname "
                + "FROM tblServiceDetail sd "
                + "JOIN tblServiceType st ON st.id = sd.tblServiceTypeid "
                + "LEFT JOIN tblAccount ta ON ta.id = sd.tblTechniqueid "
                + "WHERE sd.tblOrderid = ? ORDER BY sd.id";
        String sqlPart = "SELECT pd.id AS pdid, pt.id AS ptid, pt.name AS ptname, pt.description AS ptdesc, "
                + "pt.price AS ptprice, b.id AS bid, b.quantity AS bqty, b.expiryDate AS bexp, b.price AS bprice "
                + "FROM tblPartDetail pd "
                + "JOIN tblPartType pt ON pt.id = pd.tblPartTypeid "
                + "LEFT JOIN tblBatch b ON b.id = pd.tblBatchid "
                + "WHERE pd.tblOrderid = ? ORDER BY pd.id";
        try (Connection c = getConnection()) {
            Order o;
            try (PreparedStatement ps = c.prepareStatement(sqlOrder)) {
                ps.setString(1, orderId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }
                    o = new Order();
                    o.setId(rs.getString("id"));
                    o.setCreatedDate(rs.getDate("createdDate"));
                    o.setStatus(rs.getBoolean("status"));
                    o.setDescription(rs.getString("description"));
                    o.setNote(rs.getString("note"));
                    Customer g = new Customer();
                    g.setId(rs.getString("gid"));
                    g.setName(rs.getString("gname"));
                    g.setPhoneNumber(rs.getString("gphone"));
                    g.setPoint(rs.getInt("gpoint"));
                    o.setGuest(g);
                    if (rs.getString("sid") != null) {
                        SalesStaff s = new SalesStaff();
                        s.setId(rs.getString("sid"));
                        s.setName(rs.getString("sname"));
                        o.setSalesman(s);
                    }
                }
            }
            try (PreparedStatement ps = c.prepareStatement(sqlService)) {
                ps.setString(1, orderId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ServiceType t = new ServiceType();
                        t.setId(rs.getString("stid"));
                        t.setName(rs.getString("stname"));
                        t.setDescription(rs.getString("stdesc"));
                        t.setPrice(rs.getDouble("stprice"));
                        ServiceDetail d = new ServiceDetail();
                        d.setId(rs.getString("sdid"));
                        d.setType(t);
                        if (rs.getString("taid") != null) {
                            TechniqueStaff e = new TechniqueStaff();
                            e.setId(rs.getString("taid"));
                            e.setName(rs.getString("taname"));
                            d.setEngineer(e);
                        }
                        o.getServiceList().add(d);
                    }
                }
            }
            try (PreparedStatement ps = c.prepareStatement(sqlPart)) {
                ps.setString(1, orderId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        PartType t = new PartType();
                        t.setId(rs.getString("ptid"));
                        t.setName(rs.getString("ptname"));
                        t.setDescription(rs.getString("ptdesc"));
                        t.setPrice(rs.getDouble("ptprice"));
                        PartDetail d = new PartDetail();
                        d.setId(rs.getString("pdid"));
                        d.setType(t);
                        if (rs.getString("bid") != null) {
                            Batch b = new Batch();
                            b.setId(rs.getString("bid"));
                            b.setQuantity(rs.getInt("bqty"));
                            b.setExpiryDate(rs.getDate("bexp"));
                            b.setPrice(rs.getDouble("bprice"));
                            d.setBatch(b);
                        }
                        o.getPartList().add(d);
                    }
                }
            }
            return o;
        } catch (SQLException e) {
            throw new DataAccessException("Loi lay don hang theo id", e);
        }
    }

    /** Cap nhat trang thai (tu dong mo ket noi rieng). */
    public boolean updateOrderStatus(String orderId, boolean status) {
        try (Connection c = getConnection()) {
            return updateOrderStatus(orderId, status, c);
        } catch (SQLException e) {
            throw new DataAccessException("Loi cap nhat trang thai don hang", e);
        }
    }

    /**
     * Cap nhat trang thai trong mot giao dich co san. Chi cap nhat khi trang thai dang khac gia tri moi
     * nen neu don hang da duoc nhan vien khac thanh toan truoc do thi tra ve false (xu ly xung dot E2).
     */
    public boolean updateOrderStatus(String orderId, boolean status, Connection c) {
        String sql = "UPDATE tblOrder SET status = ? WHERE id = ? AND status <> ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setString(2, orderId);
            ps.setBoolean(3, status);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataAccessException("Loi cap nhat trang thai don hang", e);
        }
    }
}
