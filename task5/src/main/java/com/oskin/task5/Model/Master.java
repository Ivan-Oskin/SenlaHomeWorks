package com.oskin.task5.Model;

import java.util.ArrayList;

public class Master implements Nameable {
    private String name;
    private ArrayList<String> namesOfOrder;


    Master(String name) {
        this.name = name;
        this.namesOfOrder = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }


    public void addOrder(Order order) {
        namesOfOrder.add(order.getName());
    }

    public void deleteOrder(Order order) {
        int i = namesOfOrder.indexOf(order.getName());
        if (i >= 0) {
            namesOfOrder.remove(i);
        }
    }

    public ArrayList<String> getNamesOfOrder() {
        return namesOfOrder;
    }

    public int getCountOfOrders() {
        return namesOfOrder.size();
    }


}
