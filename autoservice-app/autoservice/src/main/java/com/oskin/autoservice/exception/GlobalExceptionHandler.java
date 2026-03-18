package com.oskin.autoservice.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionResponse> handlerNullPointerException(NullPointerException nullPointerException) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
        ExceptionResponse responseBody = new ExceptionResponse(status.value(), "an error occurred, entity no found");
        return new ResponseEntity<>(responseBody, buildHeaders(), status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handlerBadRequestException(MethodArgumentTypeMismatchException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionResponse responseBody = new ExceptionResponse(status.value(), exception.getMessage());
        return new ResponseEntity<>(responseBody, buildHeaders(), status);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponse> handlerNotFoundException(NoHandlerFoundException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionResponse responseBody = new ExceptionResponse(status.value(), exception.getMessage());
        return new ResponseEntity<>(responseBody, buildHeaders(), status);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handler(BadCredentialsException exception) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ExceptionResponse responseBody = new ExceptionResponse(status.value(), exception.getMessage());
        return new ResponseEntity<>(responseBody, buildHeaders(), status);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExceptionResponse> handlerGlobalException(Throwable throwable) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
        ExceptionResponse responseBody = new ExceptionResponse(status.value(), throwable.getMessage());
        return new ResponseEntity<>(responseBody, buildHeaders(), status);
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
