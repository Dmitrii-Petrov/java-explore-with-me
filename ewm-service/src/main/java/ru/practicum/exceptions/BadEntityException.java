package ru.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BadEntityException extends RuntimeException {
    public BadEntityException(String s) {
        super(s);
    }
}
