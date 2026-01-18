package com.oskin.autoservice.Model;

import java.time.LocalDateTime;

public class Order implements IIndentified {
    private int id;
    private String name;
    private StatusOrder status;
    private LocalDateTime timeCreate;
    private LocalDateTime timeStart;
    private LocalDateTime timeComplete;
    private int cost;
    private Place place;


    public void close() {
        this.status = StatusOrder.CLOSE;
    }

    public void cancel() {
        this.status = StatusOrder.CANCEL;
    }

    public String getName() {
        return this.name;
    }

    public StatusOrder getStatus() {
        return this.status;
    }

    public LocalDateTime getTimeCreate() {
        return this.timeCreate;
    }

    public LocalDateTime getTimeStart() {
        return this.timeStart;
    }

    public LocalDateTime getTimeComplete() {
        return this.timeComplete;
    }

    public int getCost() {
        return this.cost;
    }

    public Place getPlace() {
        return this.place;
    }

    public int getId(){
        return this.id;
    }

    public Order(){

    }

    public Order(int id, String name, int cost, Place place, LocalDateTime timeCreate, LocalDateTime timeStart, LocalDateTime timeComplete) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.timeCreate = timeCreate;
        this.timeStart = timeStart;
        this.timeComplete = timeComplete;
        this.status = StatusOrder.ACTIVE;
        this.place = place;
    }
    public Order(int id, String name, int cost, Place place, LocalDateTime timeCreate, LocalDateTime timeStart, LocalDateTime timeComplete, StatusOrder status) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.timeCreate = timeCreate;
        this.timeStart = timeStart;
        this.timeComplete = timeComplete;
        this.status = status;
        this.place = place;
    }
}
