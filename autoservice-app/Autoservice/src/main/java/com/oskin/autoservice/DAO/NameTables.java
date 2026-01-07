package com.oskin.autoservice.DAO;

public enum NameTables {
    ORDER("Orders"),
    PLACE("Places"),
    MASTER("Masters"),
    ORDERS_BY_MASTER("OrdersByMaster");

    private final String NAME_TABLE;

    NameTables(String NAME_TABLE){
        this.NAME_TABLE = NAME_TABLE;
    }

    public String getNAME_TABLE(){
        return this.NAME_TABLE;
    }
}
