package com.oskin.autoservice.model;

public enum SortTypeMaster implements SortType {
    ID("id"),
    ALPHABET("name"),
    BUSYNESS("busy");

    private final String sortType;

    SortTypeMaster(String sortType) {
        this.sortType = sortType;
    }

    public String getStringSortType() {
        return this.sortType;
    }
}
