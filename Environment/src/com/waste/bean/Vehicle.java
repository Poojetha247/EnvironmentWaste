package com.waste.bean;

public class Vehicle {

    private String vehicleID;
    private String vehicleType;
    private int capacityKg;
    private String suitability;
    private String status;

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getCapacityKg() {
        return capacityKg;
    }

    public void setCapacityKg(int capacityKg) {
        this.capacityKg = capacityKg;
    }

    public String getSuitability() {
        return suitability;
    }

    public void setSuitability(String suitability) {
        this.suitability = suitability;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
