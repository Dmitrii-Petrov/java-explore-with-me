package ru.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class FailNameException extends RuntimeException {
    public FailNameException(String s) {
        super(s);
    }
}
