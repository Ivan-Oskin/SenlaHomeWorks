package com.oskin.autoservice.dto.request;

public class OffsetRequest {
    private int day;
    private int hour;

    public OffsetRequest() {

    }

    public OffsetRequest(int day, int hour) {
        this.day = day;
        this.hour = hour;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
