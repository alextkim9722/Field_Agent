package learn.field_agent.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>(
                "Something went wrong on our end. Your request failed. :(",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(
                "The message provided is not readable",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleHttpNotReadableException(HttpMediaTypeNotSupportedException ex) {
        return new ResponseEntity<>(
                "The message provided is not supported",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleHttpNotReadableException(NoHandlerFoundException ex) {
        return new ResponseEntity<>(
                "The data you are looking for is not found",
                HttpStatus.NOT_FOUND);
    }
}
