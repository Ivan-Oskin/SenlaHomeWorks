package com.oskin.autoservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class OrderRequest {
    private String name;
    private int cost;
    @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    @JsonProperty("time_start")
    private LocalDateTime timeStart;
    @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    @JsonProperty("time_complete")
    private LocalDateTime timeComplete;
    @JsonProperty("place_id")
    private int placeId;

    OrderRequest() {

    }
    OrderRequest(String name, int cost, LocalDateTime timeStart, LocalDateTime timeComplete, int placeId) {
        this.name = name;
        this.cost = cost;
        this.timeStart = timeStart;
        this.timeComplete = timeComplete;
        this.placeId = placeId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public LocalDateTime getTimeComplete() {
        return timeComplete;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }
}


