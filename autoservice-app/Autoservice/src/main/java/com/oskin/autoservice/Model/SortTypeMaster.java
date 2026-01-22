package com.oskin.autoservice.Model;

public enum SortTypeMaster implements SortType {
    ID("id"),
    ALPHABET("name"),
    BUSYNESS("busy");

    String sortType;

    SortTypeMaster(String sortType) {
        this.sortType = sortType;
    }

    public String getStringSortType() {
        return this.sortType;
    }
}
