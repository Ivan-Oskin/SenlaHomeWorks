package com.oskin.autoservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "order_master")
public class OrderMaster implements IIdentified {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "master_id")
    private Master master;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderMaster() {

    }

    public OrderMaster(Order order, Master master) {
        this.master = master;
        this.order = order;
    }

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
