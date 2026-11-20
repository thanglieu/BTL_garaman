package com.garaman.model;

/** Khach hang (ke thua Account), co diem tich luy. */
public class Customer extends Account {
    private int point;

    public Customer() {
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    /**
     * Giam gia theo diem tich luy: cu 100 diem giam 1% tren tong tien, toi da 10%.
     * Ket qua lam tron den dong.
     */
    public double getDiscountPrice(double subtotal) {
        double percent = Math.min(point / 100, 10);
        return Math.round(subtotal * percent / 100.0);
    }

}
