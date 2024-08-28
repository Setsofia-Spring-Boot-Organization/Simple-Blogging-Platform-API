package com.example.blogging.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BlogPostException.class)
    public ResponseEntity<ErrorResponse> handleBlogPostExceptions(BlogPostException exception) {
        HttpStatus status = HttpStatus.OK; // set the default status
        Causes cause = exception.cause;

        switch (cause) {
            case NO_EMPTY_FIELDS_ALLOWED -> status = HttpStatus.BAD_REQUEST;
        }

         ErrorResponse errorResponse = new ErrorResponse(status.value(), exception.getMessage(), exception.getCause().getMessage());
         return new ResponseEntity<>(errorResponse, status);
    }
}
