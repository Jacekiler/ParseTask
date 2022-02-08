package com.example.parseTask.exceptions.handling;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> jsonProcessingException(JsonProcessingException exception){
        return ResponseEntity.internalServerError()
                .body(exception.getMessage());
    }
}
