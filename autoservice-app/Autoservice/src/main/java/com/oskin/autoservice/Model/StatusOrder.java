package com.oskin.autoservice.model;

public enum StatusOrder {
    ACTIVE("Active"),
    CANCEL("Cancel"),
    CLOSE("Close");

    private final String STATUS;

    StatusOrder(String status) {
        this.STATUS = status;
    }

    public String getSTATUS() {
        return this.STATUS;
    }
}
