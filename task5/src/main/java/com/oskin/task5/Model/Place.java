package com.oskin.task5.Model;

public class Place implements Nameable {
    private String name;

    Place(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
