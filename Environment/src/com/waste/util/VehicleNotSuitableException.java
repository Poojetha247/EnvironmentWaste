package com.waste.util;

public class VehicleNotSuitableException extends Exception {

    public VehicleNotSuitableException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "VehicleNotSuitableException: " + getMessage();
    }
}