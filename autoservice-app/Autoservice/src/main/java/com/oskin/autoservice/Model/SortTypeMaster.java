package com.oskin.autoservice.Model;

public enum SortTypeMaster {
    ID("id"),
    ALPHABET("name"),
    BUSYNESS("busy");

    String sortType;

    SortTypeMaster(String sortType){
        this.sortType = sortType;
    }

    public String getStringSortType(){
        return this.sortType;
    }
}
