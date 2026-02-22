package com.kristof.assignment.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DogNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleDogNotFoundException(DogNotFoundException ex) {
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("message", ex.getMessage());
        errorMessage.put("status", "404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errorMessage = new HashMap<>();
        String requiredType = (ex.getRequiredType() != null) ? ex.getRequiredType().getSimpleName() : "unknown";
        String message = String.format("Parameter %s should be of type %s",
                ex.getName(), requiredType);
        errorMessage.put("error", "Bad Request");
        errorMessage.put("message", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("error", "Bad Request");
        errorMessage.put("message", ex.getMessage());
        errorMessage.put("status", "400");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "An unexpected error occurred");
        errors.put("message", "Something went wrong. Please try again later.");
        errors.put("status", "500");
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
