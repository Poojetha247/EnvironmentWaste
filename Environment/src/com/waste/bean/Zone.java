package com.waste.bean;

public class Zone {

    private String zoneID;
    private String zoneName;
    private String zoneType;
    private double areaSqKm;
    private String status;

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneType() {
        return zoneType;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
    }

    public double getAreaSqKm() {
        return areaSqKm;
    }

    public void setAreaSqKm(double areaSqKm) {
        this.areaSqKm = areaSqKm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
