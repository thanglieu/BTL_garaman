package com.garaman.model;

/** Phu tung cu the tham gia vao mot don hang. */
public class PartDetail {
    private String id;
    private Batch batch;
    private PartType type;

    public PartDetail() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public PartType getType() {
        return type;
    }

    public void setType(PartType type) {
        this.type = type;
    }

    /** Gia cua phu tung chi tiet = gia cua loai phu tung. */
    public double getPartPrice() {
        return type == null ? 0 : type.getPrice();
    }

}
