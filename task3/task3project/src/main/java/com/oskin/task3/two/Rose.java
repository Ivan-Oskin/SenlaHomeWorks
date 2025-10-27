package com.oskin.task3.two;

public class Rose extends Flower {
    private boolean needles;

    public Rose(String color, int price, boolean needles) {
        super("rose", color, needles ? price + 50 : price);
        this.needles = needles;
    }

    public boolean isNeedles() {
        return this.needles;
    }
}
