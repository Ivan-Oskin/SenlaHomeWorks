package com.oskin.autoservice.Model;

public class OrderMaster implements IIndentified {
    private int id;
    private Master master;
    private Order order;

    public OrderMaster(int id, Order order, Master master) {
        this.id = id;
        this.master = master;
        this.order = order;
    }

    public Order getOrder() {
        return this.order;
    }

    public Master getMaster() {
        return this.master;
    }

    public int getId() {
        return this.id;
    }
}
