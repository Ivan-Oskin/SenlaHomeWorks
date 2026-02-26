package com.oskin.autoservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderMasterDto {
    private int id;
    @JsonProperty("master")
    private MasterDto masterDto;
    @JsonProperty("order")
    private OrderDto orderDto;

    public int getId() {
        return this.id;
    }

    public MasterDto getMasterDto() {
        return this.masterDto;
    }

    public OrderDto getOrderDto() {
        return this.orderDto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMasterDto(MasterDto masterDto) {
        this.masterDto = masterDto;
    }

    public void setOrderDto(OrderDto orderDto) {
        this.orderDto = orderDto;
    }
}
