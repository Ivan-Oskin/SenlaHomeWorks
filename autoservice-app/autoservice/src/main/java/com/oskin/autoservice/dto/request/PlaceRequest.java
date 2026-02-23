package com.oskin.autoservice.dto.request;

public class PlaceRequest {
    private String name;

    public PlaceRequest(String name) {
        this.name = name;
    }
    public PlaceRequest() {
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
}
