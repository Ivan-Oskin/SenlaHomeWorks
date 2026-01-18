package com.oskin.autoservice.DAO;

public enum NameTables {
    ORDER("orders"),
    PLACE("places"),
    MASTER("masters"),
    ORDERS_BY_MASTER("order_master");

    private final String NAME_TABLE;

    NameTables(String NAME_TABLE){
        this.NAME_TABLE = NAME_TABLE;
    }

    public String getNAME_TABLE(){
        return this.NAME_TABLE;
    }
}
