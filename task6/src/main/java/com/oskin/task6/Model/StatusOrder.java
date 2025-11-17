package com.oskin.task6.Model;

public enum StatusOrder {
    ACTIVE("Active"),
    CANCEL("Cancel"),
    CLOSE("Close");

    private final String STATUS;

    StatusOrder(String STATUS){
        this.STATUS = STATUS;
    }

    public String getSTATUS(){
        return this.STATUS;
    }
}
