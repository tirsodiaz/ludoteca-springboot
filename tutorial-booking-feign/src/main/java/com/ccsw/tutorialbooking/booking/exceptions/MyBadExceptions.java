package com.ccsw.tutorialbooking.booking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MyBadExceptions extends Exception {
    public MyBadExceptions(String message) {
        super(message);
    }
}
