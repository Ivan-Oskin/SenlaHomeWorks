package com.oskin.autoservice.Model;
import java.util.ArrayList;

public class Master {
    private int id;
    private String name;
    private ArrayList<Integer> idOfOrders;

    public Master(){
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

    public void addArrayOrdersId(ArrayList<Integer> OrdersId) {
        this.idOfOrders = OrdersId;
    }
    public ArrayList<Integer> getIdOfOrder() {
        return idOfOrders;
    }
    public int getCountOfOrdersId() {
        return idOfOrders.size();
    }
}
