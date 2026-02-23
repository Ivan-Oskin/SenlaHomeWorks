package com.oskin.autoservice.dto;

public class OrderMasterDto {
    private int id;
    private MasterDto masterDto;
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
