package com.oskin.task3.two;

public class Tulip extends Flower {
    private boolean openedBud;

    public Tulip(String color, int price, boolean openedBud) {
        super("tulip", color, price);
        this.openedBud = openedBud;
    }

    public boolean isOpenedBud() {
        return openedBud;
    }
}
