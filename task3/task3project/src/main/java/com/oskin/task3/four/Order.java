package com.oskin.task3.four;

import java.time.LocalDateTime;

public class Order extends ObjectName {
    private String customer;
    private String status;
    private LocalDateTime time_start;
    private LocalDateTime time_complete;

    void close() {
        this.status = "close";
    }

    void cancel() {
        this.status = "cancel";
    }

    String getStatus() {
        return this.status;
    }

    String getCustomer() {
        return this.customer;
    }

    String getTime() {
        return "start: " + this.time_start + "\n" + "complete: " + this.time_complete;
    }


    Order(String name, String customer, LocalDateTime time_start, LocalDateTime time_complete) {
        super(name);
        this.customer = customer;
        this.time_start = time_start;
        this.time_complete = time_complete;
        this.status = "active";
    }

    void changeDay(int day) {
        this.time_start = time_start.plusDays(day);
        this.time_complete = time_complete.plusDays(day);
        System.out.println("Change:\nstart: " + time_start + "\ncomplete: " + time_complete);
    }

    void changeHour(int hour) {
        this.time_start = time_start.plusHours(hour);
        this.time_complete = time_complete.plusHours(hour);
        System.out.println("Change:\nstart: " + time_start + "\ncomplete: " + time_complete);
    }
}
