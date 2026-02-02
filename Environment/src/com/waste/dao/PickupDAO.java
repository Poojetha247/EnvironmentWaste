package com.waste.dao;

import com.waste.bean.Pickup;
import com.waste.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PickupDAO {

    // Generate Pickup ID
    public int generatePickupID() {
        String sql = "SELECT NVL(MAX(PICKUP_ID), 70000) + 1 FROM PICKUP_TBL";
        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 70001;
    }

    // Insert Pickup
    public boolean insertPickup(Connection con, Pickup p) throws SQLException {

        String sql =
                "INSERT INTO PICKUP_TBL " +
                "(PICKUP_ID, ZONE_ID, VEHICLE_ID, PICKUP_DATE, START_TIME, " +
                "EXPECTED_VOLUME_KG, ACTUAL_VOLUME_KG, STATUS) " +
                "VALUES (?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getPickupID());
            ps.setString(2, p.getZoneID());
            ps.setString(3, p.getVehicleID());
            ps.setDate(4, p.getPickupDate());
            ps.setString(5, p.getStartTime());
            ps.setInt(6, p.getExpectedVolumeKg());

            if (p.getActualVolumeKg() == null) {
                ps.setNull(7, Types.INTEGER);
            } else {
                ps.setInt(7, p.getActualVolumeKg());
            }

            ps.setString(8, p.getStatus());

            return ps.executeUpdate() == 1;
        }
    }

    // Update pickup completion
    public boolean updateCompletion(Connection con, int id, int actualVol) throws SQLException {

        String sql =
                "UPDATE PICKUP_TBL " +
                "SET ACTUAL_VOLUME_KG = ?, STATUS = 'COMPLETED' " +
                "WHERE PICKUP_ID = ? AND STATUS = 'SCHEDULED'";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, actualVol);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        }
    }

    // Find active pickups by zone
    public List<Pickup> findActiveByZone(String zoneID) {

        List<Pickup> list = new ArrayList<>();

        String sql =
                "SELECT PICKUP_ID, ZONE_ID, VEHICLE_ID, PICKUP_DATE, START_TIME, " +
                "EXPECTED_VOLUME_KG, ACTUAL_VOLUME_KG, STATUS " +
                "FROM PICKUP_TBL WHERE ZONE_ID = ? AND STATUS = 'SCHEDULED'";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, zoneID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapPickup(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Find active pickups by vehicle
    public List<Pickup> findActiveByVehicle(String vehicleID) {

        List<Pickup> list = new ArrayList<>();

        String sql =
                "SELECT PICKUP_ID, ZONE_ID, VEHICLE_ID, PICKUP_DATE, START_TIME, " +
                "EXPECTED_VOLUME_KG, ACTUAL_VOLUME_KG, STATUS " +
                "FROM PICKUP_TBL WHERE VEHICLE_ID = ? AND STATUS = 'SCHEDULED'";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, vehicleID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapPickup(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Conflict check
    public List<Pickup> findConflicts(String vehicleID, Date date, String startTime) {

        List<Pickup> list = new ArrayList<>();

        String sql =
                "SELECT PICKUP_ID, ZONE_ID, VEHICLE_ID, PICKUP_DATE, START_TIME, " +
                "EXPECTED_VOLUME_KG, ACTUAL_VOLUME_KG, STATUS " +
                "FROM PICKUP_TBL " +
                "WHERE VEHICLE_ID = ? AND PICKUP_DATE = ? " +
                "AND START_TIME = ? AND STATUS = 'SCHEDULED'";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, vehicleID);
            ps.setDate(2, date);
            ps.setString(3, startTime);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapPickup(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Find pickup by ID
    public Pickup findById(int id) {

        String sql =
                "SELECT PICKUP_ID, ZONE_ID, VEHICLE_ID, PICKUP_DATE, START_TIME, " +
                "EXPECTED_VOLUME_KG, ACTUAL_VOLUME_KG, STATUS " +
                "FROM PICKUP_TBL WHERE PICKUP_ID = ?";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapPickup(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Map ResultSet to Pickup object
    private Pickup mapPickup(ResultSet rs) throws SQLException {

        Pickup p = new Pickup();
        p.setPickupID(rs.getInt("PICKUP_ID"));
        p.setZoneID(rs.getString("ZONE_ID"));
        p.setVehicleID(rs.getString("VEHICLE_ID"));
        p.setPickupDate(rs.getDate("PICKUP_DATE"));
        p.setStartTime(rs.getString("START_TIME"));
        p.setExpectedVolumeKg(rs.getInt("EXPECTED_VOLUME_KG"));

        int actualVol = rs.getInt("ACTUAL_VOLUME_KG");
        if (rs.wasNull()) {
            p.setActualVolumeKg(null);
        } else {
            p.setActualVolumeKg(actualVol);
        }

        p.setStatus(rs.getString("STATUS"));
        return p;
    }
}