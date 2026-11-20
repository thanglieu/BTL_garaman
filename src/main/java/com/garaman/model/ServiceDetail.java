package com.garaman.model;

/** Dich vu cu the gan voi mot don hang. */
public class ServiceDetail {
    private String id;
    private TechniqueStaff engineer;
    private ServiceType type;

    public ServiceDetail() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TechniqueStaff getEngineer() {
        return engineer;
    }

    public void setEngineer(TechniqueStaff engineer) {
        this.engineer = engineer;
    }

    public ServiceType getType() {
        return type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    /** Gia cua dich vu chi tiet = gia cua loai dich vu. */
    public double getServicePrice() {
        return type == null ? 0 : type.getPrice();
    }

}
