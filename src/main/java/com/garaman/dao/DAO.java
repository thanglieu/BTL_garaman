package com.garaman.dao;

import com.garaman.util.DBUtil;
import java.sql.Connection;
import java.sql.SQLException;

/** Lop co so cho cac DAO: cung cap getConnection() nhu trong so do lop. */
public abstract class DAO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    protected Connection getConnection() throws SQLException {
        return DBUtil.getConnection();
    }
}
