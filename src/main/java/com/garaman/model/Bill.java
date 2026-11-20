package com.garaman.model;

import java.sql.Date;

/** Hoa don cua mot don hang (quan he 1-1 voi Order). */
public class Bill {
    private String id;
    private double servicesPrice;
    private double partsPrice;
    private double discount;
    private double total;
    private java.sql.Date paymentDate;
    private Order order;

    public Bill() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getServicesPrice() {
        return servicesPrice;
    }

    public void setServicesPrice(double servicesPrice) {
        this.servicesPrice = servicesPrice;
    }

    public double getPartsPrice() {
        return partsPrice;
    }

    public void setPartsPrice(double partsPrice) {
        this.partsPrice = partsPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public java.sql.Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(java.sql.Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
