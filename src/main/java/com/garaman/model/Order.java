package com.garaman.model;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

/** Don hang. status = true nghia la da thanh toan. */
public class Order {
    private String id;
    private java.sql.Date createdDate;
    private boolean status;
    private String description;
    private String note;
    private Customer guest;
    private SalesStaff salesman;
    private List<PartDetail> partList = new ArrayList<>();
    private List<ServiceDetail> serviceList = new ArrayList<>();

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public java.sql.Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(java.sql.Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Customer getGuest() {
        return guest;
    }

    public void setGuest(Customer guest) {
        this.guest = guest;
    }

    public SalesStaff getSalesman() {
        return salesman;
    }

    public void setSalesman(SalesStaff salesman) {
        this.salesman = salesman;
    }

    public List<PartDetail> getPartList() {
        return partList;
    }

    public void setPartList(List<PartDetail> partList) {
        this.partList = partList;
    }

    public List<ServiceDetail> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<ServiceDetail> serviceList) {
        this.serviceList = serviceList;
    }

    /** Tong tien cac dich vu trong don hang. */
    public double getServicesPrice() {
        double sum = 0;
        for (ServiceDetail s : serviceList) {
            sum += s.getServicePrice();
        }
        return sum;
    }

    /** Tong tien cac phu tung trong don hang. */
    public double getPartsPrice() {
        double sum = 0;
        for (PartDetail p : partList) {
            sum += p.getPartPrice();
        }
        return sum;
    }

}
