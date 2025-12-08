package com.oskin.autoservice.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class Master implements Nameable {
    private int id;
    private String name;
    private ArrayList<String> namesOfOrder;

    public Master(){
    }

    public Master(int id, String name) {
        this.name = name;
        this.namesOfOrder = new ArrayList<>();
        this.id = id;
    }
    public Master(int id, String name, ArrayList<String> listOfOrders) {
        this.name = name;
        this.namesOfOrder = new ArrayList<>(listOfOrders);
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
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


    @JsonIgnore
    public int getCountOfOrders() {
        return namesOfOrder.size();
    }


}
