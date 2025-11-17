package com.oskin.task6.Model;

public enum FileName {
    ORDER("Order"),
    GARAGE("Garage"),
    MASTER("Master");

    private final String NAME;

    FileName(String NAME) {
        this.NAME = NAME;
    }

    public String getNAME(){
        return this.NAME;
    }
}
