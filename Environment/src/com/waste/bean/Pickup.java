package com.waste.bean;

import java.sql.Date;

public class Pickup {

    private int pickupID;
    private String zoneID;
    private String vehicleID;
    private Date pickupDate;
    private String startTime;
    private int expectedVolumeKg;
    private Integer actualVolumeKg;
    private String status;

    public int getPickupID() {
        return pickupID;
    }

    public void setPickupID(int pickupID) {
        this.pickupID = pickupID;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getExpectedVolumeKg() {
        return expectedVolumeKg;
    }

    public void setExpectedVolumeKg(int expectedVolumeKg) {
        this.expectedVolumeKg = expectedVolumeKg;
    }

    public Integer getActualVolumeKg() {
        return actualVolumeKg;
    }

    public void setActualVolumeKg(Integer actualVolumeKg) {
        this.actualVolumeKg = actualVolumeKg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}