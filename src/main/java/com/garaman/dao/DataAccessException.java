package com.garaman.dao;

/** Loi truy cap CSDL (boc SQLException de DAO khong phai khai bao throws). */
public class DataAccessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
