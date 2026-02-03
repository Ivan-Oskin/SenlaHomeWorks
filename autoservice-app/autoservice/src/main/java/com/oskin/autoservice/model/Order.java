package com.oskin.autoservice.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

import java.time.LocalDateTime;


@Entity
@Table(name = "orders")
public class Order implements IIndentified {
    @Id
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusOrder status;
    @Column(name = "timeCreate")
    private LocalDateTime timeCreate;
    @Column(name = "timeStart")
    private LocalDateTime timeStart;
    @Column(name = "timeComplete")
    private LocalDateTime timeComplete;
    @Column(name = "cost")
    private int cost;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
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

    public int getId() {
        return this.id;
    }

    public Order() {

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
