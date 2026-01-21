package com.oskin.autoservice.Model;

public enum SortTypePlace implements SortType {
    ID("id"),
    NAME("name");

    private String StringSortType;

    SortTypePlace(String StringSortType){
        this.StringSortType = StringSortType;
    }

    public String getStringSortType(){
        return this.StringSortType;
    }
}
