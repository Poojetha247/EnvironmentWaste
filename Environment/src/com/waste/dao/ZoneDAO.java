package com.waste.dao;

import com.waste.bean.Zone;
import com.waste.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ZoneDAO {

    public Zone findZone(String zoneID) {
        String sql = "SELECT ZONE_ID, ZONE_NAME, ZONE_TYPE, AREA_SQKM, STATUS FROM ZONE_TBL WHERE ZONE_ID = ?";
        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, zoneID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Zone z = new Zone();
                z.setZoneID(rs.getString(1));
                z.setZoneName(rs.getString(2));
                z.setZoneType(rs.getString(3));
                z.setAreaSqKm(rs.getDouble(4));
                z.setStatus(rs.getString(5));
                return z;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertZone(Zone z) {
        String sql = "INSERT INTO ZONE_TBL (ZONE_ID, ZONE_NAME, ZONE_TYPE, AREA_SQKM, STATUS) VALUES (?,?,?,?,?)";
        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, z.getZoneID());
            ps.setString(2, z.getZoneName());
            ps.setString(3, z.getZoneType());
            ps.setDouble(4, z.getAreaSqKm());
            ps.setString(5, z.getStatus());
            int count = ps.executeUpdate();
            con.commit();
            return count == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteZone(String zoneID) {
        String sql = "DELETE FROM ZONE_TBL WHERE ZONE_ID = ?";
        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, zoneID);
            int count = ps.executeUpdate();
            con.commit();
            return count == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Zone> viewAllZones() {
        List<Zone> list = new ArrayList<>();
        String sql = "SELECT ZONE_ID, ZONE_NAME, ZONE_TYPE, AREA_SQKM, STATUS FROM ZONE_TBL";
        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Zone z = new Zone();
                z.setZoneID(rs.getString(1));
                z.setZoneName(rs.getString(2));
                z.setZoneType(rs.getString(3));
                z.setAreaSqKm(rs.getDouble(4));
                z.setStatus(rs.getString(5));
                list.add(z);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
        
        
    }
}
