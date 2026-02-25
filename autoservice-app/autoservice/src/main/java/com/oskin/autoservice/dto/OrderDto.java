package com.oskin.autoservice.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oskin.autoservice.model.StatusOrder;
import java.time.LocalDateTime;

public class OrderDto {
    private int id;
    private String name;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonProperty("time_create")
    private LocalDateTime timeCreate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonProperty("time_start")
    private LocalDateTime timeStart;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonProperty("time_complete")
    private LocalDateTime timeComplete;
    private int cost;
    @JsonProperty("place")
    private PlaceDto placeDto;

    public int getId() {
        return this.id;
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
    public PlaceDto getPlaceDto() {
        return this.placeDto;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setStatus(StatusOrder statusOrder) {
        this.status = statusOrder.getSTATUS();
    }
    public void setTimeCreate(LocalDateTime timeCreate) {
        this.timeCreate = timeCreate;
    }
    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }
    public void setTimeComplete(LocalDateTime timeComplete) {
        this.timeComplete = timeComplete;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
    public void setPlaceDto(PlaceDto placeDto) {
        this.placeDto = placeDto;
    }
}
