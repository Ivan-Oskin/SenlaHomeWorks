package com.oskin.autoservice.model;

public class Place implements IIndentified {
    private String name;
    private int id;

    public Place() {
    }

    public Place(int id, String name) {

        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
