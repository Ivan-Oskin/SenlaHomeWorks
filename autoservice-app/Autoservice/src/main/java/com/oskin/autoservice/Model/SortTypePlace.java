package com.oskin.autoservice.Model;

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
