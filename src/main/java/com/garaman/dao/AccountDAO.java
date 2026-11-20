package com.garaman.dao;

import com.garaman.model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Phuc vu dang nhap (use case "Dang nhap" dung chung cho moi tac nhan). */
public class AccountDAO extends DAO {
    private static final long serialVersionUID = 1L;


    public AccountDAO() {
    }

    /** Tim tai khoan theo ten dang nhap (co mat khau da bam de doi chieu). Null neu khong co. */
    public Account getAccountByUsername(String username) {
        String sql = "SELECT id, name, username, password, phoneNumber, birthDay, address, email "
                + "FROM tblAccount WHERE username = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Account a = new Account();
                a.setId(rs.getString("id"));
                a.setName(rs.getString("name"));
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                a.setPhoneNumber(rs.getString("phoneNumber"));
                a.setBirthDay(rs.getDate("birthDay"));
                a.setAddress(rs.getString("address"));
                a.setEmail(rs.getString("email"));
                return a;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Loi tim tai khoan", e);
        }
    }

    /** Xac dinh vai tro: CUSTOMER, SALES, MANAGER, WAREHOUSE, TECHNIQUE (null neu khong co vai tro). */
    public String getRole(String accountId) {
        String sql = "SELECT 'CUSTOMER' AS r FROM tblCustomer WHERE tblAccountid = ? "
                + "UNION ALL SELECT 'SALES' FROM tblSalesStaff WHERE tblStaffid = ? "
                + "UNION ALL SELECT 'MANAGER' FROM tblManager WHERE tblStaffid = ? "
                + "UNION ALL SELECT 'WAREHOUSE' FROM tblWarehouseStaff WHERE tblStaffid = ? "
                + "UNION ALL SELECT 'TECHNIQUE' FROM tblTechniqueStaff WHERE tblStaffid = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                ps.setString(i, accountId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("r") : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Loi xac dinh vai tro", e);
        }
    }
}
