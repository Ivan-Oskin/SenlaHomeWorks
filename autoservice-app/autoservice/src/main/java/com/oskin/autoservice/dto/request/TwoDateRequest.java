package com.oskin.autoservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class TwoDateRequest {
    @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private LocalDateTime start;
    @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private LocalDateTime end;

    public TwoDateRequest() {

    }

    TwoDateRequest(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }
}
