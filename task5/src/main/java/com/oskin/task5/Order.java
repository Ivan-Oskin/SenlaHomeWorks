package com.oskin.task5;

import java.time.LocalDateTime;

public class Order implements Nameable {
    private String name;
    private StatusOrder status;
    private LocalDateTime timeCreate;
    private LocalDateTime timeStart;
    private LocalDateTime timeComplete;
    private int cost;
    private Place place;


    void close() {
        this.status = StatusOrder.CLOSE;
    }

    void cancel() {
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


    Order(String name, int cost, Place place, LocalDateTime timeCreate, LocalDateTime timeStart, LocalDateTime timeComplete) {
        this.name = name;
        this.cost = cost;
        this.timeCreate = timeCreate;
        this.timeStart = timeStart;
        this.timeComplete = timeComplete;
        this.status = StatusOrder.ACTIVE;
        this.place = place;
    }

    public void changeDay(int day) {
        this.timeStart = timeStart.plusDays(day);
        this.timeComplete = timeComplete.plusDays(day);
    }

    public void changeHour(int hour) {
        this.timeStart = timeStart.plusHours(hour);
        this.timeComplete = timeComplete.plusHours(hour);
    }
}
