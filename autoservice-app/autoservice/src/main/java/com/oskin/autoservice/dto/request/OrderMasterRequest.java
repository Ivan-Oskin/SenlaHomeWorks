package com.oskin.autoservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderMasterRequest {
    @JsonProperty("order_id")
    private int orderId;
    @JsonProperty("master_id")
    private int masterId;

    public OrderMasterRequest() {

    }

    public OrderMasterRequest(int orderId, int masterId) {
        this.masterId = masterId;
        this.orderId = orderId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getMasterId() {
        return masterId;
    }

    public int getOrderId() {
        return orderId;
    }
}
