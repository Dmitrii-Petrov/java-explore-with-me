package ru.practicum.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptions.BadEntityException;
import ru.practicum.exceptions.FailNameException;
import ru.practicum.exceptions.NotFoundEntityException;
import ru.practicum.exceptions.WrongParamsException;

import java.util.LinkedHashMap;
import java.util.Map;

import static ru.practicum.DateUtils.getErrorTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = {FailNameException.class})
    public ResponseEntity<Map<String, Object>> handleFailNameException(final FailNameException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CONFLICT.name());
        response.put("reason", HttpStatus.CONFLICT.getReasonPhrase());
        response.put("message", ex.getMessage());
        response.put("timestamp", getErrorTime());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(value = {BadEntityException.class})
    public ResponseEntity<Map<String, Object>> handleBadEntityException(final BadEntityException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CONFLICT.name());
        response.put("reason", HttpStatus.CONFLICT.getReasonPhrase());
        response.put("message", ex.getMessage());
        response.put("timestamp", getErrorTime());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(value = {WrongParamsException.class})
    public ResponseEntity<Map<String, Object>> handleWrongParamsException(final WrongParamsException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.name());
        response.put("reason", HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.put("message", ex.getMessage());
        response.put("timestamp", getErrorTime());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {NotFoundEntityException.class})
    public ResponseEntity<Map<String, Object>> handleNotFoundEntityException(final NotFoundEntityException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.NOT_FOUND.name());
        response.put("reason", HttpStatus.NOT_FOUND.getReasonPhrase());
        response.put("message", ex.getMessage());
        response.put("timestamp", getErrorTime());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }
}
