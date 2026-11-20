package com.garaman.util;

/** Tien ich cho truy van LIKE. */
public final class SqlUtil {
    private SqlUtil() {
    }

    /** Vo hieu hoa cac ky tu dac biet cua LIKE (\ % _) trong tu khoa nguoi dung nhap. */
    public static String likeContains(String keyword) {
        String k = keyword == null ? "" : keyword.trim();
        if (k.length() > 100) {
            k = k.substring(0, 100);
        }
        k = k.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
        return "%" + k + "%";
    }
}
