package com.oskin.autoservice.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;

public class Master implements Nameable {
    private int id;
    private String name;
    private ArrayList<String> namesOfOrder;
    private ArrayList<Integer> idOfOrders;

    public Master(){
    }
    public Master(int id, String name) {
        this.name = name;
        this.namesOfOrder = new ArrayList<>();
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
    public void addArrayOrdersName(ArrayList<String> OrdersName) {
        this.namesOfOrder = OrdersName;
    }
    public void addArrayOrdersId(ArrayList<Integer> OrdersId) {
        this.idOfOrders = OrdersId;
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
    public ArrayList<Integer> getIdOfOrder() {
        return idOfOrders;
    }
    @JsonIgnore
    public int getCountOfOrdersName() {
        return namesOfOrder.size();
    }
    @JsonIgnore
    public int getCountOfOrdersId() {
        return idOfOrders.size();
    }
}
