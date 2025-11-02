package com.oskin.task4.one;

import java.time.LocalDateTime;

public class Order implements Nameable {
    private String name;

    private String status;
    private LocalDateTime timeCreate;
    private LocalDateTime timeStart;
    private LocalDateTime timeComplete;
    private int cost;
    private Place place;


    void close() {
        this.status = "close";
    }

    void cancel() {
        this.status = "cancel";
    }

    public String getName() {
        return this.name;
    }

    public String getStatus() {
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
        this.status = "active";
        this.place = place;
    }

    public void changeDay(int day) {
        this.timeStart = timeStart.plusDays(day);
        this.timeComplete = timeComplete.plusDays(day);
        System.out.println("Change:\nstart: " + timeStart + "\ncomplete: " + timeComplete);
    }

    public void changeHour(int hour) {
        this.timeStart = timeStart.plusHours(hour);
        this.timeComplete = timeComplete.plusHours(hour);
        System.out.println("Change:\nstart: " + timeStart + "\ncomplete: " + timeComplete);
    }
}
