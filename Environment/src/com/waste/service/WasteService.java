package com.waste.service;

import com.waste.bean.Pickup;
import com.waste.bean.Vehicle;
import com.waste.bean.Zone;
import com.waste.dao.PickupDAO;
import com.waste.dao.VehicleDAO;
import com.waste.dao.ZoneDAO;
import com.waste.util.ActivePickupExistsException;
import com.waste.util.DBUtil;
import com.waste.util.ValidationException;
import com.waste.util.VehicleNotSuitableException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class WasteService {

    private ZoneDAO zoneDAO = new ZoneDAO();
    private VehicleDAO vehicleDAO = new VehicleDAO();
    private PickupDAO pickupDAO = new PickupDAO();

    public boolean registerZone(Zone z) throws ValidationException {
        if (z.getZoneID() == null || z.getZoneID().trim().isEmpty()) {
            throw new ValidationException("Zone ID cannot be empty");
        }
        if (z.getZoneName() == null || z.getZoneName().trim().isEmpty()) {
            throw new ValidationException("Zone name cannot be empty");
        }
        if (z.getAreaSqKm() <= 0) {
            throw new ValidationException("Area must be positive");
        }

        Zone existing = zoneDAO.findZone(z.getZoneID());
        if (existing != null) {
            throw new ValidationException("Zone ID already exists");
        }

        return zoneDAO.insertZone(z);
    }

    public boolean registerVehicle(Vehicle v) throws ValidationException {
        if (v.getVehicleID() == null || v.getVehicleID().trim().isEmpty()) {
            throw new ValidationException("Vehicle ID cannot be empty");
        }
        if (v.getVehicleType() == null || v.getVehicleType().trim().isEmpty()) {
            throw new ValidationException("Vehicle type cannot be empty");
        }
        if (v.getCapacityKg() <= 0) {
            throw new ValidationException("Capacity must be positive");
        }

        Vehicle existing = vehicleDAO.findVehicle(v.getVehicleID());
        if (existing != null) {
            throw new ValidationException("Vehicle ID already exists");
        }

        return vehicleDAO.insertVehicle(v);
    }

    public boolean schedulePickup(String zoneID, String vehicleID, Date date,
                                  String startTime, int expectedVolume)
            throws ValidationException, VehicleNotSuitableException {

        if (zoneID == null || zoneID.trim().isEmpty()) {
            throw new ValidationException("Zone ID required");
        }
        if (vehicleID == null || vehicleID.trim().isEmpty()) {
            throw new ValidationException("Vehicle ID required");
        }
        if (date == null) {
            throw new ValidationException("Pickup date required");
        }
        if (startTime == null || startTime.trim().isEmpty()) {
            throw new ValidationException("Start time required");
        }
        if (expectedVolume <= 0) {
            throw new ValidationException("Expected volume must be > 0");
        }

        Zone zone = zoneDAO.findZone(zoneID);
        Vehicle vehicle = vehicleDAO.findVehicle(vehicleID);

        if (zone == null || vehicle == null) {
            return false;
        }

        if (!vehicle.getSuitability().equalsIgnoreCase(zone.getZoneType())) {
            throw new VehicleNotSuitableException("Vehicle not suitable for zone type");
        }

        List<?> conflicts = pickupDAO.findConflicts(vehicleID, date, startTime);
        if (!conflicts.isEmpty()) {
            throw new ValidationException("Conflicting pickup exists for vehicle/time");
        }

        Connection con = null;
        try {
            con = DBUtil.getDBConnection();
            int newId = pickupDAO.generatePickupID();

            Pickup p = new Pickup();
            p.setPickupID(newId);
            p.setZoneID(zoneID);
            p.setVehicleID(vehicleID);
            p.setPickupDate(date);
            p.setStartTime(startTime);
            p.setExpectedVolumeKg(expectedVolume);
            p.setActualVolumeKg(null);
            p.setStatus("SCHEDULED");

            boolean ok = pickupDAO.insertPickup(con, p);
            if (ok) {
                con.commit();
            } else {
                con.rollback();
            }
            return ok;
        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (con != null) {
                try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return false;
    }

    public boolean completePickup(int id, int actualVolume) throws ValidationException {
        if (id <= 0) {
            throw new ValidationException("Pickup ID invalid");
        }
        if (actualVolume < 0) {
            throw new ValidationException("Actual volume cannot be negative");
        }

        Pickup existing = new PickupDAO().findById(id);
        if (existing == null) {
            return false;
        }
        if (!"SCHEDULED".equalsIgnoreCase(existing.getStatus())) {
            throw new ValidationException("Pickup not in SCHEDULED status");
        }

        Connection con = null;
        try {
            con = DBUtil.getDBConnection();
            boolean ok = pickupDAO.updateCompletion(con, id, actualVolume);
            if (ok) {
                con.commit();
            } else {
                con.rollback();
            }
            return ok;
        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (con != null) {
                try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return false;
    }

    public boolean removeZone(String zoneID) throws ActivePickupExistsException {
        List<?> active = pickupDAO.findActiveByZone(zoneID);
        if (!active.isEmpty()) {
            throw new ActivePickupExistsException("Active pickups exist for zone " + zoneID);
        }
        return zoneDAO.deleteZone(zoneID);
    }

    public boolean removeVehicle(String vehicleID) throws ActivePickupExistsException {
        List<?> active = pickupDAO.findActiveByVehicle(vehicleID);
        if (!active.isEmpty()) {
            throw new ActivePickupExistsException("Active pickups exist for vehicle " + vehicleID);
        }
        return vehicleDAO.deleteVehicle(vehicleID);
    }
}