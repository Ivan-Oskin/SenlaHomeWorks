package com.oskin.autoservice.Model;

public enum SortTypeOrderMaster implements SortType {
    ID("id"),
    MASTER_ID("master_id"),
    ORDER_ID("order_id");

    private String sortType;

    SortTypeOrderMaster(String sortType) {
        this.sortType = sortType;
    }

    public String getStringSortType() {
        return this.sortType;
    }
}
