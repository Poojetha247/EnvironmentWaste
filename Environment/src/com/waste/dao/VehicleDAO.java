package com.waste.dao;

import com.waste.bean.Vehicle;
import com.waste.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    public Vehicle findVehicle(String vehicleID) {
        String sql = "SELECT VEHICLE_ID, VEHICLE_TYPE, CAPACITY_KG, SUITABILITY, STATUS " +
                     "FROM VEHICLE_TBL WHERE VEHICLE_ID = ?";
        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, vehicleID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Vehicle v = new Vehicle();
                v.setVehicleID(rs.getString(1));
                v.setVehicleType(rs.getString(2));
                v.setCapacityKg(rs.getInt(3));
                v.setSuitability(rs.getString(4));
                v.setStatus(rs.getString(5));
                return v;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertVehicle(Vehicle v) {
        String sql = "INSERT INTO VEHICLE_TBL (VEHICLE_ID, VEHICLE_TYPE, CAPACITY_KG, SUITABILITY, STATUS) " +
                     "VALUES (?,?,?,?,?)";
        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, v.getVehicleID());
            ps.setString(2, v.getVehicleType());
            ps.setInt(3, v.getCapacityKg());
            ps.setString(4, v.getSuitability());
            ps.setString(5, v.getStatus());
            int count = ps.executeUpdate();
            con.commit();
            return count == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteVehicle(String vehicleID) {
        String sql = "DELETE FROM VEHICLE_TBL WHERE VEHICLE_ID = ?";
        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, vehicleID);
            int count = ps.executeUpdate();
            con.commit();
            return count == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Vehicle> viewAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT VEHICLE_ID, VEHICLE_TYPE, CAPACITY_KG, SUITABILITY, STATUS FROM VEHICLE_TBL";
        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vehicle v = new Vehicle();
                v.setVehicleID(rs.getString(1));
                v.setVehicleType(rs.getString(2));
                v.setCapacityKg(rs.getInt(3));
                v.setSuitability(rs.getString(4));
                v.setStatus(rs.getString(5));
                list.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}