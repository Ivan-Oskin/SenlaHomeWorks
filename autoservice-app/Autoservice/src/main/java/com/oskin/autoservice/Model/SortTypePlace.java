package com.oskin.autoservice.model;

public enum SortTypePlace implements SortType {
    ID("id"),
    NAME("name");

    private String StringSortType;

    SortTypePlace(String stringSortType) {
        this.StringSortType = stringSortType;
    }

    public String getStringSortType() {
        return this.StringSortType;
    }
}
