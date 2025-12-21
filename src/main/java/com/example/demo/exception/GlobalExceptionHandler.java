package com.example.demo.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ApiError(LocalDateTime.now(), 404, "NOT_FOUND", ex.getMessage(), req.getRequestURI()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ApiError(LocalDateTime.now(), 400, "BAD_REQUEST", ex.getMessage(), req.getRequestURI()),
                HttpStatus.BAD_REQUEST
        );
    }
}
