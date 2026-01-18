package com.oskin.autoservice.Model;

public enum SortTypeOrder {
    CREATE("timeCreate"),
    START("timeStart"),
    COMPLETE("timeComplete"),
    COST("cost"),
    ID("id");

    private String sortType;

    SortTypeOrder(String sortType){
        this.sortType = sortType;
    }

    public String getStringSortType(){
        return this.sortType;
    }
}
