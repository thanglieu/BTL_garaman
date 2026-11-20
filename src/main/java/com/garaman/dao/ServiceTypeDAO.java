package com.garaman.dao;

import com.garaman.model.ServiceType;
import com.garaman.util.SqlUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceTypeDAO extends DAO {
    private static final long serialVersionUID = 1L;


    public ServiceTypeDAO() {
    }

    /** Tim cac dich vu co ten chua tu khoa. */
    public List<ServiceType> getServiceByName(String name) {
        String sql = "SELECT id, name, description, price FROM tblServiceType "
                + "WHERE name LIKE ? ORDER BY name LIMIT 200";
        List<ServiceType> list = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, SqlUtil.likeContains(name));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Loi tim dich vu theo ten", e);
        }
        return list;
    }

    /** Lay chi tiet mot dich vu, tra ve null neu khong ton tai. */
    public ServiceType getServiceById(String id) {
        String sql = "SELECT id, name, description, price FROM tblServiceType WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Loi lay dich vu theo id", e);
        }
    }

    private ServiceType map(ResultSet rs) throws SQLException {
        ServiceType s = new ServiceType();
        s.setId(rs.getString("id"));
        s.setName(rs.getString("name"));
        s.setDescription(rs.getString("description"));
        s.setPrice(rs.getDouble("price"));
        return s;
    }
}
