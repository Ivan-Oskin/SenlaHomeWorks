package com.oskin.task4.one;

public class Place implements Nameable {
    private String name;

    Place(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
