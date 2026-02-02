package com.waste.app;

import com.waste.bean.Vehicle;
import com.waste.bean.Zone;
import com.waste.service.WasteService;
import com.waste.util.ValidationException;
import com.waste.util.VehicleNotSuitableException;

import java.sql.Date;

public class WasteMain {
	 private static WasteService service = new WasteService();
	 public static void main(String[] args) throws ValidationException, VehicleNotSuitableException {
	 // DEMO 1: Register Zone
	 Zone z = new Zone();
	 z.setZoneID("ZN39");
	 z.setZoneName("Commercial Hub West");
	 z.setZoneType("Commercial");
	 z.setAreaSqKm(4.2);
	 z.setStatus("ACTIVE");
	 service.registerZone(z);
	 System.out.println("Zone Registered");
	 // DEMO 2: Register Vehicle
	 Vehicle v = new Vehicle();
	 v.setVehicleID("VH55");
	 v.setVehicleType("Mini-Truck");
	 v.setCapacityKg(1200);
	 v.setSuitability("Commercial");
	 v.setStatus("AVAILABLE");
	 service.registerVehicle(v);
	 System.out.println("Vechile Registered");
	 // DEMO 3: Schedule Pickup
	 java.sql.Date d = new 
	java.sql.Date(System.currentTimeMillis());
	 service.schedulePickup("ZN39","VH55",d,"06:30",900);
	 System.out.println("Pickup Scheduled");
	 }
	}