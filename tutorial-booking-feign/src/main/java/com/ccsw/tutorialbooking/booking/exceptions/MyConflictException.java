package com.ccsw.tutorialbooking.booking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MyConflictException extends Exception {
    public MyConflictException(String message) {
        super(message);
    }
}
