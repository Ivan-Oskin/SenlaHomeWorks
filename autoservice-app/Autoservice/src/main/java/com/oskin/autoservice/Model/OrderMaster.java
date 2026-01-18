package com.oskin.autoservice.Model;

public class OrderMaster {
    private int id;
    private Master master;
    private Order order;

    public OrderMaster(int id, Order order, Master master){
        this.id = id;
        this.master = master;
        this.order = order;
    }


}
