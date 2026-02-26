package com.oskin.autoservice.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ExceptionResponse {
    private final int status;
    private final String message;
    @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private final LocalDateTime timestamp;

    public ExceptionResponse(int status, String message) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
