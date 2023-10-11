package com.ccsw.tutorialcustomer.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MyConflictException extends Exception {
    public MyConflictException(String message) {
        super(message);
    }
}
