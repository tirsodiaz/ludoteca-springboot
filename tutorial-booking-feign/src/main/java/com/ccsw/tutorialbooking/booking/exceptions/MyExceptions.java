package com.ccsw.tutorialbooking.booking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MyExceptions extends Exception {
    public MyExceptions(String message) {
        super(message);
    }
}
