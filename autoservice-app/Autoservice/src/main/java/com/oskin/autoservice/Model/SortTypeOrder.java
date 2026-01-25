package com.oskin.autoservice.model;

public enum SortTypeOrder implements SortType {
    CREATE("timeCreate"),
    START("timeStart"),
    COMPLETE("timeComplete"),
    COST("cost"),
    ID("id");

    private String sortType;

    SortTypeOrder(String sortType) {
        this.sortType = sortType;
    }

    public String getStringSortType() {
        return this.sortType;
    }
}
