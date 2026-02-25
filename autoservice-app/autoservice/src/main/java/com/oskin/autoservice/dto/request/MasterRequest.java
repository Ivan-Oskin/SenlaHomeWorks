package com.oskin.autoservice.dto.request;

public class MasterRequest {
    String name;
    public MasterRequest() {

    }
    public MasterRequest(String name) {
        this.name = name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
}
