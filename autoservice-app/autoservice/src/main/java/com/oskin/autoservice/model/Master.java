package com.oskin.autoservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "masters")
public class Master implements IIndentified {
    @Id
    private int id;
    @Column(name = "name")
    private String name;

    public Master() {
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
}
