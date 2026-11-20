package com.garaman.dao;

import com.garaman.model.Bill;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BillDAO extends DAO {
    private static final long serialVersionUID = 1L;


    public BillDAO() {
    }

    /** Luu hoa don (tu dong mo ket noi rieng). */
    public boolean createBill(Bill bill) {
        try (Connection c = getConnection()) {
            return createBill(bill, c);
        } catch (SQLException e) {
            throw new DataAccessException("Loi luu hoa don", e);
        }
    }

    /**
     * Luu hoa don trong mot giao dich co san. Moi don hang chi co mot hoa don (rang buoc UNIQUE),
     * nen luu trung se bi tu choi va tra ve false.
     */
    public boolean createBill(Bill bill, Connection c) {
        if (bill.getId() == null) {
            bill.setId(UUID.randomUUID().toString());
        }
        String sql = "INSERT INTO tblBill (id, servicesPrice, partsPrice, discount, total, paymentDate, tblOrderid) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, bill.getId());
            ps.setDouble(2, bill.getServicesPrice());
            ps.setDouble(3, bill.getPartsPrice());
            ps.setDouble(4, bill.getDiscount());
            ps.setDouble(5, bill.getTotal());
            ps.setDate(6, bill.getPaymentDate());
            ps.setString(7, bill.getOrder().getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                return false; // vi pham rang buoc: hoa don cua don hang nay da ton tai
            }
            throw new DataAccessException("Loi luu hoa don", e);
        }
    }

    /** Lay hoa don da luu cua mot don hang (bo sung de xem lai don hang da thanh toan). Null neu chua co. */
    public Bill getBillByOrder(String orderId) {
        String sql = "SELECT id, servicesPrice, partsPrice, discount, total, paymentDate "
                + "FROM tblBill WHERE tblOrderid = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Bill b = new Bill();
                b.setId(rs.getString("id"));
                b.setServicesPrice(rs.getDouble("servicesPrice"));
                b.setPartsPrice(rs.getDouble("partsPrice"));
                b.setDiscount(rs.getDouble("discount"));
                b.setTotal(rs.getDouble("total"));
                b.setPaymentDate(rs.getDate("paymentDate"));
                return b;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Loi lay hoa don theo don hang", e);
        }
    }
}
