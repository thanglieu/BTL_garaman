package com.garaman.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Tao ket noi JDBC tu file db.properties (hoac bien moi truong). */
public final class DBUtil {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                PROPS.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
        String driver = PROPS.getProperty("db.driver");
        if (driver != null && !driver.isEmpty()) {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    private DBUtil() {
    }

    private static String get(String key, String env) {
        String v = System.getenv(env);
        return (v != null && !v.isEmpty()) ? v : PROPS.getProperty(key);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                get("db.url", "GARAMAN_DB_URL"),
                get("db.user", "GARAMAN_DB_USER"),
                get("db.password", "GARAMAN_DB_PASSWORD"));
    }
}
