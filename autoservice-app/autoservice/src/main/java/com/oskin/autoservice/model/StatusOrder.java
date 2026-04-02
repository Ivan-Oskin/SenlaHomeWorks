package com.oskin.autoservice.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusOrder {
    ACTIVE("Active"),
    CANCEL("Cancel"),
    CLOSE("Close");

    @JsonValue
    private final String STATUS;

    StatusOrder(String status) {
        this.STATUS = status;
    }

    public String getSTATUS() {
        return this.STATUS;
    }
}
