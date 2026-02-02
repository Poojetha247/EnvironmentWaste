package com.waste.util;

public class ActivePickupExistsException extends Exception {

    public ActivePickupExistsException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "ActivePickupExistsException: " + getMessage();
    }
}