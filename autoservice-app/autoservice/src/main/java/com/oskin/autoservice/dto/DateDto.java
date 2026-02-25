package com.oskin.autoservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class DateDto {
    @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private LocalDateTime date;

    public DateDto() {

    }

    public DateDto(LocalDateTime localDateTime) {
        this.date = localDateTime;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
