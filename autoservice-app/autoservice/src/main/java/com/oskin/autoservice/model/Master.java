package com.oskin.autoservice.model;

public class Master implements IIndentified {
    private int id;
    private String name;

    public Master() {
    }

    public Master(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }
}
