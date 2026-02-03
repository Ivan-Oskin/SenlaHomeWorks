package com.oskin.autoservice.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "places")
public class Place implements IIndentified {
    @Id
    private int id;
    @Column(name = "name")
    private String name;

    public Place() {
    }

    public Place(int id, String name) {

        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
