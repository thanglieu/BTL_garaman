package com.garaman.dao;

import com.garaman.model.Customer;
import com.garaman.util.SqlUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends DAO {
    private static final long serialVersionUID = 1L;

    private static final String SELECT =
            "SELECT a.id, a.name, a.username, a.phoneNumber, a.birthDay, a.address, a.email, c.point "
            + "FROM tblCustomer c JOIN tblAccount a ON a.id = c.tblAccountid ";

    public CustomerDAO() {
    }

    /** Tim cac khach hang co ten chua tu khoa (khong tra ve mat khau). */
    public List<Customer> getCustomerByName(String name) {
        String sql = SELECT + "WHERE a.name LIKE ? ORDER BY a.name LIMIT 100";
        List<Customer> list = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, SqlUtil.likeContains(name));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Loi tim khach hang theo ten", e);
        }
        return list;
    }

    /** Lay mot khach hang theo id (bo sung de hien ten khach tren giao dien danh sach don hang). */
    public Customer getCustomerById(String id) {
        String sql = SELECT + "WHERE a.id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Loi lay khach hang theo id", e);
        }
    }

    private Customer map(ResultSet rs) throws SQLException {
        Customer cu = new Customer();
        cu.setId(rs.getString("id"));
        cu.setName(rs.getString("name"));
        cu.setUsername(rs.getString("username"));
        cu.setPhoneNumber(rs.getString("phoneNumber"));
        cu.setBirthDay(rs.getDate("birthDay"));
        cu.setAddress(rs.getString("address"));
        cu.setEmail(rs.getString("email"));
        cu.setPoint(rs.getInt("point"));
        return cu;
    }
}
