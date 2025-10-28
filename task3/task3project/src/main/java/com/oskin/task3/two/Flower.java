package com.oskin.task3.two;

public abstract class Flower {
    private String name;
    private double price;
    private String color;

    public Flower(String name, String color, double price) {
        this.name = name;
        this.price = price;
        this.color = color;

    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public double getPrice() {
        return this.price;
    }

}
