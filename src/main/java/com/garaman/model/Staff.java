package com.garaman.model;

/** Nhan vien (ke thua Account). */
public class Staff extends Account {
    private double salary;
    private int experienceYear;

    public Staff() {
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getExperienceYear() {
        return experienceYear;
    }

    public void setExperienceYear(int experienceYear) {
        this.experienceYear = experienceYear;
    }

}
