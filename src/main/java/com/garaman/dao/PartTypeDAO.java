package com.garaman.dao;

import com.garaman.model.PartType;
import com.garaman.util.SqlUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartTypeDAO extends DAO {
    private static final long serialVersionUID = 1L;


    public PartTypeDAO() {
    }

    /** Tim cac phu tung co ten chua tu khoa. */
    public List<PartType> getPartTypeByName(String name) {
        String sql = "SELECT id, name, description, price FROM tblPartType "
                + "WHERE name LIKE ? ORDER BY name LIMIT 200";
        List<PartType> list = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, SqlUtil.likeContains(name));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Loi tim phu tung theo ten", e);
        }
        return list;
    }

    /** Lay chi tiet mot phu tung, tra ve null neu khong ton tai. */
    public PartType getPartTypeById(String id) {
        String sql = "SELECT id, name, description, price FROM tblPartType WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Loi lay phu tung theo id", e);
        }
    }

    private PartType map(ResultSet rs) throws SQLException {
        PartType s = new PartType();
        s.setId(rs.getString("id"));
        s.setName(rs.getString("name"));
        s.setDescription(rs.getString("description"));
        s.setPrice(rs.getDouble("price"));
        return s;
    }
}
